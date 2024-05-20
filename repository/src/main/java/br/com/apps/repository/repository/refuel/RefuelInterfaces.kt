package br.com.apps.repository.repository.refuel

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.model.travel.Refuel
import br.com.apps.repository.util.Response

interface RefuelRepositoryI : RefuelWriteI, RefuelReadI

interface RefuelWriteI {

    suspend fun save(dto: RefuelDto)

    suspend fun delete(refuelId: String)

    suspend fun deleteRefuelForThisTravel(travelId: String, refuelId: String)

}

interface RefuelReadI {

    suspend fun getRefuelListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Refuel>>>

    suspend fun getRefuelListByTravelId(travelId: String, flow: Boolean = false)
            : LiveData<Response<List<Refuel>>>

    suspend fun getRefuelListByTravelIds(idList: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Refuel>>>

    suspend fun getRefuelById(refuelId: String, flow: Boolean = false)
            : LiveData<Response<Refuel>>

}
