package br.com.apps.trucktech.ui.fragments.nav_documents.documents_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.enums.LabelCategory
import br.com.apps.model.model.TruckDocument
import br.com.apps.model.model.TruckDocument.Companion.merge
import br.com.apps.model.model.label.Label
import br.com.apps.repository.repository.document.DocumentRepository
import br.com.apps.repository.repository.label.LabelRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.exceptions.NoLabelsFoundException
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DocumentsListFragmentViewModel(
    private val vmData: DocumentListVmData,
    private val documentRepository: DocumentRepository,
    private val labelRepository: LabelRepository
) : ViewModel() {

    private lateinit var labels: List<Label>

    /**
     * LiveData holding the response data of type [Response] with a list of [TruckDocument]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<TruckDocument>>()
    val data get() = _data

    private val _state = MutableLiveData<State>()
    val state get() = _state

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(State.Loading)
        loadData()
    }

    private fun setState(state: State) {
        if (state != _state.value) _state.value = state
    }

    fun loadData() {
        viewModelScope.launch {
            delay(1000)

            try {
                labels = loadLabels()
                loadDocuments {
                    sendResponse(it)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                setState(State.Error(e))
            }

        }
    }

    private suspend fun loadLabels(): List<Label> {
        val response =
            labelRepository
                .fetchLabelListByMasterUidAndType(LabelCategory.DOCUMENT.toString(), vmData.masterUid)
                .asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NoLabelsFoundException()
        }
    }

    private suspend fun loadDocuments(onComplete: (data: List<TruckDocument>) -> Unit) {
        val response =
            documentRepository.fetchDocumentListByFleetIdList(vmData.fleetIds).asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data?.let { onComplete(it) }
                ?: throw NullPointerException("Null document")
        }
    }

    private fun sendResponse(docList: List<TruckDocument>) {
        if (docList.isEmpty()) {
            setState(State.Empty)
        } else {
            docList.merge(labels)
            setState(State.Loaded)
            _data.value = docList
        }

    }

}

data class DocumentListVmData(
    val masterUid: String,
    val fleetIds: List<String>
)