package br.com.apps.repository.repository.travel

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.Response


interface TravelRepositoryI : TravelReadI, TravelWriteI

interface TravelWriteI {

    suspend fun delete(travelId: String)

    suspend fun save(dto: TravelDto)

}

interface TravelReadI {

    suspend fun getTravelListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Travel>>>

    suspend fun getTravelById(travelId: String, flow: Boolean = false)
            : LiveData<Response<Travel>>

}

