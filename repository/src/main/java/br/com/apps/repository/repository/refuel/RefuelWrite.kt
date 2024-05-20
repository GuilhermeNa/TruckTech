package br.com.apps.repository.repository.refuel

import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REFUELS
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRAVELS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

class RefuelWrite(fireStore: FirebaseFirestore): RefuelWriteI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REFUELS)
    private val parentCollection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    /**
     * Saves the [RefuelDto] object.
     * If the ID of the [RefuelDto] is null, it creates a new [Refuel].
     * If the ID is not null, it updates the existing [Refuel].
     *
     * @param dto The [RefuelDto] object to be saved.
     */
    override suspend fun save(dto: RefuelDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: RefuelDto): String {
        val document = collection.document()
        dto.id = document.id

        document
            .set(dto)
            .await()

        return document.id
    }

    private suspend fun update(dto: RefuelDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()

    }

    /**
     * Deletes an [Refuel] document from the database based on the specified ID.
     *
     * @param refuelId The ID of the document to be deleted.
     */
    override suspend fun delete(refuelId: String) {
        collection
            .document(refuelId)
            .delete()
            .await()
    }

    /**
     * Deletes a specific [Refuel] entry associated with a [Travel].
     *
     * @param travelId The ID of the travel from which to delete the refuel entry.
     * @param refuelId The ID of the refuel entry to delete.
     */
    override suspend fun deleteRefuelForThisTravel(travelId: String, refuelId: String) {
        parentCollection
            .document(travelId)
            .collection(FIRESTORE_COLLECTION_REFUELS)
            .document(refuelId)
            .delete()
            .await()
    }

}