package br.com.apps.usecase.usecase

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.repository.customer.CustomerRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.repository.util.validateAndProcess
import br.com.apps.usecase.util.awaitData
import br.com.apps.usecase.util.observeFlow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.security.InvalidParameterException

class FreightUseCase(
    private val fRepository: FreightRepository,
    private val customerRepository: CustomerRepository
) {

    /**
     * Merges the provided list of freights into the corresponding travels in the travel list.
     * Each freight is associated with a travel based on the travel ID.
     *
     * @param travelList The list of travels into which freights will be merged.
     * @param freightListNullable The nullable list of freights to merge into the travels.
     * @throws InvalidParameterException if the provided freight list is null.
     * @throws InvalidParameterException if any travel in the travel list has a null ID.
     */
    fun mergeFreightList(travelList: List<Travel>, freightListNullable: List<Freight>?) {
        val freightList = freightListNullable ?: throw InvalidParameterException(EMPTY_DATASET)
        travelList.forEach { travel ->
            val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
            val freights = freightList.filter { it.travelId == travelId }
            travel.freightsList = freights
        }
    }

    suspend fun getFreightByIdFlow(id: String, onComplete: (Response<Freight>) -> Unit) {
        coroutineScope {
            try {
                fRepository.getFreightById(id, true).observeFlow { f ->
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

    suspend fun save(writeReq: WriteRequest<FreightDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            permission = { dto.validatePermission(auth) },
            validator = { dto.validateDataForSaving() }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> fRepository.save(dto)
            }
        }
    }

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

