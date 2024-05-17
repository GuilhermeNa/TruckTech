package br.com.apps.trucktech.ui.fragments.nav_travel.cost.costs_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.Response
import br.com.apps.usecase.ExpendUseCase
import kotlinx.coroutines.launch

class ExpendListViewModel(
    private val idHolder: IdHolder,
    private val useCase: ExpendUseCase
): ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a list of expenditures [Expend]
     * to be displayed on screen.
     */
    private val _expendData = MutableLiveData<Response<List<Expend>>>()
    val expendData get() = _expendData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        val travelId = idHolder.travelId ?: throw IllegalArgumentException(EMPTY_ID)
        val masterUid = idHolder.masterUid ?: throw IllegalArgumentException(EMPTY_ID)

        viewModelScope.launch {
            useCase.getExpendListWithLabelByTravelId(masterUid, travelId).asFlow().collect {
                _expendData.value = it
            }
        }

    }

}