package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.model.label.Label
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

const val FIRESTORE_COLLECTION_DEFAULT_LABELS = "defaultLabels"
const val FIRESTORE_COLLECTION_USER_LABELS = "labels"
private const val LABEL_TYPE = "type"
private const val LABEL_IS_OPERATIONAL = "isOperational"

class LabelRepository(fireStore: FirebaseFirestore) {

    private val write = LabWrite(fireStore)
    private val read = LabRead(fireStore)

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    suspend fun delete(labelId: String) = write.delete(labelId)

    suspend fun save(dto: LabelDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    suspend fun getLabelListByMasterUid(masterUid: String, flow: Boolean = false) =
        read.getLabelListByMasterUid(masterUid, flow)

    suspend fun getLabelListByMasterUidAndType(type: String, masterUid: String, flow: Boolean = false) =
        read.getLabelListByMasterUidAndType(type, masterUid, flow)

    suspend fun getLabelById(labelId: String, flow: Boolean = false) =
        read.getLabelById(labelId, flow)

    suspend fun getLabelListByMasterUidAndTypeAndOperational(
        masterUid: String,
        type: String,
        isOperational: Boolean,
        flow: Boolean = false
    ) = read.getLabelListByMasterUidAndTypeAndOperational(masterUid, type, isOperational, flow)

    suspend fun getDefaultLabelList(type: String, isOperational: Boolean, flow: Boolean = false) =
        read.getDefaultLabelList(type, isOperational, flow)

    suspend fun getDefaultExpendLabelList(isOperational: Boolean? = false, flow: Boolean = false) =
        read.getDefaultExpendLabelList(isOperational, flow)

    suspend fun getAllOperationalLabelListForDrivers(masterUid: String) =
        read.getAllOperationalLabelListForDrivers(masterUid)

}

private class LabRead(fireStore: FirebaseFirestore) {

    private val userCollection = fireStore.collection(FIRESTORE_COLLECTION_USER_LABELS)
    private val defaultCollection = fireStore.collection(FIRESTORE_COLLECTION_DEFAULT_LABELS)

    /**
     * Get all documents by User id.
     * @param masterUid The user id.
     * @return The mutable live data with a liste of labels for this user.
     */
    suspend fun getLabelListByMasterUid(
        masterUid: String,
        flow: Boolean = false
    ): LiveData<Response<List<Label>>> {
        val listener = userCollection.whereEqualTo(MASTER_UID, masterUid)

        return if (flow) listener.onSnapShot { it.toLabelList() }
        else listener.onComplete { it.toLabelList() }
    }

    /**
     * Retrieve all labels of a specific type.
     * @param type The label type for search.
     * @param masterUid The master user id.
     * @return A Live data with the list of labels.
     */
    suspend fun getLabelListByMasterUidAndType(
        type: String,
        masterUid: String,
        flow: Boolean = false
    ): LiveData<Response<List<Label>>> {
        val listener =
            userCollection.whereEqualTo(LABEL_TYPE, type).whereEqualTo(MASTER_UID, masterUid)

        return if (flow) listener.onSnapShot { it.toLabelList() }
        else listener.onComplete { it.toLabelList() }
    }

    /**
     *
     */
    suspend fun getLabelById(labelId: String, flow: Boolean = false): LiveData<Response<Label>> {
        val listener = userCollection.document(labelId)

        return if (flow) listener.onSnapShot { it.toLabelObject() }
        else listener.onComplete { it.toLabelObject() }
    }

    /**
     * User operational Labels
     */
    suspend fun getLabelListByMasterUidAndTypeAndOperational(
        masterUid: String,
        type: String,
        isOperational: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Label>>> {
        val listener = userCollection
            .whereEqualTo(MASTER_UID, masterUid)
            .whereEqualTo(LABEL_TYPE, type)
            .whereEqualTo(LABEL_IS_OPERATIONAL, isOperational)

        return if (flow) listener.onSnapShot { it.toLabelList() }
        else listener.onComplete { it.toLabelList() }
    }

    /**
     * Default operational Labels
     */
    suspend fun getDefaultLabelList(
        type: String,
        isOperational: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Label>>> {
        val listener = defaultCollection.whereEqualTo(LABEL_TYPE, type)
            .whereEqualTo(LABEL_IS_OPERATIONAL, isOperational)

        return if (flow) listener.onSnapShot { it.toLabelList() }
        else listener.onComplete { it.toLabelList() }
    }

    /**
     * Get Default expend labels
     */
    suspend fun getDefaultExpendLabelList(
        isOperational: Boolean? = false,
        flow: Boolean = false
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
                    when (responseA) {
                        is Response.Error -> mediator.value = responseA
                        is Response.Success -> responseA.data?.let { dataSet.addAll(it) }
                    }
                    deferredA.complete(Unit)
                }
                mediator.addSource(liveDataB) { responseB ->
                    when (responseB) {
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

private class LabWrite(fireStore: FirebaseFirestore) {

    private val userCollection = fireStore.collection(FIRESTORE_COLLECTION_USER_LABELS)

    /**
     * Delete a label.
     * @param id The id of the label.
     * @return LiveData with the result of operation.
     */
    suspend fun delete(id: String) {
        userCollection.document(id).delete().await()
    }

    /**
     * Add a new user or edit if already exists.
     * @param dto The label sent by the user.
     * @return The ID of the saved label.
     */
    suspend fun save(dto: LabelDto) {
        if (dto.id == null) create(dto)
        else update(dto)
    }

    private suspend fun update(dto: LabelDto) {
        val document = userCollection.document(dto.id!!)
        document.set(dto).await()
    }

    private suspend fun create(dto: LabelDto): String {
        val document = userCollection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

}