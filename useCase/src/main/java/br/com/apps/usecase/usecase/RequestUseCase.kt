package br.com.apps.usecase.usecase

import br.com.apps.model.dto.request.ItemDto
import br.com.apps.model.dto.request.RequestDto
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.exceptions.null_objects.NullIdException
import br.com.apps.model.expressions.getMonthFormatted
import br.com.apps.model.expressions.getYearReference
import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.item.ItemRepository
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.repository.user.UserRepository
import br.com.apps.repository.util.NULL_ID
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.util.buildRequestStoragePath
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * Use case class responsible for handling business logic related to payment requests and items.
 *
 * @param requestRepo The repository instance to interact with Firestore.
 */
class RequestUseCase(
    private val requestRepo: RequestRepository,
    private val itemsRepo: ItemRepository,
    private val storage: StorageRepository,
    private val userRepository: UserRepository,
) {

    suspend fun createRequest(dto: RequestDto, uid: String): String {
        val number = userRepository.getUserRequestNumber(uid) + 1
        val year = LocalDateTime.now().getYearReference()
        val month = LocalDateTime.now().getMonthFormatted()
        val newNumber = ("$year$month$number").toLong()

        dto.requestNumber = newNumber
        val id = requestRepo.save(dto)
        userRepository.updateRequestNumber(uid)
        return id
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun delete(writeReq: WriteRequest<RequestDto>, items: Array<ItemDto>? = null) {
        val dto = writeReq.data
        val auth = writeReq.authLevel
        val id = dto.id ?: throw NullIdException(NULL_ID)

        //Validate
        dto.validateDataIntegrity()
        dto.validateWriteAccess(auth)

        items?.forEach { itemDto ->
            itemDto.validateDataIntegrity()
            itemDto.validateWriteAccess(auth)
        }

        //Delete objects
        requestRepo.delete(id)

        items?.map { it.id ?: throw NullIdException(NULL_ID) }
            ?.toTypedArray()
            ?.run {
                if (isNotEmpty()) itemsRepo.delete(this)
            }

        //Delete images
        GlobalScope.launch {
            writeReq.data.let { dto ->
                dto.urlImage?.run {
                    storage.deleteImage(buildRequestStoragePath(dto.id!!))
                }
            }
            items?.mapNotNull { it.id }?.forEach { id ->
                storage.deleteImage(buildRequestStoragePath(id))
            }
        }

    }

    suspend fun save(access: AccessLevel, dto: RequestDto) {
        dto.validateDataForDbInsertion()
        dto.validateWriteAccess(access)
        requestRepo.save(dto)
    }

    fun validateForDelete(writeReq: WriteRequest<RequestDto>) {
        writeReq.run {
            data.validateDataIntegrity()
            data.validateWriteAccess(authLevel)
        }
    }

}