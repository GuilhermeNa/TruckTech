package br.com.apps.repository.repository.fine

import br.com.apps.model.dto.FleetFineDto

class FineRepository(
    private val write: FineWriteImpl,
    private val read: FineReadImpl
): FineRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun delete(fineId: String) = write.delete(fineId)

    override suspend fun save(dto: FleetFineDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchFineListByDriverId(id: String, flow: Boolean) =
        read.fetchFineListByDriverId(id, flow)

    override suspend fun fetchFineListByFleetId(id: String, flow: Boolean) =
        read.fetchFineListByFleetId(id, flow)

    override suspend fun fetchFineById(id: String, flow: Boolean) = read.fetchFineById(id, flow)

}
