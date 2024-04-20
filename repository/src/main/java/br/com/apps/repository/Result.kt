package br.com.apps.repository

sealed class Response<out T> {
    data class Success<T>(val data: T? = null): Response<T>()
    data class Error(val exception: Exception): Response<Nothing>()
}