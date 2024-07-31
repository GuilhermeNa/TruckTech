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

    override suspend fun fetchFreightListByDriverId(id: String, flow: Boolean) =
        read.fetchFreightListByDriverId(id, flow)

    override suspend fun fetchFreightListByTravelId(id: String, flow: Boolean) =
        read.fetchFreightListByTravelId(id, flow)

    override suspend fun fetchFreightListByTravelIds(ids: List<String>, flow: Boolean) =
        read.fetchFreightListByTravelIds(ids, flow)

    override suspend fun fetchFreightById(id: String, flow: Boolean) =
        read.fetchFreightById(id, flow)

    override suspend fun fetchFreightListByDriverIdAndIsNotPaidYet(
        id: String,
        flow: Boolean
    ) = read.fetchFreightListByDriverIdAndIsNotPaidYet(id, flow)

    override suspend fun fetchFreightListByDriverIdsAndPaymentStatus(
        ids: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ) = read.fetchFreightListByDriverIdsAndPaymentStatus(ids, isPaid, flow)

    override suspend fun fetchFreightListByDriverIdAndPaymentStatus(
        id: String,
        isPaid: Boolean,
        flow: Boolean
    ) = read.fetchFreightListByDriverIdAndPaymentStatus(id, isPaid, flow)

}