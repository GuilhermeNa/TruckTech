package br.com.apps.trucktech.ui.fragments.nav_settings.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.usecase.AuthenticationUseCase

class ChangePasswordFragmentViewModel(private val useCase: AuthenticationUseCase): ViewModel() {

    suspend fun updatePassword(oldPass: String, newPass: String) =
        liveData(viewModelScope.coroutineContext) {
            emit(useCase.updatePassword(oldPass, newPass))
        }

}