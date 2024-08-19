package br.com.apps.usecase.usecase

import br.com.apps.repository.repository.auth.AuthenticationRepository
import br.com.apps.repository.util.Response
import com.google.firebase.auth.FirebaseUser

class AuthenticationUseCase(private val authRepository: AuthenticationRepository) {

    /**
     * Retrieve the user currently logged in.
     */
    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

    suspend fun updatePassword(oldPassword: String, newPassword: String): Response<Boolean> {
        return authRepository.updatePassword(oldPassword, newPassword)
    }

}