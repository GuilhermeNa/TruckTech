package br.com.apps.trucktech.ui.fragments.nav_travel.cost.cost_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.ExpendRepository
import kotlinx.coroutines.launch

class ExpendPreviewViewModel(
    private val expendId: String,
    private val repository: ExpendRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Expend]
     * to be displayed on screen.
     */
    private val _expendData = MutableLiveData<Response<Expend>>()
    val expendData get() = _expendData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getExpendById(expendId, true).asFlow().collect {
                _expendData.value = it
            }
        }
    }

    fun delete() = liveData<Response<Unit>>(viewModelScope.coroutineContext) {
        try {
            repository.delete(expendId)
            emit(Response.Success())
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }

}