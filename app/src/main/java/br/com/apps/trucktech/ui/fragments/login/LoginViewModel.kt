package br.com.apps.trucktech.ui.fragments.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val STATE_INITIAL = 0
const val STATE_CREDENTIAL = 1
const val STATE_LOGIN = 2

class LoginViewModel : ViewModel() {

    private val _uiState = MutableLiveData(STATE_INITIAL)
    val uiState get() = _uiState

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    fun setStateCredential() {
        viewModelScope.launch {
            delay(1000)
            _uiState.value = STATE_CREDENTIAL
        }

    }

    fun setStateLogin() {
        viewModelScope.launch {
            delay(1000)
            _uiState.value = STATE_LOGIN
        }
    }

}