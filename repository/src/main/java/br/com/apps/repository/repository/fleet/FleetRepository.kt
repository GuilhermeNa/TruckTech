package br.com.apps.repository.repository.fleet

import br.com.apps.model.dto.TruckDto

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

    override suspend fun getTruckListByMasterUid(masterUid: String, flow: Boolean) =
        read.getTruckListByMasterUid(masterUid, flow)

    override suspend fun getTruckById(truckId: String, flow: Boolean) =
        read.getTruckById(truckId, flow)

    override suspend fun getTruckByDriverId(driverId: String, flow: Boolean) =
        read.getTruckByDriverId(driverId, flow)

}
