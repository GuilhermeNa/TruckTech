package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.FineDto
import br.com.apps.model.mapper.FineMapper
import br.com.apps.model.model.Fine
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject

private const val FIRESTORE_COLLECTION_FINES = "fines"

class FineRepository(private val fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FINES)

    fun getAllByDriverId(driverId: String): LiveData<List<Fine>> {
        val liveData = MutableLiveData<List<Fine>>()

        collection.whereEqualTo("driverId", driverId).get()
            .addOnSuccessListener { querySnapShot ->
                querySnapShot?.let {
                  liveData.value = getMappedDataSet(it)
                }
            }

        return liveData
    }

    private fun getMappedDataSet(querySnapShot: QuerySnapshot): List<Fine> {
        return querySnapShot.documents.mapNotNull { document ->
            document.toObject<FineDto>()?.let { fineDto ->
                FineMapper.toModel(fineDto)
            }
        }
    }


}