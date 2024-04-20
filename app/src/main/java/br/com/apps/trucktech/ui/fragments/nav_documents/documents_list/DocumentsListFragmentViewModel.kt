package br.com.apps.trucktech.ui.fragments.nav_documents.documents_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.Document
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.LabelType
import br.com.apps.repository.Response
import br.com.apps.usecase.DocumentUseCase
import br.com.apps.usecase.LabelUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class DocumentsListFragmentViewModel(
    private val masterUid: String,
    private val truckId: String,
    private val documentUseCase: DocumentUseCase,
    private val labelUseCase: LabelUseCase
) : ViewModel() {

    private val _documentData = MutableLiveData<Response<List<Document>>>()
    val documentData get() = _documentData

    fun loadData() {
        viewModelScope.launch {
            val documentList = mutableListOf<Document>()
            val labelList = mutableListOf<Label>()

            val liveDataA = documentUseCase.getByTruckId(truckId)
            val liveDataB = labelUseCase.getAllByType(LabelType.DOCUMENT, masterUid)

            val deferredA = async {
                liveDataA.asFlow().collect { responseA ->
                    when (responseA) {
                        is Response.Error -> throw IllegalArgumentException()

                        is Response.Success -> {
                            documentList.clear()
                            documentList.addAll(responseA.data!!)
                        }
                    }
                }
            }

            val deferredB = async {
                liveDataB.asFlow().collect { responseB ->
                    when (responseB) {
                        is Response.Error -> throw IllegalArgumentException()

                        is Response.Success -> {
                            labelList.clear()
                            labelList.addAll(responseB.data!!)

                        }
                    }
                }
            }

            awaitAll(deferredA, deferredB)
            mergeData(documentList, labelList)
            _documentData.postValue(Response.Success(data = documentList))
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