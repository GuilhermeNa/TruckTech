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

    override suspend fun fetchTravelListByDriverIdAndIsFinished(driverId: String, flow: Boolean) =
        read.fetchTravelListByDriverIdAndIsFinished(driverId, flow)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchTravelListByDriverId(driverId: String, flow: Boolean) =
        read.fetchTravelListByDriverId(driverId, flow)

    override suspend fun fetchTravelById(travelId: String, flow: Boolean) =
        read.fetchTravelById(travelId, flow)

}

