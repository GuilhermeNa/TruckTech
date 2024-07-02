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
import br.com.apps.trucktech.exceptions.NoLabelsFoundException
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DocumentsListFragmentViewModel(
    idHolder: IdHolder,
    private val documentRepository: DocumentRepository,
    private val labelRepository: LabelRepository
) : ViewModel() {

    val masterUid = idHolder.masterUid ?: throw IllegalArgumentException("masterUid is null")
    val truckId = idHolder.truckId ?: throw IllegalArgumentException("truckId is null")

    private lateinit var labels: List<Label>

    /**
     * LiveData holding the response data of type [Response] with a list of [Document]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<Document>>()
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
        _state.value = state
    }

    fun loadData() {
        viewModelScope.launch {
            delay(1000)

            try {
                labels = loadLabels()
                loadDocuments { sendResponse(it) }

            } catch (e: Exception) {
                e.printStackTrace()
                setState(State.Error(e))
            }

        }
    }

    private suspend fun loadDocuments(onComplete: (data: List<Document>) -> Unit) {
        val response = documentRepository.getDocumentListByTruckId(truckId).asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> onComplete(response.data ?: emptyList())
        }

    }

    private suspend fun loadLabels(): List<Label> {
        val response =
            labelRepository
                .getLabelListByMasterUidAndType(LabelType.DOCUMENT.description, masterUid)
                .asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NoLabelsFoundException()
        }
    }

    private fun sendResponse(docList: List<Document>) {
        if (docList.isEmpty()) {
            setState(State.Empty)
        } else {
            mergeData(docList)
            setState(State.Loaded)
            _data.value = docList
        }

    }

    private fun mergeData(documents: List<Document>) {
        documents.map { document ->
            val id = document.labelId
            val label = labels.firstOrNull { it.id == id }
            document.name = label?.name
        }
    }

}