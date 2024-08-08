package br.com.apps.repository.repository.bank_account

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.bank.BankAccountDto
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.repository.util.Response

interface BankAccountInterface : BankAccountWriteInterface, BankAccountReadInterface

interface BankAccountWriteInterface {

    /**
     * Deletes a bank account associated with an employee.
     *
     * @param id The ID of the bank account to delete.
     * @param type The type of employee.
     */
    suspend fun delete(id: String)

    /**
     * Saves a bank account for an employee.
     *
     * @param dto The DTO containing bank account information.
     * @param type The type of employee.
     */
    suspend fun save(dto: BankAccountDto)

    /**
     * Updates the main account ID associated with an employee.
     *
     * @param oldMainAccId The ID of the current main account to be replaced (nullable).
     * @param newMainAccId The ID of the new main account.
     */
    suspend fun updateMainAccount(oldMainAccId: String?, newMainAccId: String)

}

interface BankAccountReadInterface {

    /**
     * Retrieves bank accounts associated with an employee based on employee ID and type.
     *
     * @param id The ID of the employee whose bank accounts to retrieve.
     * @param type The type of employee (e.g., regular, manager, etc.).
     * @param flow If true, indicates to use a continuous flow of data updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of BankAccount objects or an Error with an exception.
     */
    suspend fun fetchBankAccsByEmployeeId(id: String, flow: Boolean = false)
            : LiveData<Response<List<BankAccount>>>

    /**
     * Retrieves a specific bank account associated with an employee based on employee ID, bank ID, and type.
     *
     * @param id The ID of the bank account to retrieve.
     * @param type The type of employee (e.g., regular, manager, etc.).
     * @param flow If true, indicates to use a continuous flow of data updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with the BankAccount object or an Error with an exception.
     */
    suspend fun fetchBankAccById(id: String, flow: Boolean = false)
            : LiveData<Response<BankAccount>>

}