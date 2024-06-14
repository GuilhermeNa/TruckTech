package br.com.apps.usecase.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun <T> LiveData<T>.awaitValue(): T = suspendCancellableCoroutine { cont ->
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            cont.resume(value)
            this@awaitValue.removeObserver(this)
        }
    }
    cont.invokeOnCancellation { this.removeObserver(observer) }
    this.observeForever(observer)
}