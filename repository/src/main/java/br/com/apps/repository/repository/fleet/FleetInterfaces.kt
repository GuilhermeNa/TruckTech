package br.com.apps.repository.repository.fleet

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.TruckDto
import br.com.apps.model.model.Truck
import br.com.apps.repository.util.Response

interface FleetRepositoryI : FleetReadI, FleetWriteI

interface FleetReadI {

    suspend fun getTruckListByMasterUid(masterUid: String, flow: Boolean = false)
            : LiveData<Response<List<Truck>>>

    suspend fun getTruckById(truckId: String, flow: Boolean = false)
            : LiveData<Response<Truck>>

    suspend fun getTruckByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<Truck>>

}

interface FleetWriteI {

    suspend fun delete(truckId: String)

    suspend fun save(dto: TruckDto)

}