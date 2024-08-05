package br.com.apps.usecase.usecase

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.repository.util.validateAndProcess

class FreightUseCase(private val fRepository: FreightRepository) {

    /**
     * Saves a freight DTO to the repository.
     *
     * @param writeReq The WriteRequest containing the data transfer object (DTO) to save and authorization level.
     *
     * @throws InvalidAuthLevelException if the permission validation fails (Response.Error).
     * @throws NullPointerException if there is any null field.
     */
    suspend fun save(writeReq: WriteRequest<FreightDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            validatePermission = { dto.validateWriteAccess(auth) },
            validateData = { dto.validateDataForDbInsertion() }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> fRepository.save(dto)
            }
        }
    }

    /**
     * Deletes a freight DTO from the repository.
     *
     * @param writeReq The WriteRequest containing the data transfer object (DTO) to delete and authorization level.
     *
     * @throws InvalidAuthLevelException if the permission validation fails (Response.Error).
     */
    suspend fun delete(writeReq: WriteRequest<FreightDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            validatePermission = { dto.validateWriteAccess(auth) }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> fRepository.delete(dto.id)
            }
        }
    }

}

