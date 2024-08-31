package br.com.apps.repository.repository

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class StorageRepository(private val fireStorage: FirebaseStorage) {

    /**
     * Posts an image to Firebase Storage at the specified path.
     *
     * @param image The byte array of the image data to upload.
     * @param path The path in Firebase Storage where the image will be stored.
     * @return String The URL of the uploaded image.
     */
    suspend fun postImage(image: ByteArray, path: String): String {
        val reference = fireStorage.reference.child(path)
        reference.putBytes(image).await()
        val url = reference.downloadUrl.await()
        return url.toString()
    }

    /**
     * Deletes an image from Firebase Storage at the specified path.
     *
     * @param path The path in Firebase Storage of the image to delete.
     */
    suspend fun deleteImage(path: String) {
        val reference = fireStorage.reference.child(path)
        reference.delete().await()
    }

    suspend fun deleteImages(paths: List<String>) {
        paths.forEach {
            val reference = fireStorage.reference.child(it)
            reference.delete().await()
        }
    }

}