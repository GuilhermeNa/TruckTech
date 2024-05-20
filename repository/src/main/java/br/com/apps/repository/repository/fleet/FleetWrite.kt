package br.com.apps.repository.repository.fleet

import br.com.apps.model.dto.TruckDto
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRUCKS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FleetWrite(fireStore: FirebaseFirestore): FleetWriteI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRUCKS)

    override suspend fun save(dto: TruckDto) {
        if (dto.id == null) create(dto)
        else update(dto)
    }

    private suspend fun create(dto: TruckDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

    private suspend fun update(dto: TruckDto) {
        val document = collection.document(dto.id!!)
        document.set(dto).await()
    }

    override suspend fun delete(truckId: String) {
        collection.document(truckId).delete().await()
    }

}