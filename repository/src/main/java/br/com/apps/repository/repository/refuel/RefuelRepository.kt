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

    override suspend fun fetchRefuelListByDriverId(id: String, flow: Boolean) =
        read.fetchRefuelListByDriverId(id, flow)

    override suspend fun fetchRefuelListByTravelId(id: String, flow: Boolean) =
        read.fetchRefuelListByTravelId(id, flow)

    override suspend fun fetchRefuelListByTravelIds(ids: List<String>, flow: Boolean) =
        read.fetchRefuelListByTravelIds(ids, flow)

    override suspend fun fetchRefuelById(id: String, flow: Boolean) =
        read.fetchRefuelById(id, flow)

}