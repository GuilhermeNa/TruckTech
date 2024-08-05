package br.com.apps.model.interfaces

/**
 * It establishes a contract for various model classes within the system.
 *
 * @param T The type of the DTO that the Model object will be converted to.
 */
interface ModelObjectInterface<T> {

    /**
     * Converts the Model object to a DTO.
     *
     * This method should be implemented to create a Data Transfer Object that represents
     * the Model object. The resulting DTO is used to communicate with FireBase.
     *
     * @return The Data Transfer Object (DTO) representing the Model object.
     */
    fun toDto(): T

}