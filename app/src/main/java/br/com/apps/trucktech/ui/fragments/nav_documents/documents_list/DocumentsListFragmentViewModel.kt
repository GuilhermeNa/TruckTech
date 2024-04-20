package br.com.apps.trucktech.ui.fragments.nav_documents.documents_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.Document
import br.com.apps.model.model.Label
import br.com.apps.model.model.LabelType
import br.com.apps.repository.Response
import br.com.apps.usecase.DocumentUseCase
import br.com.apps.usecase.LabelUseCase
import kotlinx.coroutines.launch

class DocumentsListFragmentViewModel(
    private val masterUid: String,
    private val truckId: String,
    private val documentUseCase: DocumentUseCase,
    private val labelUseCase: LabelUseCase
) : ViewModel() {

    private val _loadedData = MutableLiveData<Response<List<Document>>>()
    val loadedData get() = _loadedData

    fun loadData() {
        viewModelScope.launch {
            documentUseCase.getByTruckId(truckId).asFlow().collect { documentResponse ->
                when (documentResponse) {
                    is Response.Success -> {
                        labelUseCase.getAllByType(LabelType.DOCUMENT, masterUid).asFlow()
                            .collect { labelResponse ->
                                mergeData(documentResponse.data, labelResponse)
                                _loadedData.postValue(Response.Success(data = documentResponse.data))
                            }
                    }

                    is Response.Error -> throw IllegalArgumentException()
                }
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