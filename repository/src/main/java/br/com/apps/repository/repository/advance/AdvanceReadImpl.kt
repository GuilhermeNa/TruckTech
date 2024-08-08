package br.com.apps.repository.repository.advance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.payroll.Advance
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ADVANCES
import br.com.apps.repository.util.ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toAdvanceList
import br.com.apps.repository.util.toAdvanceObject
import br.com.apps.repository.util.validateId
import br.com.apps.repository.util.validateIds
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdvanceReadImpl(fireStore: FirebaseFirestore) : AdvanceReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_ADVANCES)

    override suspend fun fetchAdvanceListByEmployeeId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Advance>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toAdvanceList() }
            false -> listener.onComplete { it.toAdvanceList() }
        }
    }

    override suspend fun fetchAdvanceListByEmployeeIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Advance>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> MutableLiveData(error) }

        val listener = collection.whereIn(EMPLOYEE_ID, ids)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toAdvanceList() }
            false -> listener.onComplete { it.toAdvanceList() }
        }
    }

    override suspend fun fetchAdvanceById(
        id: String,
        flow: Boolean
    ): LiveData<Response<Advance>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toAdvanceObject() }
            false -> listener.onComplete { it.toAdvanceObject() }
        }
    }

    override suspend fun fetchAdvanceByIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Advance>>> = withContext(Dispatchers.IO){
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(ID, ids)

        return@withContext when(flow) {
            true -> listener.onSnapShot { it.toAdvanceList() }
            false -> listener.onComplete { it.toAdvanceList() }
        }
    }

}