package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.TruckDto
import br.com.apps.model.mapper.TruckMapper
import br.com.apps.model.model.Truck
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject

private const val FIRESTORE_COLLECTION_TRUCKS = "trucks"

class FleetRepository(private val fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRUCKS)

    /**
     * add a new truck
     */
    fun save(truckDto: TruckDto): String {
        val document =
            if (truckDto.id != null) {
                collection.document(truckDto.id!!)
            } else {
                collection.document().also {
                    truckDto.id = it.id
                }
            }

        document.set(truckDto)
        return document.id
    }

    /**
     * delete by id
     */
    fun delete(truckId: String): Task<Void> {
        return fireStore.collection(FIRESTORE_COLLECTION_TRUCKS).document(truckId).delete()
    }

    /**
     * Get all trucks for this user
     */
    fun getAll(): MutableLiveData<List<Truck>> {
        val liveData = MutableLiveData<List<Truck>>()
        collection.addSnapshotListener { querySnapShot, _ ->
            querySnapShot?.let {
                liveData.value = getMappedDataSet(it)
            }
        }
        return liveData
    }

    private fun getMappedDataSet(querySnapShot: QuerySnapshot): List<Truck> {
        return querySnapShot.documents.mapNotNull { document ->
            document.toObject<TruckDto>()?.let { truckDto ->
                TruckMapper.toModel(truckDto, document.id)
            }
        }
    }

    /**
     * get a truck by id
     */
    fun getById(truckId: String): DocumentReference {
        return fireStore.collection(FIRESTORE_COLLECTION_TRUCKS).document(truckId)
    }

    /**
     * get by driver id
     */
    fun getByDriverId(driverId: String): MutableLiveData<Truck> {
        val liveData = MutableLiveData<Truck>()

        collection.whereEqualTo("driverId", driverId).limit(1).get()
            .addOnSuccessListener { querySnapShot ->
                querySnapShot?.let {
                    getMappedTruck(it)?.let {
                        liveData.value = it
                    }
                }
            }

        return liveData
    }

    private fun getMappedTruck(querySnapShot: QuerySnapshot): Truck? {
        querySnapShot.documents.mapNotNull { document ->
            document.toObject<TruckDto>()?.let { truckDto ->
                return TruckMapper.toModel(truckDto, document.id)
            }
        }
        return null
    }

}