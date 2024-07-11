package br.com.apps.repository.util

/**
 * Sealed class representing a response from an asynchronous operation.
 * Allows encapsulating either a successful result with data of type [T],
 * or an error with an [Exception].
 */
sealed class Response<out T> {

    /**
     * Represents a successful response with optional data of type [T].
     *
     * @property data Optional data of type [T] representing the result of a successful operation.
     */
    data class Success<T>(val data: T? = null): Response<T>()

    /**
     * Represents an error response encapsulating an [Exception].
     *
     * @property exception The exception object that caused the error.
     */
    data class Error(val exception: Exception): Response<Nothing>()

}

fun <T> Response<T>.extractResponse(): T{
    when(this) {
        is Response.Error -> throw this.exception
        is Response.Success -> return this.data ?: throw NullPointerException()
    }
}