package br.com.apps.trucktech.ui.fragments.nav_home.fines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.model.Fine
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.fine.FineRepository
import kotlinx.coroutines.launch

class FinesListViewModel(
    private val idHolder: IdHolder,
    private val repository: FineRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a list of [Fine]
     * to be displayed on screen.
     */
    private val _fineData = MutableLiveData<Response<List<Fine>>>()
    val fineData get() = _fineData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        val id = idHolder.driverId ?: throw IllegalArgumentException(EMPTY_ID)
        viewModelScope.launch {
            repository.getFineListByDriverId(id, false).asFlow().collect {
                _fineData.value = it
            }
        }
    }

}