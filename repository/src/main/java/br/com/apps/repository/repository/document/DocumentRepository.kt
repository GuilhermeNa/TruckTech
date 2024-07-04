package br.com.apps.repository.repository.document

import br.com.apps.model.dto.DocumentDto


class DocumentRepository(
    private val write: DocumentWriteImpl,
    private val read: DocumentReadImpl
) : DocumentRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: DocumentDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getDocumentById(documentId: String, flow: Boolean) = read.getDocumentById(documentId, flow)

    override suspend fun getDocumentListByTruckId(truckId: String, flow: Boolean) =
        read.getDocumentListByTruckId(truckId, flow)

}






