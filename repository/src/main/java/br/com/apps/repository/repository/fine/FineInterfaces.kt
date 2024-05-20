package br.com.apps.repository.repository.fine

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.FineDto
import br.com.apps.model.model.Fine
import br.com.apps.repository.util.Response

interface FineRepositoryI : FineWriteI, FineReadI

interface FineWriteI {

    suspend fun delete(fineId: String)

    suspend fun save(dto: FineDto)

}

interface FineReadI {

    suspend fun getFineListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Fine>>>

    suspend fun getFineListByTruckId(truckId: String, flow: Boolean = false)
            : LiveData<Response<List<Fine>>>

    suspend fun getFineById(fineId: String, flow: Boolean = false)
            : LiveData<Response<Fine>>

}