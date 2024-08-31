package br.com.apps.usecase.util

import br.com.apps.model.exceptions.EmptyIdException

fun buildRequestStoragePath(id: String): String {
    if (id.isEmpty()) throw EmptyIdException()
    return "requests/${id}.jpeg"
}

fun buildFreightStoragePath(id: String): String {
    if (id.isEmpty()) throw EmptyIdException()
    return "freights/${id}.jpeg"
}