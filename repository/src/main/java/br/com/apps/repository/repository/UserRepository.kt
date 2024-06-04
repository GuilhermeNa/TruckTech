package br.com.apps.repository.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.user_dto.CommonUserDto
import br.com.apps.model.dto.user_dto.UserDto
import br.com.apps.model.mapper.toModel
import br.com.apps.model.model.user.User
import br.com.apps.repository.Resource
import br.com.apps.repository.util.toCommonUserObject
import br.com.apps.trucktech.expressions.getMonthFormatted
import br.com.apps.trucktech.expressions.getYearReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException
import java.time.LocalDateTime

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
                        userDto.toModel()
                    }?.let {
                        liveData.value = it
                    }
                }
            }

        return liveData
    }

    suspend fun getUserRequestNumber(uid: String): Int {
        var orderCode = -1
        var orderNumber = -1
        val listener = collection.document(uid)

        listener.get().addOnCompleteListener { task ->
            task.exception?.let { e -> throw e }
            task.result?.let { doc ->
                orderCode = doc.toCommonUserObject().orderCode
                orderNumber = doc.toCommonUserObject().orderNumber
                Log.d("teste", "pre")
            }
        }.await()

        Log.d("teste", "apos")
        if (orderCode != -1 && orderNumber != -1) {
            val year = LocalDateTime.now().getYearReference()
            val month = LocalDateTime.now().getMonthFormatted()
            val code = when (orderCode.toString().length) {
                1 -> "00$orderCode"
                2 -> "0$orderCode"
                else -> throw InvalidParameterException()
            }

            return ("$year$month$code$orderNumber").toInt()
        }

        throw InvalidParameterException("Failed to generate a request number")

    }

    suspend fun updateRequestNumber(uid: String) {
        collection.document(uid)
            .update("orderNumber", FieldValue.increment(1)).await()
    }

}