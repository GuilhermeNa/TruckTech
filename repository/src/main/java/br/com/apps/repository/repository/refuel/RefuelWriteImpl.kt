package br.com.apps.repository.repository.refuel

import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REFUELS
import com.google.firebase.firestore.FirebaseFirestore
import java.security.InvalidParameterException

class RefuelWriteImpl(fireStore: FirebaseFirestore): RefuelWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REFUELS)

    override suspend fun save(dto: RefuelDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private fun create(dto: RefuelDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto)
        return document.id
    }

    private fun update(dto: RefuelDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto)
    }

    override suspend fun delete(refuelId: String?) {
        if(refuelId == null) throw NullPointerException("Id is null")
        collection.document(refuelId).delete()
    }

}