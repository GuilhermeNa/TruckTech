package br.com.apps.repository.repository.label

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.label.Label
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DEFAULT_LABELS
import br.com.apps.repository.util.FIRESTORE_COLLECTION_USER_LABELS
import br.com.apps.repository.util.LABEL_IS_OPERATIONAL
import br.com.apps.repository.util.LABEL_TYPE
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toLabelList
import br.com.apps.repository.util.toLabelObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LabelReadImpl(fireStore: FirebaseFirestore): LabelReadInterface {

    private val userCollection = fireStore.collection(FIRESTORE_COLLECTION_USER_LABELS)
    private val defaultCollection = fireStore.collection(FIRESTORE_COLLECTION_DEFAULT_LABELS)

    override suspend fun getLabelListByMasterUid(masterUid: String, flow: Boolean)
    : LiveData<Response<List<Label>>> {
        val listener = userCollection.whereEqualTo(MASTER_UID, masterUid)
        return if (flow) listener.onSnapShot { it.toLabelList() }
        else listener.onComplete { it.toLabelList() }
    }

    override suspend fun getLabelListByMasterUidAndType(
        type: String,
        masterUid: String,
        flow: Boolean
    ): LiveData<Response<List<Label>>> {
        val listener =
            userCollection.whereEqualTo(LABEL_TYPE, type).whereEqualTo(MASTER_UID, masterUid)
        return if (flow) listener.onSnapShot { it.toLabelList() }
        else listener.onComplete { it.toLabelList() }
    }

    override suspend fun getLabelById(labelId: String, flow: Boolean): LiveData<Response<Label>> {
        val listener = userCollection.document(labelId)
        return if (flow) listener.onSnapShot { it.toLabelObject() }
        else listener.onComplete { it.toLabelObject() }
    }

    override suspend fun getLabelListByMasterUidAndTypeAndOperational(
        masterUid: String,
        type: String,
        isOperational: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Label>>> {
        val listener = userCollection
            .whereEqualTo(MASTER_UID, masterUid)
            .whereEqualTo(LABEL_TYPE, type)
            .whereEqualTo(LABEL_IS_OPERATIONAL, isOperational)
        return if (flow) listener.onSnapShot { it.toLabelList() }
        else listener.onComplete { it.toLabelList() }
    }

    override suspend fun getDefaultLabelList(
        type: String,
        isOperational: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Label>>> {
        val listener = defaultCollection.whereEqualTo(LABEL_TYPE, type)
            .whereEqualTo(LABEL_IS_OPERATIONAL, isOperational)
        return if (flow) listener.onSnapShot { it.toLabelList() }
        else listener.onComplete { it.toLabelList() }
    }

    override suspend fun getDefaultExpendLabelList(
        isOperational: Boolean?,
        flow: Boolean
    ): LiveData<Response<List<Label>>> {
        val listener = defaultCollection
            .whereEqualTo(LABEL_IS_OPERATIONAL, isOperational)
            .whereIn(LABEL_TYPE, listOf("COST", "EXPENSE"))
        return if (flow) listener.onSnapShot { it.toLabelList() }
        else listener.onComplete { it.toLabelList() }
    }

    /**
     * Retrieves a LiveData object containing a response of operational [Label] data to be used by
     * drivers associated with the specified master user ID.
     *
     * This function fetches operational label data asynchronously for all drivers associated with the given master user ID.
     * It combines the results from two different sources of label data:
     *   1. Default Operational Labels: predefined labels applicable to all drivers.
     *   2. User Labels: labels created by the user, which are exclusive to them and can only be used by the creator.
     *
     * @param masterUid The master user ID for which operational label data is to be retrieved.
     * @return A LiveData object containing a response of operational label data for all drivers associated with the specified master user ID.
     */
    override suspend fun getAllOperationalLabelListForDrivers(masterUid: String): LiveData<Response<List<Label>>> {
        return coroutineScope {
            val mediator = MediatorLiveData<Response<List<Label>>>()
            val dataSet = mutableListOf<Label>()

            CoroutineScope(Dispatchers.Main).launch {
                val deferredA = CompletableDeferred<Unit>()
                val deferredB = CompletableDeferred<Unit>()

                val liveDataA = getDefaultOperationalLabelListOfExpendsForDrivers()
                val liveDataB = getUserOperationalLabelListOfExpendsForDrivers(masterUid)

                mediator.addSource(liveDataA) { responseA ->
                    when (responseA) {
                        is Response.Error -> mediator.value = responseA
                        is Response.Success -> responseA.data?.let { dataSet.addAll(it) }
                    }
                    mediator.removeSource(liveDataA)
                    deferredA.complete(Unit)
                }
                mediator.addSource(liveDataB) { responseB ->
                    when (responseB) {
                        is Response.Error -> mediator.value = responseB
                        is Response.Success -> responseB.data?.let { dataSet.addAll(it) }
                    }
                    mediator.removeSource(liveDataB)
                    deferredB.complete(Unit)
                }

                awaitAll(deferredA, deferredB)
                mediator.value = Response.Success(dataSet)
            }

            return@coroutineScope mediator
        }
    }

    private suspend fun getDefaultOperationalLabelListOfExpendsForDrivers(): LiveData<Response<List<Label>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Label>>>()

            defaultCollection
                .whereEqualTo(LABEL_IS_OPERATIONAL, true)
                .whereIn(LABEL_TYPE, listOf("COST", "EXPENSE"))
                .get()
                .addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        val labelList = query.toLabelList()
                        liveData.postValue(Response.Success(labelList))
                    }
                }.await()

            return@withContext liveData
        }
    }

    private suspend fun getUserOperationalLabelListOfExpendsForDrivers(masterUid: String): LiveData<Response<List<Label>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Label>>>()

            userCollection
                .whereEqualTo(MASTER_UID, masterUid)
                .whereEqualTo(LABEL_IS_OPERATIONAL, true)
                .whereIn(LABEL_TYPE, listOf("COST", "EXPENSE"))
                .get()
                .addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        val labelList = query.toLabelList()
                        liveData.postValue(Response.Success(labelList))
                    }
                }.await()

            return@withContext liveData
        }
    }

}