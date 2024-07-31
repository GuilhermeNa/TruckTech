package br.com.apps.usecase.usecase

import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.PaymentRequestStatusType
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.UserRepository
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.Response
import br.com.apps.usecase.util.awaitValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.InvalidParameterException

/**
 * Use case class responsible for handling business logic related to payment requests and items.
 *
 * @param repository The repository instance to interact with Firestore.
 */
class RequestUseCase(
    private val repository: RequestRepository,
    private val userRepository: UserRepository,
    private val userUseCase: UserUseCase,
    private val storage: StorageRepository
) : CredentialsValidatorI<TravelRequestDto> {

    fun mergeRequestData(
        requestList: List<PaymentRequest>,
        itemList: List<RequestItem>
    ) {
        requestList.forEach { request ->
            val requestId = request.id ?: throw InvalidParameterException(EMPTY_ID)
            val items = itemList.filter { it.requestId == requestId }
            request.itemsList?.clear()
            request.itemsList?.addAll(items)
        }
    }

    suspend fun createRequest(dto: TravelRequestDto, uid: String): String {
        dto.requestNumber = userRepository.getUserRequestNumber(uid)
        val id = repository.save(dto)
        userRepository.updateRequestNumber(uid)
        return id
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun delete(permission: PermissionLevelType, request: PaymentRequest) {
        val id = request.id ?: throw NullPointerException(EMPTY_ID)
        val itemsId = request.itemsList?.mapNotNull { it.id }
        val dto = request.toDto()
        validatePermission(permission, dto)
        repository.delete(id, itemsId)

        GlobalScope.launch {
            request.itemsList?.forEach {
                if (it.docUrl != null) storage.deleteImage("requests/${it.id}.jpeg")
            }

            dto.encodedImage?.let {
                storage.deleteImage("requests/${dto.id}.jpeg")
            }
        }

    }

    suspend fun save(permission: PermissionLevelType, dto: TravelRequestDto) {
       dto.validateForDataBaseInsertion()
        validatePermission(permission, dto)
        repository.save(dto)
    }

    override fun validatePermission(permission: PermissionLevelType, dto: TravelRequestDto) {
        val status = dto.status?.let { PaymentRequestStatusType.getType(it) }
            ?: throw InvalidParameterException("Status is null")

        if (status != PaymentRequestStatusType.SENT &&
            permission != PermissionLevelType.MANAGER
        ) throw InvalidParameterException("Invalid credentials for $permission")

    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun deleteItem(
        permission: PermissionLevelType,
        dto: TravelRequestDto,
        item: RequestItem
    ) {
        val id = dto.id ?: throw NullPointerException(EMPTY_ID)
        validatePermission(permission, dto)
        repository.deleteItem(id, item.id!!)

        item.docUrl?.let {
            GlobalScope.launch {
                try {
                    storage.deleteImage("requests/${item.id}.jpeg")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    suspend fun saveItem(permission: PermissionLevelType, dto: RequestItemDto): String {
        val requestId = dto.requestId ?: throw NullPointerException(EMPTY_ID)
        dto.validateDataIntegrity()

        val response = repository.fetchRequestById(requestId).awaitValue()
        val requestDto = when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }.toDto()

        validatePermission(permission, requestDto)
        return repository.saveItem(dto)
    }

}



