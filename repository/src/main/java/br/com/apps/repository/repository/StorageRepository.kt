package br.com.apps.repository.repository

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class StorageRepository(private val fireStorage: FirebaseStorage) {

    suspend fun postImage(image: ByteArray, path: String) {
        val reference = fireStorage.reference.child(path)
        reference.putBytes(image).await()
    }

}