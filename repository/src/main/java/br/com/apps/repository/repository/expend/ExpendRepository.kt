package br.com.apps.repository.repository.expend

import br.com.apps.model.dto.travel.ExpendDto

class ExpendRepository(
    private val write: ExpendWriteImpl,
    private val read: ExpendReadImpl
) : ExpendRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun delete(expendId: String?) = write.delete(expendId)

    override suspend fun save(dto: ExpendDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchExpendListByDriverId(driverId: String, flow: Boolean) =
        read.fetchExpendListByDriverId(driverId, flow)

    override suspend fun fetchExpendListByDriverIdsAndRefundableStatus(
        driverIdList: List<String>, paidByEmployee: Boolean, alreadyRefunded: Boolean, flow: Boolean
    ) = read.fetchExpendListByDriverIdsAndRefundableStatus(
        driverIdList,
        paidByEmployee,
        alreadyRefunded,
        flow
    )

    override suspend fun fetchExpendListByDriverIdAndRefundableStatus(
        driverId: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean
    ) = read.fetchExpendListByDriverIdAndRefundableStatus(
        driverId,
        paidByEmployee,
        alreadyRefunded,
        flow
    )

    override suspend fun fetchExpendListByTravelId(travelId: String, flow: Boolean) =
        read.fetchExpendListByTravelId(travelId, flow)

    override suspend fun fetchExpendListByTravelIds(travelIdList: List<String>, flow: Boolean) =
        read.fetchExpendListByTravelIds(travelIdList, flow)

    override suspend fun fetchExpendById(expendId: String, flow: Boolean) =
        read.fetchExpendById(expendId, flow)

    override suspend fun fetchExpendListByDriverIdAndIsNotRefundYet(
        driverId: String,
        flow: Boolean
    ) = read.fetchExpendListByDriverIdAndIsNotRefundYet(driverId, flow)

}