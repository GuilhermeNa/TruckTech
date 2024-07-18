package br.com.apps.repository.repository.freight

import br.com.apps.model.dto.travel.FreightDto

class FreightRepository(
    private val read: FreightReadImpl,
    private val write: FreightWriteImpl
): FreightRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: FreightDto) = write.save(dto)

    override suspend fun delete(freightId: String?) = write.delete(freightId)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchFreightListByDriverId(driverId: String, flow: Boolean) =
        read.fetchFreightListByDriverId(driverId, flow)

    override suspend fun fetchFreightListByTravelId(travelId: String, flow: Boolean) =
        read.fetchFreightListByTravelId(travelId, flow)

    override suspend fun fetchFreightListByTravelIds(travelIdList: List<String>, flow: Boolean) =
        read.fetchFreightListByTravelIds(travelIdList, flow)

    override suspend fun fetchFreightById(freightId: String, flow: Boolean) =
        read.fetchFreightById(freightId, flow)

    override suspend fun fetchFreightListByDriverIdAndIsNotPaidYet(
        driverId: String,
        flow: Boolean
    ) = read.fetchFreightListByDriverIdAndIsNotPaidYet(driverId, flow)

    override suspend fun fetchFreightListByDriverIdsAndPaymentStatus(
        driverIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ) = read.fetchFreightListByDriverIdsAndPaymentStatus(driverIdList, isPaid, flow)

    override suspend fun fetchFreightListByDriverIdAndPaymentStatus(
        driverId: String,
        isPaid: Boolean,
        flow: Boolean
    ) = read.fetchFreightListByDriverIdAndPaymentStatus(driverId, isPaid, flow)

}