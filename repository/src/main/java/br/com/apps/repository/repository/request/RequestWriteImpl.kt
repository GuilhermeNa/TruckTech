package br.com.apps.repository.repository.request

import br.com.apps.model.dto.request.RequestDto
import br.com.apps.model.exceptions.EmptyIdException
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.EMPTY_ID_EXCEPTION
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REQUESTS
import br.com.apps.repository.util.IS_UPDATING
import br.com.apps.repository.util.URL_IMAGE
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

class RequestWriteImpl(fireStore: FirebaseFirestore) : RequestWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    override suspend fun save(dto: RequestDto): String = withContext(Dispatchers.IO) {
        return@withContext if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private fun create(dto: RequestDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto)
        return document.id
    }

    private fun update(dto: RequestDto): String {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto)
        return id
    }

    override suspend fun delete(id: String) {
        withContext(Dispatchers.IO) {
            if (id.isEmpty()) throw EmptyIdException(EMPTY_ID_EXCEPTION)
            collection.document(id).delete()
        }
    }

    override suspend fun updateUrlImage(id: String, url: String?) {
        withContext(Dispatchers.IO) {
            if(id.isEmpty()) throw NullPointerException()

            val updates = mutableMapOf<String, Any?>(
                Pair(URL_IMAGE, url),
                Pair(IS_UPDATING, false)
            )

            collection.document(id).update(updates)
        }
    }

    override suspend fun setUpdatingStatus(id: String, isUpdating: Boolean) {
        withContext(Dispatchers.IO) {
            collection.document(id).update(IS_UPDATING, isUpdating)
        }
    }

}