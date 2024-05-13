package br.com.apps.trucktech.ui.activities.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginActivityViewModel: ViewModel() {

    val INITIAL_STATE = 0
    val GET_CREDENTIAL_STATE = 1
    val LOGIN_STATE = 2

    private val _uiState = MutableLiveData(INITIAL_STATE)
    val uiState get() = _uiState


}