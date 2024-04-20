package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FreightEditorFragmentViewModel(): ViewModel() {

    private var _viewPagerPosition = MutableLiveData(0)
    val viewPagerPosition get() = _viewPagerPosition

    //---------------------------------------------------------------------------------------------//
    // CLASS METHODS
    //---------------------------------------------------------------------------------------------//

    fun newPageHasBeenSelected(position: Int) {
        _viewPagerPosition.value = position
    }

}