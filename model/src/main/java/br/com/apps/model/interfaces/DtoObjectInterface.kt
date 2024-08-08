package br.com.apps.model.interfaces

/**
 * This interface defines methods to ensure that DTOs (Data Transfer Objects) meet the necessary criteria
 * for consistency and interaction with a Firebase database.
 * @param T The type of the Model that the Model object will be converted to.
 */
interface DtoObjectInterface<T> {

    /**
     * Checks the integrity of the data in the DTO. Ensures that essential fields for Model
     * objects are not nullable to maintain data integrity.
     *
     * @throws CorruptedFileException when the data is corrupted.
     */
    fun validateDataIntegrity()

    /**
     * Checks if the DTO is ready for insertion into a database.
     *
     * This method verifies if the DTO has all necessary fields populated and valid
     * to be safely inserted into a database.
     *
     * @throws InvalidForSavingException when the dto is incomplete to be saved in dataBase.
     */
    fun validateDataForDbInsertion()

    /**
     * Converts the DTO to a Model object.
     *
     * @return The Model object that represents the data contained in the DTO.
     */
    fun toModel(): T

}