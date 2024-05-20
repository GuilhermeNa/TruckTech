package br.com.apps.repository.repository.document

import br.com.apps.model.dto.DocumentDto
import br.com.apps.repository.repository.document.DocumentRead
import br.com.apps.repository.repository.document.DocumentRepositoryI
import br.com.apps.repository.repository.document.DocumentWrite


class DocumentRepository(
    private val write: DocumentWrite,
    private val read: DocumentRead
) : DocumentRepositoryI {

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






