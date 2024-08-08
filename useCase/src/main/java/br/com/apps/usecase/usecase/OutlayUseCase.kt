package br.com.apps.usecase.usecase

import br.com.apps.model.dto.travel.OutlayDto
import br.com.apps.repository.repository.outlay.OutlayRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.repository.util.validateAndProcess

class OutlayUseCase(private val repository: OutlayRepository) {

    /**
     * Deletes an OutlayDto entity from the repository after validating the permission.
     *
     * @param writeReq A WriteRequest object containing the data (OutlayDto)
     * to delete and the authorization level (authLevel).
     *
     * @throws InvalidAuthLevelException if the permission validation fails (Response.Error).
     */
    suspend fun delete(writeReq: WriteRequest<OutlayDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            validatePermission = { dto.validateWriteAccess(auth) }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> repository.delete(dto.id)
            }
        }

    }

    /**
     * Saves an OutlayDto entity to the repository after validating permission and data.
     *
     * @param writeReq: A WriteRequest object containing the data (OutlayDto) to save and
     * the authorization level (authLevel).
     *
     * @throws InvalidAuthLevelException if the permission validation fails (Response.Error).
     * @throws NullPointerException if there is any null field.
     */
    suspend fun save(writeReq: WriteRequest<OutlayDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            validatePermission = { dto.validateWriteAccess(auth) },
            validateData = { dto.validateDataForDbInsertion() }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> repository.save(dto)
            }
        }

    }

}