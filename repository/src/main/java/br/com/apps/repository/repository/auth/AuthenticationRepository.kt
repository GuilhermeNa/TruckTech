package br.com.apps.repository.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.util.Resource
import br.com.apps.repository.util.Response
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthenticationRepository(private val fireBaseAuth: FirebaseAuth): AuthInterface {

    /**
     * This method is responsible for authenticate a new user in fireBase
     *
     * @param email
     * @param password
     * @return A resource containing a string UID, or blank if any error occurred
     */
    override fun authenticateNewUserWithEmailAndPassword(
        email: String,
        password: String
    ): LiveData<Resource<String>> {
        val liveData = MutableLiveData<Resource<String>>()
        try {
            fireBaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    it.user?.let { user ->
                        liveData.value = Resource(data = user.uid)
                    }
                }
                .addOnFailureListener { exception ->
                    val message = when (exception) {
                        is FirebaseAuthWeakPasswordException -> "Senha deve ter mais de 6 digitos"
                        is FirebaseAuthInvalidCredentialsException -> "E-mail inválido"
                        is FirebaseAuthUserCollisionException -> "Email já cadastrado"
                        else -> "Erro desconhecido"
                    }
                    liveData.value = Resource(data = "", error = message)
                }
        } catch (e: IllegalArgumentException) {
            liveData.value = Resource(data = "", error = "Preencha e-mail e senha")
        }
        return liveData
    }

    /**
     * Try to log in to the fireBase .
     *
     * @param user
     * @param password
     *
     * @return a Resource containing a boolean value of true if the user has logged in
     * correctly. If not, returns a Resource containing a boolean value of false along with a
     * string representing the exception message
     */
    override fun signIn(user: String, password: String): LiveData<Response<Unit>> {
            val liveData = MutableLiveData<Response<Unit>>()

            fireBaseAuth.signInWithEmailAndPassword(user, password)
                .addOnFailureListener { e -> liveData.postValue(Response.Error(e)) }
                .addOnSuccessListener { liveData.postValue(Response.Success()) }

        return liveData
    }

    /**
     * Retrieve active user in fireBase
     */
    override fun getCurrentUser(): FirebaseUser? {
        return fireBaseAuth.currentUser
    }

    /**
     * Try to login in fireBase
     */
    override fun signOut() {
        fireBaseAuth.signOut()
    }

    override suspend fun updatePassword(oldPassword: String, newPassword: String): Response<Boolean> {
        return try {
            val credential =
                EmailAuthProvider.getCredential(fireBaseAuth.currentUser!!.email!!, oldPassword)

            fireBaseAuth.currentUser!!.reauthenticate(credential).await()

            fireBaseAuth.currentUser!!.updatePassword(newPassword).await()

            Response.Success(data = true)

        } catch (e: Exception) {
            Response.Error(exception = e)
        }
    }

}