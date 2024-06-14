package br.com.apps.repository.util

data class ValidationResult<T>(
    val isValid: Boolean,
    val data: T? = null
)