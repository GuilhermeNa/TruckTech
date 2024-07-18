package br.com.apps.model.dto

interface DtoInterface {

    /**
     * Checks the integrity of the data in the DTO. Ensures that essential fields for Model
     * objects are not nullable to maintain data integrity.
     *
     * @throws CorruptedFileException when the data is incomplete.
     */
    fun validateDataIntegrity()

    /**
     * Checks if the DTO is ready for insertion into a database.
     *
     * This method verifies if the DTO has all necessary fields populated and valid
     * to be safely inserted into a database.
     *
     * @throws InvalidForSavingException when the data is incomplete for save in dataBase.
     */
    fun validateForDataBaseInsertion()

}