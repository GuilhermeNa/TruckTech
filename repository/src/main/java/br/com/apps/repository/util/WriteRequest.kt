package br.com.apps.repository.util

import br.com.apps.model.model.user.PermissionLevelType

/**
 * Represents a request to write data of type T, along with the required permission level.
 *
 * @property authLevel The permission level required to perform the write operation.
 * @property data The data of type T to be written.
 */
data class WriteRequest<T>(
    val authLevel: PermissionLevelType? = null,
    val data: T
)

/**
 * Validates and processes a write request, executing permission and validating the data.
 * @param validatePermission Function to check and execute permission level requirements.
 * @param validateData Function to validate the request data or conditions.
 *
 * @return [Response.Success] if the validation and permission checks pass successfully.
 *
 * [Response.Error] if an exception occurs during permission check or validation.
 *
 */
fun validateAndProcess(
    validatePermission: () -> Unit = {},
    validateData: () -> Unit = {}
): Response<Unit> {
    return try {
        validatePermission()
        validateData()
        Response.Success()
    } catch (e: Exception) {
        Response.Error(e)
    }

}