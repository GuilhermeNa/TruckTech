package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.user_dto.CommonUserDto
import br.com.apps.model.dto.user_dto.UserDto
import br.com.apps.model.mapper.UserMapper
import br.com.apps.model.model.user.User
import br.com.apps.repository.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

private const val FIRESTORE_COLLECTION_USERS = "users"

class UserRepository(private val fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_USERS)

    /**
     * This method is responsible for create a new User
     * @param userDto
     * @return a Resource containing a boolean value of true if the user was created.
     * If not, returns a Resource containing a boolean value of false along with a string
     * representing the exception message
     */
    fun createANewMasterUser(userDto: UserDto): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        userDto.masterUid?.let { masterUid ->
            collection.document(masterUid).set(userDto)
                .addOnSuccessListener {
                    liveData.value = Resource(data = true)
                }
                .addOnFailureListener {
                    liveData.value = Resource(data = false, error = "Falha ao criar usuário")
                }
        }

        return liveData
    }

    /**
     * Create a new Common User
     */
    fun createANewCommonUser(userDto: CommonUserDto): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        userDto.uid?.let { uid ->
            collection.document(uid).set(userDto)
                .addOnSuccessListener {
                    liveData.value = Resource(data = true)
                }
                .addOnFailureListener {
                    liveData.value = Resource(data = false, error = "Falha ao criar usuário")
                }
        }
        return liveData
    }

    fun getCommonUser(uid: String): LiveData<User> {
        val liveData = MutableLiveData<User>()

        collection.document(uid).get()
            .addOnSuccessListener { s ->
                s?.let { document ->
                    document.toObject<CommonUserDto>()?.let { userDto ->
                        UserMapper.toModel(userDto)
                    }?.let {
                        liveData.value = it
                    }
                }
            }

        return liveData
    }


}