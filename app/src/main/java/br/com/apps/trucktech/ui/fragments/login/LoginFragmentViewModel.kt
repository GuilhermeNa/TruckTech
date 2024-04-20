package br.com.apps.trucktech.ui.fragments.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.repository.Resource
import br.com.apps.usecase.AuthenticationUseCase
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginFragmentViewModel(private val authUseCase: AuthenticationUseCase): ViewModel() {

    var userId: String? = null

    private var _signIn = MutableLiveData<Resource<Boolean>>()
    val signIn get() = _signIn

    fun signIn(credentials: Pair<String, String>) {
        viewModelScope.launch {
            authUseCase.signIn(credentials).asFlow().collect {
                _signIn.value = it
            }
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        val currentUser = authUseCase.getCurrentUser()
        currentUser?.let {
            if(userId == null) {
                userId = currentUser.uid
            }
        }
        return currentUser
    }

    fun signOut() {
        authUseCase.signOut()
    }

}