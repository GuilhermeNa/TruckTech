package br.com.apps.model.dto

/**
 * This interface defines methods to ensure that DTOs meet the necessary criteria
 * for consistency and database operations.
 */
interface DtoObjectsInterface {

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
    fun validateForDataBaseInsertion()

}