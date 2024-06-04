package br.com.apps.trucktech.ui.fragments.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class LoginViewModel : ViewModel() {

    private val _state = MutableLiveData<LfState>()
    val state get() = _state

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    fun setState(state: LfState, timer: Long = 0L) {
        viewModelScope.launch {
            delay(timer)
            _state.value = state
        }
    }

    fun getErrorMessage(exception: Exception) =
        when (exception) {
            is FirebaseAuthInvalidUserException,
            is FirebaseAuthInvalidCredentialsException -> "Credenciais incorretas"

            is FirebaseNetworkException -> "Falha na conexão"
            is FirebaseTooManyRequestsException -> "Usuário bloqueado temporariamente por muitas tentativas"
            else -> "Erro desconhecido"
        }

}

sealed class LfState {
    object Opening : LfState()
    object CurrentUserNotFound : LfState()
    object CurrentUserFound : LfState()
    object TryingToLogin : LfState()
    data class LoginFailed(val error: Exception) : LfState()
}
