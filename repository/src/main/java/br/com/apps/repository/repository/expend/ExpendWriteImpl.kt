package br.com.apps.repository.repository.expend

import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EXPENDS
import com.google.firebase.firestore.FirebaseFirestore
import java.security.InvalidParameterException

class ExpendWriteImpl(fireStore: FirebaseFirestore): ExpendWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EXPENDS)

    override suspend fun delete(expendId: String?) {
        if(expendId == null) throw NullPointerException("Id is null")
        collection.document(expendId).delete()
    }

    override suspend fun save(dto: ExpendDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private fun create(dto: ExpendDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto)
        return document.id
    }

    private fun update(dto: ExpendDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto)
    }

}