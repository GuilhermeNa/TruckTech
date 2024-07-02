package br.com.apps.usecase

import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.PaymentRequestStatusType
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.UserRepository
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.Response
import br.com.apps.usecase.util.awaitValue
import java.security.InvalidParameterException

/**
 * Use case class responsible for handling business logic related to payment requests and items.
 *
 * @param repository The repository instance to interact with Firestore.
 */
class RequestUseCase(
    private val repository: RequestRepository,
    private val userRepository: UserRepository,
    private val userUseCase: UserUseCase
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

    suspend fun delete(permission: PermissionLevelType, dto: TravelRequestDto) {
        val id = dto.id ?: throw NullPointerException(EMPTY_ID)
        val itemsId = dto.itemsList?.mapNotNull { it.id }
        validatePermission(permission, dto)
        repository.delete(id, itemsId)
    }

    suspend fun save(permission: PermissionLevelType, dto: TravelRequestDto) {
        if (!dto.validateFields()) throw InvalidParameterException("Invalid Request for saving")
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

    suspend fun deleteItem(
        permission: PermissionLevelType,
        dto: TravelRequestDto,
        itemId: String
    ) {
        val id = dto.id ?: throw NullPointerException(EMPTY_ID)
        validatePermission(permission, dto)
        repository.deleteItem(id, itemId)
    }

    suspend fun updateEncodedImage(
        permission: PermissionLevelType,
        dto: TravelRequestDto,
        encodedImage: String
    ) {
        val id = dto.id ?: throw NullPointerException(EMPTY_ID)
        validatePermission(permission, dto)
        repository.updateEncodedImage(id, encodedImage)
    }

    suspend fun saveItem(permission: PermissionLevelType, dto: RequestItemDto) {
        val requestId = dto.requestId ?: throw NullPointerException(EMPTY_ID)
        if (!dto.validateFields()) throw InvalidParameterException("Invalid Item for saving")

        val response = repository.getRequestById(requestId).awaitValue()
        val requestDto = when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }.toDto()

        validatePermission(permission, requestDto)
        repository.saveItem(dto)
    }


}



