package br.com.apps.repository.repository.fleet

import br.com.apps.model.dto.fleet.TruckDto

class FleetRepository(
    private val write: FleetWriteImpl,
    private val read: FleetReadImpl
): FleetRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: TruckDto) = write.save(dto)

    override suspend fun delete(truckId: String) = write.delete(truckId)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchTruckListByMasterUid(uid: String, flow: Boolean) =
        read.fetchTruckListByMasterUid(uid, flow)

    override suspend fun fetchTruckById(id: String, flow: Boolean) =
        read.fetchTruckById(id, flow)

    override suspend fun fetchTruckByDriverId(id: String, flow: Boolean) =
        read.fetchTruckByDriverId(id, flow)

    override suspend fun fetchTrailerListLinkedToTruckById(id: String, flow: Boolean) =
        read.fetchTrailerListLinkedToTruckById(id, flow)

}
