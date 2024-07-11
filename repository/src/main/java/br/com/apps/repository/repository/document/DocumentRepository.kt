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

    override suspend fun getDocumentById(documentId: String, flow: Boolean) = read.getDocumentById(documentId, flow)

    override suspend fun getDocumentListByTruckId(truckId: String, flow: Boolean) =
        read.getDocumentListByTruckId(truckId, flow)

    override suspend fun fetchDocumentListByTruckIdList(idList: List<String>, flow: Boolean) =
        read.fetchDocumentListByTruckIdList(idList, flow)

}






