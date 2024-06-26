package br.com.apps.repository.repository.refuel

import br.com.apps.model.dto.travel.RefuelDto

class RefuelRepository(
    private val write: RefuelWrite,
    private val read: RefuelRead
): RefuelRepositoryI {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: RefuelDto) = write.save(dto)

    override suspend fun delete(refuelId: String) = write.delete(refuelId)

    override suspend fun deleteRefuelForThisTravel(travelId: String, refuelId: String) =
        write.deleteRefuelForThisTravel(travelId, refuelId)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getRefuelListByDriverId(driverId: String, flow: Boolean) =
        read.getRefuelListByDriverId(driverId, flow)

    override suspend fun getRefuelListByTravelId(travelId: String, flow: Boolean) =
        read.getRefuelListByTravelId(travelId, flow)

    override suspend fun getRefuelListByTravelIds(idList: List<String>, flow: Boolean) =
        read.getRefuelListByTravelIds(idList, flow)

    override suspend fun getRefuelById(refuelId: String, flow: Boolean) =
        read.getRefuelById(refuelId, flow)

}