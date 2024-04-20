package br.com.apps.repository.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.model.label.Label
import br.com.apps.repository.MASTER_UID
import br.com.apps.repository.Response
import br.com.apps.repository.toLabelList
import br.com.apps.repository.toLabelObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

const val FIRESTORE_COLLECTION_DEFAULT_LABELS = "defaultLabels"
const val FIRESTORE_COLLECTION_USER_LABELS = "labels"
private const val LABEL_TYPE = "type"
private const val LABEL_IS_OPERATIONAL = "isOperational"

class LabelRepository(private val fireStore: FirebaseFirestore) {

    private val userCollection = fireStore.collection(FIRESTORE_COLLECTION_USER_LABELS)
    private val defaultCollection = fireStore.collection(FIRESTORE_COLLECTION_DEFAULT_LABELS)

    /**
     * Get all documents by User id.
     * @param uid The user id.
     * @return The mutable live data with a liste of labels for this user.
     */
    suspend fun getAll(uid: String): LiveData<Response<List<Label>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Label>>>()

            userCollection
                .whereEqualTo(MASTER_UID, uid)
                .addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        val dataSet = query.toLabelList()
                        liveData.postValue(Response.Success(data = dataSet))
                    }
                }

            return@withContext liveData
        }
    }

    /**
     * Retrieve all labels of a specific type.
     * @param type The label type for search.
     * @param uid The master user id.
     * @return A Live data with the list of labels.
     */
    suspend fun getAllByType(type: String, uid: String): LiveData<Response<List<Label>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Label>>>()

            userCollection
                .whereEqualTo(LABEL_TYPE, type)
                .whereEqualTo(MASTER_UID, uid)
                .addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        val dataSet = query.toLabelList()
                        liveData.postValue(Response.Success(data = dataSet))
                    }
                }

            return@withContext liveData
        }
    }

    suspend fun getLabelById(labelId: String): LiveData<Response<Label>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<Label>>()

            userCollection
                .document(labelId)
                .addSnapshotListener { documentSnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    documentSnap?.let { document ->
                        val label = document.toLabelObject()
                        liveData.postValue(Response.Success(label))
                    }
                }

            return@withContext liveData
        }
    }

    /**
     * Delete a label.
     * @param id The id of the label.
     * @return LiveData with the result of operation.
     */
    fun delete(id: String): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        userCollection.document(id).delete()
        liveData.value = true
        return liveData
    }

    /**
     * Add a new user or edit if already exists.
     * @param labelDto The label sent by the user.
     * @return The ID of the saved label.
     */
    fun save(labelDto: LabelDto): String {
        val document =
            if (labelDto.id != null) {
                userCollection.document(labelDto.id!!)
            } else {
                userCollection.document().also {
                    labelDto.id = it.id
                }
            }
        document.set(labelDto)
        return document.id
    }

    /**
     * User operational Labels
     */
    suspend fun getUserLabelList(
        masterUid: String,
        type: String,
        isOperational: Boolean
    ): LiveData<Response<List<Label>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Label>>>()

            userCollection
                .whereEqualTo(MASTER_UID, masterUid)
                .whereEqualTo(LABEL_TYPE, type)
                .whereEqualTo(LABEL_IS_OPERATIONAL, isOperational)
                .get()
                .addOnSuccessListener { querySnap ->
                    querySnap?.let { query ->
                        val labelList = query.toLabelList()
                        liveData.postValue(Response.Success(data = labelList))
                    }
                }

            return@withContext liveData
        }
    }

    /**
     * Default operational Labels
     */
    suspend fun getDefaultLabelList(
        type: String,
        isOperational: Boolean
    ): LiveData<Response<List<Label>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Label>>>()

            defaultCollection
                .whereEqualTo(LABEL_TYPE, type)
                .whereEqualTo(LABEL_IS_OPERATIONAL, isOperational)
                .get()
                .addOnSuccessListener { querySnap ->
                    querySnap?.let { query ->
                        val labelList = query.toLabelList()
                        liveData.postValue(Response.Success(data = labelList))
                    }
                }

            return@withContext liveData
        }
    }

    /**
     * Get Default expend labels
     */
    suspend fun getDefaultExpendLabelList(isOperational: Boolean? = false): LiveData<Response<List<Label>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Label>>>()

            defaultCollection
                .whereEqualTo(LABEL_IS_OPERATIONAL, isOperational)
                .whereIn(LABEL_TYPE, listOf("COST", "EXPENSE"))
                .addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        val labelList = query.toLabelList()
                        liveData.postValue(Response.Success(labelList))
                        Log.d("teste", "data A encontrada")
                    }
                }

            return@withContext liveData
        }
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
    suspend fun getAllOperationalLabelListForDrivers(masterUid: String): LiveData<Response<List<Label>>> {
        return coroutineScope {
            val mediator = MediatorLiveData<Response<List<Label>>>()
            val dataSet = mutableListOf<Label>()

            CoroutineScope(Dispatchers.Main).launch {
                val deferredA = CompletableDeferred<Unit>()
                val deferredB = CompletableDeferred<Unit>()

                val liveDataA = getDefaultOperationalLabelListOfExpendsForDrivers()
                val liveDataB = getUserOperationalLabelListOfExpendsForDrivers(masterUid)

                mediator.addSource(liveDataA) { responseA ->
                    when(responseA) {
                        is Response.Error -> mediator.value = responseA
                        is Response.Success -> responseA.data?.let { dataSet.addAll(it) }
                    }
                    deferredA.complete(Unit)
                }
                mediator.addSource(liveDataB) { responseB ->
                    when(responseB) {
                        is Response.Error -> mediator.value = responseB
                        is Response.Success -> responseB.data?.let { dataSet.addAll(it) }
                    }
                    deferredB.complete(Unit)
                }

                awaitAll(deferredA, deferredB)
                mediator.removeSource(liveDataA)
                mediator.removeSource(liveDataB)
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