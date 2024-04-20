package br.com.apps.trucktech.ui.fragments.nav_settings.themes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.apps.trucktech.ui.DARK_SWITCH
import br.com.apps.trucktech.ui.LIGHT_SWITCH

class ThemeFragmentViewModel : ViewModel() {

    private var _selectedSwitch = MutableLiveData<HashMap<Int, Boolean>>()
    val selectedSwitch get() = _selectedSwitch

    fun newSwitchHaveBeenSelected(selectedSwitch: Int) {
        when (selectedSwitch) {
            LIGHT_SWITCH ->
                _selectedSwitch.value =
                    hashMapOf(
                        Pair(LIGHT_SWITCH, true),
                        Pair(DARK_SWITCH, false)
                    )

            DARK_SWITCH -> _selectedSwitch.value =
                hashMapOf(
                    Pair(LIGHT_SWITCH, false),
                    Pair(DARK_SWITCH, true)
                )
        }
    }

}