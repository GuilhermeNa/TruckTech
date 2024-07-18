package br.com.apps.usecase.usecase

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.repository.customer.CustomerRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.repository.util.validateAndProcess
import br.com.apps.usecase.util.awaitData
import br.com.apps.usecase.util.observeFlow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FreightUseCase(
    private val fRepository: FreightRepository,
    private val customerRepository: CustomerRepository
) {

    /**
     * Retrieves a freight by its ID asynchronously using Flow, including associated customer information.
     *
     * @param id The ID of the freight to retrieve.
     * @param onComplete Callback function to handle the response when retrieval is complete.
     */
    suspend fun getFreightByIdFlow(id: String, onComplete: (Response<Freight>) -> Unit) {
        coroutineScope {
            try {
                fRepository.fetchFreightById(id, true).observeFlow { f ->
                    launch {
                        f.customer = customerRepository.getCustomerById(f.customerId).awaitData()
                        onComplete(Response.Success(f))
                    }
                }

            } catch (e: Exception) {
                onComplete(Response.Error(e))
            }

        }
    }

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
            permission = { dto.validatePermission(auth) },
            validator = { dto.validateForDataBaseInsertion() }
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
            permission = { dto.validatePermission(auth) }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> fRepository.delete(dto.id)
            }
        }
    }

}

