package br.com.apps.repository.repository.travel

import br.com.apps.model.dto.travel.TravelDto

class TravelRepository(
    private val read: TravelRead,
    private val write: TravelWrite
): TravelRepositoryI {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: TravelDto) = write.save(dto)

    override suspend fun delete(travelId: String) = write.delete(travelId)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getTravelListByDriverId(id: String, flow: Boolean) =
        read.getTravelListByDriverId(id, flow)

    override suspend fun getTravelById(id: String, flow: Boolean) =
        read.getTravelById(id, flow)

}

