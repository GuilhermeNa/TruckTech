package br.com.apps.trucktech.service

import androidx.lifecycle.asFlow
import br.com.apps.model.exceptions.null_objects.NullFreightException
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first

class FreightService(
    private val repository: FreightRepository
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun close() = scope.cancel()

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    suspend fun fetchFreightById(id: String): Freight {
        val response = repository.fetchFreightById(id).asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullFreightException(UNKNOWN_EXCEPTION)
        }
    }


}