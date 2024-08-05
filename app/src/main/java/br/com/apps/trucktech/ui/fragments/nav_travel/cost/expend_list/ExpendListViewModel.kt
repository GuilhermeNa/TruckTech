package br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.exceptions.null_objects.NullExpendException
import br.com.apps.model.exceptions.null_objects.NullLabelException
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.travel.Outlay.Companion.merge
import br.com.apps.repository.repository.outlay.OutlayRepository
import br.com.apps.repository.repository.label.LabelRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ExpendListViewModel(
    private val vmData: ExpendLVmData,
    private val labelRepo: LabelRepository,
    private val expendRepo: OutlayRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a list of expenditures [Outlay]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<Outlay>>()
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

    private fun loadData() {
        viewModelScope.launch {
            try {
                val labels = loadLabels()
                loadExpends { expends ->
                    sendResponse(labels, expends)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                setState(State.Error(e))

            }
        }
    }

    private suspend fun loadLabels(): List<Label> {
        val response = labelRepo.fetchLabelListByMasterUid(vmData.masterUid)
            .asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullLabelException(UNKNOWN_EXCEPTION)
        }
    }

    private suspend fun loadExpends(complete: (List<Outlay>) -> Unit) {
        expendRepo.fetchOutlayListByTravelId(vmData.travelId).asFlow().collect { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data?.let { complete(it) }
                    ?: throw NullExpendException(UNKNOWN_EXCEPTION)
            }
        }
    }

    private fun sendResponse(labels: List<Label>, outlays: List<Outlay>) {
        if (outlays.isEmpty()) setState(State.Empty)
        else {
            outlays.merge(labels)
            setFragmentData(outlays)
            setState(State.Loaded)
        }
    }

    private fun setFragmentData(data: List<Outlay>) {
        _data.value = data
    }

    private fun setState(state: State) {
        if (state != _state.value) _state.value = state
    }

}

class ExpendLVmData(
    val masterUid: String,
    val travelId: String
)