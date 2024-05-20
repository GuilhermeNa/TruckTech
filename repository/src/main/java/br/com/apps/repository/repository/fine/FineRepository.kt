package br.com.apps.repository.repository.fine

import br.com.apps.model.dto.FineDto

class FineRepository(
    private val write: FineWrite,
    private val read: FineRead
): FineRepositoryI {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun delete(fineId: String) = write.delete(fineId)

    override suspend fun save(dto: FineDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getFineListByDriverId(driverId: String, flow: Boolean) =
        read.getFineListByDriverId(driverId, flow)

    override suspend fun getFineListByTruckId(truckId: String, flow: Boolean) =
        read.getFineListByTruckId(truckId, flow)

    override suspend fun getFineById(fineId: String, flow: Boolean) = read.getFineById(fineId, flow)

}
