package br.com.apps.repository.repository

import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.toExpendList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await

private const val FIRESTORE_COLLECTION_EXPENDS = "expends"

class ExpendRepository(fireStore: FirebaseFirestore) {

    private val parentCollection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    fun getExpendListForThisTravelQuery(
        query: QuerySnapshot,
        dataSet: List<Travel>,
        deferred: CompletableDeferred<Unit>
    ) {
        var tasksCounter = 0
        query.forEach { document ->
            document.reference.collection(FIRESTORE_COLLECTION_EXPENDS)
                .addSnapshotListener { querySnap, _ ->
                    val expendsData = querySnap?.toExpendList()
                    expendsData?.firstOrNull()?.let { expend ->
                        dataSet
                            .firstOrNull { it.id == expend.travelId }
                            ?.also { it.expendsList = expendsData }
                    }
                    tasksCounter ++
                    if (tasksCounter == query.size()) deferred.complete(Unit)
                }
        }
    }

    suspend fun deleteExpendForThisTravel(travelId: String, expendId: String) {
        parentCollection
            .document(travelId)
            .collection(FIRESTORE_COLLECTION_EXPENDS)
            .document(expendId)
            .delete()
            .await()
    }

}