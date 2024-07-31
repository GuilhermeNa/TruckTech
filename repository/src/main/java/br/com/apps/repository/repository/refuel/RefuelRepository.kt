package br.com.apps.repository.repository.refuel

import br.com.apps.model.dto.travel.RefuelDto

class RefuelRepository(
    private val write: RefuelWriteImpl,
    private val read: RefuelReadImpl
): RefuelRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: RefuelDto) = write.save(dto)

    override suspend fun delete(refuelId: String?) = write.delete(refuelId)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchRefuelListByDriverId(driverId: String, flow: Boolean) =
        read.fetchRefuelListByDriverId(driverId, flow)

    override suspend fun fetchRefuelListByTravelId(travelId: String, flow: Boolean) =
        read.fetchRefuelListByTravelId(travelId, flow)

    override suspend fun fetchRefuelListByTravelIds(idList: List<String>, flow: Boolean) =
        read.fetchRefuelListByTravelIds(idList, flow)

    override suspend fun fetchRefuelById(refuelId: String, flow: Boolean) =
        read.fetchRefuelById(refuelId, flow)

}