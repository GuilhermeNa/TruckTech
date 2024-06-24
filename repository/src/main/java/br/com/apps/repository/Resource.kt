package br.com.apps.repository

import br.com.apps.repository.util.Response

class Resource<T>(
    val data: T,
    val error: String? = null
)

fun <T> Response<T>.extractData(): T {
    return when(this) {
        is Response.Error -> throw this.exception
        is Response.Success -> this.data ?: throw NullPointerException("The data is null data")
    }
}