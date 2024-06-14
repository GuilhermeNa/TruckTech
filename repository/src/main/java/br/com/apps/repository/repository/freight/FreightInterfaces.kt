package br.com.apps.repository.repository.freight

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.Response

interface FreightRepositoryI : FreightWriteI, FreightReadI

interface FreightWriteI {

    suspend fun save(dto: FreightDto)

    suspend fun delete(freightId: String)

}

interface FreightReadI {

    suspend fun getFreightListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Freight>>>

    suspend fun getFreightListByDriverIdsAndPaymentStatus(
        driverIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Freight>>>

    suspend fun getFreightListByDriverIdAndPaymentStatus(
        driverId: String,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Freight>>>

    suspend fun getFreightListByTravelId(travelId: String, flow: Boolean = false)
            : LiveData<Response<List<Freight>>>

    suspend fun getFreightListByTravelIds(travelIdList: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Freight>>>

    suspend fun getFreightById(freightId: String, flow: Boolean = false)
            : LiveData<Response<Freight>>

    suspend fun getFreightListByDriverIdAndIsNotPaidYet(driverId: String, flow: Boolean = false)
    : LiveData<Response<List<Freight>>>

}
