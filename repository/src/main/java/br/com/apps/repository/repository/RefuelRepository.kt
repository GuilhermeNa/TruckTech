package br.com.apps.repository.repository

import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.toRefuelList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await

private const val FIRESTORE_COLLECTION_REFUELS = "refuels"

class RefuelRepository(fireStore: FirebaseFirestore) {

    private val parentCollection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    fun getRefuelListForThisTravelQuery(
        query: QuerySnapshot,
        dataSet: List<Travel>,
        deferred: CompletableDeferred<Unit>
    ) {
        var tasksCounter = 0
        query.forEach { document ->
            document.reference.collection(FIRESTORE_COLLECTION_REFUELS)
                .addSnapshotListener { querySnap, _ ->
                    val refuelsData = querySnap?.toRefuelList()
                    refuelsData?.firstOrNull()?.let { refuel ->
                        dataSet
                            .firstOrNull { it.id == refuel.travelId }
                            ?.also { it.refuelsList = refuelsData }
                    }
                    tasksCounter++
                    if (tasksCounter == query.size()) deferred.complete(Unit)

                }
        }
    }

    suspend fun deleteRefuelForThisTravel(travelId: String, refuelId: String) {
        parentCollection
            .document(travelId)
            .collection(FIRESTORE_COLLECTION_REFUELS)
            .document(refuelId)
            .delete()
            .await()
    }

}