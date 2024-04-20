package br.com.apps.trucktech.ui.fragments.nav_travel.records

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordsFragmentViewModel(): ViewModel() {

    lateinit var travelId: String

    private var _viewPagerPosition = MutableLiveData(0)
    val viewPagerPosition get() = _viewPagerPosition

    //---------------------------------------------------------------------------------------------//
    // CLASS METHODS
    //---------------------------------------------------------------------------------------------//

    fun newPageHasBeenSelected(position: Int) {
        _viewPagerPosition.value = position
    }

}