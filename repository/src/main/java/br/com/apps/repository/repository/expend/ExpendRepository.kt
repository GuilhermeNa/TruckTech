package br.com.apps.repository.repository.expend

import br.com.apps.model.dto.travel.ExpendDto

class ExpendRepository(
    private val write: ExpendWrite,
    private val read: ExpendRead
) : ExpendRepositoryI {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun delete(expendId: String) = write.delete(expendId)

    override suspend fun save(dto: ExpendDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getExpendListByDriverId(driverId: String, flow: Boolean) =
        read.getExpendListByDriverId(driverId, flow)

    override suspend fun getExpendListByDriverIdsAndRefundableStatus(
        driverIdList: List<String>, paidByEmployee: Boolean, alreadyRefunded: Boolean, flow: Boolean
    ) = read.getExpendListByDriverIdsAndRefundableStatus(
        driverIdList,
        paidByEmployee,
        alreadyRefunded,
        flow
    )

    override suspend fun getExpendListByDriverIdAndRefundableStatus(
        driverId: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean
    ) = read.getExpendListByDriverIdAndRefundableStatus(
        driverId,
        paidByEmployee,
        alreadyRefunded,
        flow
    )

    override suspend fun getExpendListByTravelId(travelId: String, flow: Boolean) =
        read.getExpendListByTravelId(travelId, flow)

    override suspend fun getExpendListByTravelIds(driverIdList: List<String>, flow: Boolean) =
        read.getExpendListByTravelIds(driverIdList, flow)

    override suspend fun getExpendById(expendId: String, flow: Boolean) =
        read.getExpendById(expendId, flow)

    override suspend fun getExpendListByDriverIdAndIsNotRefundYet(
        driverId: String,
        flow: Boolean
    ) = read.getExpendListByDriverIdAndIsNotRefundYet(driverId, flow)

}