package br.com.apps.trucktech.ui.fragments.base_fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.apps.repository.repository.auth.AuthenticationRepository
import br.com.apps.repository.util.Response
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    lateinit var userId: String

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    fun getCurrentUser(): FirebaseUser? {
        val currentUser = repository.getCurrentUser()
        currentUser?.let {
            if (!::userId.isInitialized) userId = currentUser.uid
        }
        return currentUser
    }

    /**
     * Try to log in to the system.
     *
     * This method will send the data to the repository for authentication.
     *
     * @param credentials <email, password>
     *
     * @return liveData with Resource from repository
     */
    fun signIn(credentials: Pair<String, String>): LiveData<Response<Unit>> {
        return repository.signIn(credentials.first, credentials.second)
    }

    fun signOut() {
        repository.signOut()
    }

}

