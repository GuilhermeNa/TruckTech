package br.com.apps.repository.repository.outlay

import br.com.apps.model.dto.travel.OutlayDto

class OutlayRepository(
    private val write: OutlayWriteImpl,
    private val read: OutlayReadImpl
) : OutlayRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun delete(expendId: String?) = write.delete(expendId)

    override suspend fun save(dto: OutlayDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchOutlayListByDriverId(id: String, flow: Boolean) =
        read.fetchOutlayListByDriverId(id, flow)

    override suspend fun fetchOutlayListByDriverIdsAndRefundableStatus(
        ids: List<String>, paidByEmployee: Boolean, alreadyRefunded: Boolean, flow: Boolean
    ) = read.fetchOutlayListByDriverIdsAndRefundableStatus(
        ids,
        paidByEmployee,
        alreadyRefunded,
        flow
    )

    override suspend fun fetchOutlayListByDriverIdAndRefundableStatus(
        id: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean
    ) = read.fetchOutlayListByDriverIdAndRefundableStatus(
        id,
        paidByEmployee,
        alreadyRefunded,
        flow
    )

    override suspend fun fetchOutlayListByTravelId(id: String, flow: Boolean) =
        read.fetchOutlayListByTravelId(id, flow)

    override suspend fun fetchOutlayListByTravelIds(ids: List<String>, flow: Boolean) =
        read.fetchOutlayListByTravelIds(ids, flow)

    override suspend fun fetchOutlayById(id: String, flow: Boolean) =
        read.fetchOutlayById(id, flow)

    override suspend fun fetchOutlayListByDriverIdAndIsNotRefundYet(
        id: String,
        flow: Boolean
    ) = read.fetchOutlayListByDriverIdAndIsNotRefundYet(id, flow)

    override suspend fun fetchOutlayByIds(
        ids: List<String>,
        flow: Boolean
    ) = read.fetchOutlayByIds(ids, flow)

}