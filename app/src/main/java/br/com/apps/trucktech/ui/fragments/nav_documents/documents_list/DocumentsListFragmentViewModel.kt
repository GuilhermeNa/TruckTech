package br.com.apps.trucktech.ui.fragments.nav_documents.documents_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.model.Document
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.LabelType
import br.com.apps.repository.repository.document.DocumentRepository
import br.com.apps.repository.repository.label.LabelRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
    private val _data = MutableLiveData<Response<List<Document>>>()
    val data get() = _data

    private val _state = MutableLiveData<State>()
    val state get() = _state

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        _state.value = State.Loading
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {

            val docListDef = loadDocumentData()
            val labelListDef = loadLabelData()

            val docList = docListDef.await()
            val labList = labelListDef.await()

            mergeData(docList, labList)
            sendResponse(docList)

        }
    }

    private suspend fun loadDocumentData(): CompletableDeferred<List<Document>> {
        val deferred = CompletableDeferred<List<Document>>()

        documentRepository.getDocumentListByTruckId(truckId)
            .asFlow().first { response ->
                when (response) {
                    is Response.Error -> {
                        _state.value = State.Error(response.exception)
                        _data.value = response
                    }
                    is Response.Success -> response.data?.let { deferred.complete(it) }
                }
                true
            }

        return deferred
    }

    private suspend fun loadLabelData(): CompletableDeferred<List<Label>> {
        val deferred = CompletableDeferred<List<Label>>()

        labelRepository.getLabelListByMasterUidAndType(LabelType.DOCUMENT.description, masterUid)
            .asFlow().first { response ->
                when (response) {
                    is Response.Error -> {
                        _state.value = State.Error(response.exception)
                        _data.value = response
                    }
                    is Response.Success -> response.data?.let { deferred.complete(it) }
                }
                true
            }

        return deferred
    }

    private fun mergeData(documents: List<Document>?, labelResponse: List<Label>) {
        documents?.map { document ->
            val id = document.labelId
            val label = labelResponse.firstOrNull { it.id == id }
            document.name = label?.name
        }
    }

    private fun sendResponse(docList: List<Document>) {
        if (docList.isEmpty()) {
            _state.value = State.Empty
        }
        else {
            _state.value = State.Loaded
        }

        _data.postValue(Response.Success(data = docList))
    }

}