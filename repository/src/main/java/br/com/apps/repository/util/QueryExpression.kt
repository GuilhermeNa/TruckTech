package br.com.apps.repository.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

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