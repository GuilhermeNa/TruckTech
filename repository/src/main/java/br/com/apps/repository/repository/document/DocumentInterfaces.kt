package br.com.apps.repository.repository.document

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.DocumentDto
import br.com.apps.model.model.Document
import br.com.apps.repository.util.Response

interface DocumentRepositoryI : DocumentWriteI, DocumentReadI

interface DocumentWriteI {

    suspend fun save(dto: DocumentDto)

}

interface DocumentReadI {

    suspend fun getDocumentById(documentId: String, flow: Boolean = false)
            : LiveData<Response<Document>>

    suspend fun getDocumentListByTruckId(truckId: String, flow: Boolean = false)
            : LiveData<Response<List<Document>>>

}