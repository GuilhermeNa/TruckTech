package br.com.apps.trucktech.ui.fragments.nav_documents.documents_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.model.Document
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.LabelType
import br.com.apps.repository.Response
import br.com.apps.repository.repository.DocumentRepository
import br.com.apps.repository.repository.LabelRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DocumentsListFragmentViewModel(
    idHolder: IdHolder,
    private val documentRepository: DocumentRepository,
    private val labelRepository: LabelRepository
) : ViewModel() {

    val masterUid = idHolder.masterUid ?: throw IllegalArgumentException("masterUid is null")
    val truckId = idHolder.truckId ?: throw IllegalArgumentException("truckId is null")

    /**
     * LiveData holding the response data of type [Response] with a list of [Document]
     * to be displayed on screen.
     */
    private val _documentData = MutableLiveData<Response<List<Document>>>()
    val documentData get() = _documentData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val deferredA = CompletableDeferred<List<Document>>()
            val deferredB = CompletableDeferred<List<Label>>()

            launch { loadDocumentData { data -> deferredA.complete(data) } }
            launch { loadLabelData { data -> deferredB.complete(data) } }

            awaitAll(deferredA, deferredB)
            val documentList = deferredA.getCompleted()
            val labelList = deferredB.getCompleted()

            mergeData(documentList, labelList)
            _documentData.postValue(Response.Success(data = documentList))
        }
    }

    private suspend fun loadDocumentData(complete: (data: List<Document>) -> Unit) {
        documentRepository.getDocumentListByTruckId(truckId).asFlow().collect { response ->
            when (response) {
                is Response.Error -> throw IllegalArgumentException()
                is Response.Success -> response.data?.let { complete(it) }
            }
        }
    }

    private suspend fun loadLabelData(complete: (data: List<Label>) -> Unit) {
        labelRepository.getLabelListByTypeAndUserId(LabelType.DOCUMENT.description, masterUid)
            .asFlow().collect { response ->
                when (response) {
                    is Response.Error -> throw IllegalArgumentException()
                    is Response.Success -> response.data?.let { complete(it) }
                }
            }
    }

    private fun mergeData(documents: List<Document>?, labelResponse: List<Label>) {
        documents?.map { document ->
            val id = document.labelId
            val label = labelResponse.firstOrNull { it.id == id }
            document.name = label?.name
        }
    }

}