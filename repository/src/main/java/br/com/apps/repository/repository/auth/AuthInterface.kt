package br.com.apps.repository.repository.auth

import androidx.lifecycle.LiveData
import br.com.apps.repository.util.Resource
import br.com.apps.repository.util.Response
import com.google.firebase.auth.FirebaseUser

interface AuthInterface {

    fun authenticateNewUserWithEmailAndPassword(email: String, password: String): LiveData<Resource<String>>

    fun signIn(user: String, password: String): LiveData<Response<Unit>>

    fun getCurrentUser(): FirebaseUser?

    fun signOut()

    suspend fun updatePassword(oldPassword: String,newPassword: String): Response<Boolean>

}