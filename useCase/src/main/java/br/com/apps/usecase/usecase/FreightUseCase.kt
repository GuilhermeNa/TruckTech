package br.com.apps.usecase.usecase

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.util.buildFreightStoragePath

class FreightUseCase(
    private val fRepository: FreightRepository,
    private val storage: StorageRepository
) {

    /**
     * Saves a freight DTO to the repository.
     *
     * @param writeReq The WriteRequest containing the data transfer object (DTO) to save and authorization level.
     *
     * @throws InvalidAuthLevelException if the permission validation fails (Response.Error).
     * @throws NullPointerException if there is any null field.
     */
    suspend fun save(writeReq: WriteRequest<FreightDto>): String {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        dto.validateWriteAccess(auth)
        dto.validateDataForDbInsertion()

        return fRepository.save(dto)
    }

    /**
     * Deletes a freight DTO from the repository.
     *
     * @param writeReq The WriteRequest containing the data transfer object (DTO) to delete and authorization level.
     *
     * @throws InvalidAuthLevelException if the permission validation fails (Response.Error).
     */
    suspend fun delete(writeReq: WriteRequest<FreightDto>) {
        writeReq.data.let { dto ->
            dto.validateWriteAccess(writeReq.authLevel)
            dto.validateDataIntegrity()

            dto.urlInvoice?.run {
                val path = buildFreightStoragePath(dto.id!!)
                storage.deleteImage(path)
            }

            fRepository.delete(dto.id!!)
        }
    }

}

