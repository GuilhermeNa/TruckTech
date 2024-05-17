package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import br.com.apps.model.factory.UserDtoFactory
import br.com.apps.model.mapper.UserMapper
import br.com.apps.model.model.UserCredentials
import br.com.apps.repository.Resource
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseUser

class AuthenticationUseCase(
    private val authRepository: AuthenticationRepository,
    private val userUseCase: UserUseCase
) {

    /**
     * This method is responsible for the flow of registering new Users. First, registering new
     * credentials for authentication. After the registration is completed, it obtains a UID and
     * creates a new user, associating them together
     *
     * @param credentials are the necessary data collected from the user input texts.
     */
    fun authenticateANewUser(credentials: UserCredentials): LiveData<Resource<Boolean>> {
        val uidLiveData = authRepository.authenticateNewUserWithEmailAndPassword(
            credentials.email,
            credentials.password
        )

        val mediatorLiveData = MediatorLiveData<Resource<Boolean>>()
        mediatorLiveData.addSource(uidLiveData) { uidResource ->
            if (uidResource.data.isNotBlank()) {
                createANewUser(credentials, uidResource, mediatorLiveData)
            } else {
                mediatorLiveData.value = Resource(data = false, error = uidLiveData.value?.error)
            }
        }

        return mediatorLiveData
    }

    private fun createANewUser(
        credentials: UserCredentials,
        authResource: Resource<String>,
        mediatorLiveData: MediatorLiveData<Resource<Boolean>>
    ) {
        val userDto = UserMapper.toDto(credentials)
        userDto.masterUid = authResource.data

        mediatorLiveData.addSource(userUseCase.createNewUser(userDto)) { userResource ->
            mediatorLiveData.value = userResource
        }

    }

    /**
     * Autehtnicate a user that already exists
     */
    fun authenticateAnExistingUser(credentials: UserCredentials, employeeId: String):
            LiveData<Resource<Boolean>> {
        val mediatorLiveData = MediatorLiveData<Resource<Boolean>>()
        val userDto = UserDtoFactory.createObject(credentials, employeeId)

        val uidLiveData = authRepository.authenticateNewUserWithEmailAndPassword(
            credentials.email,
            credentials.password
        )

        mediatorLiveData.addSource(uidLiveData) {
            it?.let { uidResource ->
                if (uidResource.data.isNotBlank()) {
                    userDto.uid = uidResource.data
                    mediatorLiveData.addSource(userUseCase.createNewUser(userDto)) { userResource ->
                        mediatorLiveData.value = userResource
                    }
                } else {
                    mediatorLiveData.value =
                        Resource(data = false, error = uidLiveData.value?.error)
                }
            }
        }
        return mediatorLiveData
    }

    /**
     * Retrieve the user currently logged in.
     */
    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

    /**
     * Try to log in to the system.
     *
     * This method will send the data to the repository for authentication
     *
     * @param credentials <email, password>
     *
     * @return liveData with Resource from repository
     */
    fun signIn(credentials: Pair<String, String>): LiveData<Resource<Boolean>> {
        return authRepository.signInWithEmailAndPassword(credentials.first, credentials.second)
    }

    /**
     * Log out to the system
     */
    fun signOut() {
        return authRepository.signOut()
    }

    suspend fun updatePassword(oldPassword: String, newPassword: String): Response<Boolean> {
        return authRepository.updatePassword(oldPassword, newPassword)
    }

}