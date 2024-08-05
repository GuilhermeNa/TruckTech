package br.com.apps.repository.repository.travel

import br.com.apps.model.dto.travel.TravelDto

class TravelRepository(
    private val read: TravelReadImpl,
    private val write: TravelWriteImpl
) : TravelRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: TravelDto) = write.save(dto)

    override suspend fun delete(travelId: String) = write.delete(travelId)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchTravelListByDriverIdAndIsFinished(id: String, flow: Boolean) =
        read.fetchTravelListByDriverIdAndIsFinished(id, flow)

    override suspend fun fetchTravelListByDriverId(id: String, flow: Boolean) =
        read.fetchTravelListByDriverId(id, flow)

    override suspend fun fetchTravelById(id: String, flow: Boolean) =
        read.fetchTravelById(id, flow)

}

