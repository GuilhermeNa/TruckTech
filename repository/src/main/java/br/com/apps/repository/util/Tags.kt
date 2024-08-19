package br.com.apps.repository.util

import br.com.apps.model.exceptions.invalid.InvalidIdException

const val TAG_DEBUG = "debug"

fun String.validateId(): Response.Error? {
    return if (this.isBlank()) Response.Error(InvalidIdException(BLANK_ID_EXCEPTION))
    else null
}

fun List<String>.validateIds(): Response.Error? {
    return if (this.isEmpty()) Response.Error(InvalidIdException(EMPTY_ID_EXCEPTION))
    else null
}