package br.com.apps.repository.repository.document

import br.com.apps.model.dto.TruckDocumentDto


class DocumentRepository(
    private val write: DocumentWriteImpl,
    private val read: DocumentReadImpl
) : DocumentRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: TruckDocumentDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchDocumentById(id: String, flow: Boolean) = read.fetchDocumentById(id, flow)

    override suspend fun fetchDocumentListByFleetId(id: String, flow: Boolean) =
        read.fetchDocumentListByFleetId(id, flow)

    override suspend fun fetchDocumentListByFleetIdList(ids: List<String>, flow: Boolean) =
        read.fetchDocumentListByFleetIdList(ids, flow)

}






