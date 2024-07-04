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

    override suspend fun getFreightListByDriverId(driverId: String, flow: Boolean) =
        read.getFreightListByDriverId(driverId, flow)

    override suspend fun getFreightListByTravelId(travelId: String, flow: Boolean) =
        read.getFreightListByTravelId(travelId, flow)

    override suspend fun getFreightListByTravelIds(travelIdList: List<String>, flow: Boolean) =
        read.getFreightListByTravelIds(travelIdList, flow)

    override suspend fun getFreightById(freightId: String, flow: Boolean) =
        read.getFreightById(freightId, flow)

    override suspend fun getFreightListByDriverIdAndIsNotPaidYet(
        driverId: String,
        flow: Boolean
    ) =
        read.getFreightListByDriverIdAndIsNotPaidYet(driverId, flow)

    override suspend fun getFreightListByDriverIdsAndPaymentStatus(
        driverIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ) =
        read.getFreightListByDriverIdsAndPaymentStatus(driverIdList, isPaid, flow)

    override suspend fun getFreightListByDriverIdAndPaymentStatus(
        driverId: String,
        isPaid: Boolean,
        flow: Boolean
    ) =
        read.getFreightListByDriverIdAndPaymentStatus(driverId, isPaid, flow)

}