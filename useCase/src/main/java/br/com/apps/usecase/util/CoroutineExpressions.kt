package br.com.apps.usecase.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import br.com.apps.repository.util.Response
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

suspend fun <T> LiveData<Response<T>>.awaitData(): T = suspendCancellableCoroutine { cont ->
    val observer = object : Observer<Response<T>> {
        override fun onChanged(value: Response<T>) {
            when (value) {
                is Response.Error -> cont.resumeWithException(value.exception)
                is Response.Success -> value.data?.let { cont.resume(it) }
                    ?: cont.resumeWithException(NullPointerException())
            }

            this@awaitData.removeObserver(this)
        }
    }
    cont.invokeOnCancellation { this.removeObserver(observer) }
    this.observeForever(observer)
}

suspend fun <T> LiveData<Response<T>>.observeFlow(onComplete: (T) -> Unit): T =
    suspendCancellableCoroutine { cont ->
        val observer = object : Observer<Response<T>> {
            override fun onChanged(value: Response<T>) {
                when (value) {
                    is Response.Error -> {
                        cont.resumeWithException(value.exception)
                        this@observeFlow.removeObserver(this)
                    }

                    is Response.Success -> {
                        value.data?.let { onComplete(it) }
                        if(value.data == null) {
                            cont.resumeWithException(NullPointerException())
                            this@observeFlow.removeObserver(this)
                        }
                    }
                }
            }
        }
        this.observeForever(observer)
        cont.invokeOnCancellation {
            this.removeObserver(observer)
        }
    }
