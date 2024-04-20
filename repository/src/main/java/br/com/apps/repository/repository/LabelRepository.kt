package br.com.apps.repository.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.mapper.LabelMapper
import br.com.apps.model.mapper.toModel
import br.com.apps.model.model.Label
import br.com.apps.repository.MASTER_UID
import br.com.apps.repository.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val FIRESTORE_COLLECTION_LABELS = "labels"
private const val LABEL_TYPE = "type"
private const val LABEL_IS_OPERATIONAL = "isOperational"

class LabelRepository(private val fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_LABELS)

    /**
     * Get all documents by User id.
     * @param uid The user id.
     * @return The mutable live data with a liste of labels for this user.
     */
    fun getAll(uid: String): MutableLiveData<List<Label>> {
        val liveData = MutableLiveData<List<Label>>()
        collection.whereEqualTo(MASTER_UID, uid)
        collection.addSnapshotListener { s, _ ->
            s?.let { snapShot ->
                liveData.value = getMappedDataSet(snapShot)
            }
        }
        return liveData
    }

    private fun getMappedDataSet(snapShot: QuerySnapshot): List<Label> {
        return snapShot.documents.mapNotNull { document ->
            document.toObject<LabelDto>()?.let { labelDto ->
                LabelMapper.toModel(labelDto, document.id)
            }
        }
    }

    /**
     * Retrieve all labels of a specific type.
     * @param type The label type for search.
     * @param uid The master user id.
     * @return A Live data with the list of labels.
     */
    suspend fun getAllByType(type: String, uid: String): LiveData<List<Label>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<List<Label>>()
            collection
                .whereEqualTo(LABEL_TYPE, type)
                .whereEqualTo(MASTER_UID, uid)
                .addSnapshotListener { s, _ ->
                    s?.let { snapShot ->
                        liveData.value = getMappedDataSet(snapShot)
                    }
                }
            return@withContext liveData
        }
    }

    fun getById(id: String): LiveData<Label> {
        val liveData = MutableLiveData<Label>()
        collection.document(id).addSnapshotListener { s, _ ->
            s?.let { document ->
                document.toObject<LabelDto>()?.let { labelDto ->
                    LabelMapper.toModel(labelDto)
                }?.let {
                    Log.d("teste", "getById: label devolvido $it")
                    liveData.value = it
                }
            }
        }
        return liveData
    }

    fun getByPlate(plate: String) {}

    /**
     * Delete a label.
     * @param id The id of the label.
     * @return LiveData with the result of operation.
     */
    fun delete(id: String): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        collection.document(id).delete()
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
                collection.document(labelDto.id!!)
            } else {
                collection.document().also {
                    labelDto.id = it.id
                }
            }
        document.set(labelDto)
        return document.id
    }

    suspend fun getOperationalLabels(masterUid: String, type: String): LiveData<Resource<List<Label>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Resource<List<Label>>>()

            collection
                .whereEqualTo(MASTER_UID, masterUid)
                .whereEqualTo(LABEL_TYPE, type)
                .whereEqualTo(LABEL_IS_OPERATIONAL, true)
                .get()
                .addOnSuccessListener {
                    it?.let { query ->
                        val labelsList = query.documents.mapNotNull { document ->
                            document.toObject<LabelDto>()?.toModel()
                        }
                        liveData.postValue(Resource(data = labelsList))
                    }
                }

            return@withContext liveData
        }
    }

}