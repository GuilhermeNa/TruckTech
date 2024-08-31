package br.com.apps.repository.repository.request

import br.com.apps.model.dto.request.RequestDto

class RequestRepository(
    private val read: RequestReadImpl,
    private val write: RequestWriteImpl
) : RequestRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: RequestDto) = write.save(dto)

    override suspend fun delete(id: String) =
        write.delete(id)

    override suspend fun updateUrlImage(id: String, url: String?) =
        write.updateUrlImage(id, url)

    override suspend fun setUpdatingStatus(id: String, isUpdating: Boolean) = write.setUpdatingStatus(id, isUpdating)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchRequestListByUid(id: String, flow: Boolean) =
        read.fetchRequestListByUid(id, flow)

    override suspend fun fetchRequestById(id: String, flow: Boolean) =
        read.fetchRequestById(id, flow)

}