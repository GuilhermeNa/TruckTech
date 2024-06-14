package br.com.apps.repository.repository.expend

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.util.Response

interface ExpendRepositoryI : ExpendReadI, ExpendWriteI

interface ExpendReadI {

    suspend fun getExpendListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Expend>>>

    suspend fun getExpendListByDriverIdsAndRefundableStatus(
        driverIdList: List<String>,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Expend>>>

    suspend fun getExpendListByDriverIdAndRefundableStatus(
        driverId: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Expend>>>

    suspend fun getExpendListByTravelId(travelId: String, flow: Boolean = false)
            : LiveData<Response<List<Expend>>>

    suspend fun getExpendListByTravelIds(driverIdList: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Expend>>>

    suspend fun getExpendById(expendId: String, flow: Boolean = false)
            : LiveData<Response<Expend>>

    suspend fun getExpendListByDriverIdAndIsNotRefundYet(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Expend>>>

}

interface ExpendWriteI {

    suspend fun delete(expendId: String)

    suspend fun save(dto: ExpendDto)

}