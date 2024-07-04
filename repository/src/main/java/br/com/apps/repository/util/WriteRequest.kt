package br.com.apps.repository.util

import br.com.apps.model.model.user.PermissionLevelType

data class WriteRequest<T>(
    val authLevel: PermissionLevelType?,
    val data: T
)

fun validateAndProcess(
    permission: () -> Unit = {},
    validator: () -> Unit = {}
): Response<Unit> {
    return try {
        permission()
        validator()
        Response.Success()
    } catch (e: Exception) {
        Response.Error(e)
    }

}