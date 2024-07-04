package br.com.apps.repository.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

/**
 * This extension function convert the resulting QuerySnapshot into type T, and returning it
 * wrapped in a LiveData that emits a Response. It waits for the operation to complete using
 * coroutine await() and handles potential exceptions.
 *
 * @param convert function that converts a QuerySnapshot to type T.
 *
 * @return LiveData that emits a Response containing either the converted data (Success) or an error (Error).
 *
 */
suspend fun <T> Query.onComplete(convert: (QuerySnapshot) -> T): LiveData<Response<T>> {
    val liveData = MutableLiveData<Response<T>>()

    this.get().addOnCompleteListener { task ->
        task.exception?.let { e -> liveData.postValue(Response.Error(e)) }

        task.result?.let { query ->
            val response = try {
                Response.Success(convert(query))
            } catch (e: Exception) {
                Response.Error(e)
            }

            liveData.postValue(response)
        }

    }.await()

    return liveData
}

/**
 * This extension function convert the resulting DocumentSnapshot into type T, and returning it
 * wrapped in a LiveData that emits a Response. It waits for the operation to complete using
 * coroutine await() and handles potential exceptions.
 *
 * @param convert Lambda function that converts a DocumentSnapshot to type T.
 *
 * @return LiveData that emits a Response containing either the converted data (Success) or an error (Error).
 *
 */
suspend fun <T> DocumentReference.onComplete(convert: (DocumentSnapshot) -> T): LiveData<Response<T>> {
    val liveData = MutableLiveData<Response<T>>()

    this.get().addOnCompleteListener { task ->
        task.exception?.let { e -> liveData.postValue(Response.Error(e)) }

        task.result?.let { doc ->
            val response = try {
                Response.Success(convert(doc))
            } catch (e: Exception) {
                Response.Error(e)
            }

            liveData.postValue(response)
        }

    }.await()

    return liveData
}

/**
 * This extension function listens for snapshot changes on a Firebase Firestore Query,
 * converting the resulting QuerySnapshot into type T, and continuously updating a LiveData
 * that emits a Response. It handles potential errors and updates the LiveData with new data upon changes.
 *
 * @param convert Lambda function that converts a QuerySnapshot to type T.
 *
 * @return LiveData that emits a Response containing either the converted data (Success) or an error (Error).
 *
 */
fun <T> Query.onSnapShot(convert: (QuerySnapshot) -> T): LiveData<Response<T>> {
    val liveData = MutableLiveData<Response<T>>()

    this.addSnapshotListener { nQuery, error ->
        error?.let { e -> liveData.postValue(Response.Error(e)) }

        nQuery?.let { query ->
            val response = try {
                Response.Success(convert(query))
            } catch (e: Exception) {
                Response.Error(e)
            }

            liveData.postValue(response)
        }
    }

    return liveData
}

/**
 * This extension function listens for snapshot changes on a Firebase Firestore DocumentReference,
 * converting the resulting DocumentSnapshot into type T, and continuously updating a LiveData that
 * emits a Response. It handles potential errors and updates the LiveData with new data upon changes.
 *
 * @param convert Lambda function that converts a DocumentSnapshot to type T.
 *
 * @return LiveData that emits a Response containing either the converted data (Success) or an error (Error).
 *
 */
fun <T> DocumentReference.onSnapShot(convert: (DocumentSnapshot) -> T): LiveData<Response<T>> {
    val liveData = MutableLiveData<Response<T>>()

    this.addSnapshotListener { nDoc, error ->
        error?.let { e -> liveData.postValue(Response.Error(e)) }

        nDoc?.let { doc ->
            val response = try {
                Response.Success(convert(doc))
            } catch (e: Exception) {
                Response.Error(e)
            }

            liveData.postValue(response)
        }
    }

    return liveData
}