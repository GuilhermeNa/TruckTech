package br.com.apps.usecase.usecase

import br.com.apps.model.dto.request.ItemDto
import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.item.ItemRepository
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.util.buildRequestStoragePath

class ItemUseCase(
    private val repository: ItemRepository,
    private val storage: StorageRepository
) {

    suspend fun save(writeReq: WriteRequest<ItemDto>): String {
       return writeReq.data.run {
           validateDataForDbInsertion()
           validateWriteAccess(writeReq.authLevel)
           repository.save(this)
       }
    }

    suspend fun delete(writeReq: WriteRequest<ItemDto>) {
        writeReq.data.let { dto ->
            dto.validateWriteAccess(writeReq.authLevel)
            dto.validateDataIntegrity()

            dto.urlImage?.run {
                val path = buildRequestStoragePath(dto.id!!)
                storage.deleteImage(path)
            }

            repository.delete(dto.id!!)

        }
    }

}