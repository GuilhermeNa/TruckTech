package br.com.apps.model.interfaces

import br.com.apps.model.model.user.AccessLevel

/**
 * Interface for managing and validating access permissions.
 * Provides methods to validate read and write access for sensitive data or resources.
 */
interface AccessPermissionInterface {

    /**
     * Validates whether the current context has write access permissions.
     *
     * @param access The level of access required for the write operation.
     *               This parameter can be null, which may indicate that the write access level is not specified.
     * @throws AccessDeniedException if the write access is not granted based on the provided access level.
     */
    fun validateWriteAccess(access: AccessLevel?)

    /**
     * Validates whether the current context has read access permissions.
     *
     * @throws AccessDeniedException if the read access is not granted.
     */
    fun validateReadAccess()

}