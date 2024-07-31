package br.com.apps.usecase.usecase

import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.repository.util.validateAndProcess

class RefuelUseCase(private val repository: RefuelRepository) {

    /**
     * Deletes a refuel DTO from the repository.
     *
     * @param writeReq The WriteRequest containing the refuel DTO to delete and the authorization level.
     * @throws Exception If permission validation fails (`Response.Error`).
     */
    suspend fun delete(writeReq: WriteRequest<RefuelDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            validatePermission = { dto.validatePermission(auth) }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> repository.delete(dto.id)
            }
        }

    }

    /**
     * Saves a refuel DTO to the repository.
     *
     * @param writeReq The WriteRequest containing the refuel DTO to save and the authorization level.
     * @throws Exception If permission or data validation fails (`Response.Error`).
     */
    suspend fun save(writeReq: WriteRequest<RefuelDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            validatePermission = { dto.validatePermission(auth) },
            validateData = { dto.validateForDataBaseInsertion() }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> repository.save(dto)
            }
        }
    }

}