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

    override suspend fun getTravelListByDriverIdAndIsFinished(driverId: String, flow: Boolean) =
        read.getTravelListByDriverIdAndIsFinished(driverId, flow)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getTravelListByDriverId(driverId: String, flow: Boolean) =
        read.getTravelListByDriverId(driverId, flow)

    override suspend fun getTravelById(travelId: String, flow: Boolean) =
        read.getTravelById(travelId, flow)

}

