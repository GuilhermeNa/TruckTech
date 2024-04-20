package br.com.apps.repository.repository

import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.toFreightList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await

private const val FIRESTORE_COLLECTION_FREIGHTS = "freights"

class FreightRepository(fireStore: FirebaseFirestore) {

    private val parentCollection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    fun getFreightListForThisTravelQuery(
        query: QuerySnapshot,
        dataSet: List<Travel>,
        deferred: CompletableDeferred<Unit>,
    ) {
        var tasksCounter = 0
        query.forEach { document ->
            document.reference.collection(FIRESTORE_COLLECTION_FREIGHTS)
                .addSnapshotListener { querySnap, _ ->
                    val freightsData = querySnap?.toFreightList()
                    val travelId = freightsData?.firstOrNull()?.travelId
                    travelId?.let { id ->
                        val travel = dataSet.firstOrNull { it.id == id }
                        travel?.freightsList= freightsData
                    }
                    tasksCounter++
                    if (tasksCounter == query.size()) {
                        deferred.complete(Unit)
                    }
                }
        }
    }

    suspend fun deleteFreightForThisTravel(travelId: String, freightId: String) {
        parentCollection
            .document(travelId)
            .collection(FIRESTORE_COLLECTION_FREIGHTS)
            .document(freightId)
            .delete()
            .await()
    }

}