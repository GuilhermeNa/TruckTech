package br.com.apps.repository.repository.employee

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.enums.WorkRole
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.Employee
import br.com.apps.repository.util.Response

interface EmployeeRepositoryInterface : EmployeeWriteInterface, EmployeeReadInterface

interface EmployeeWriteInterface {

    /**
     * Saves employee data provided in the DTO (Data Transfer Object).
     *
     * @param dto The DTO containing the employee data to be saved.
     * @return A string representing the ID of the saved employee.
     */
    suspend fun save(dto: EmployeeDto): String

    /**
     * Deletes an employee record based on the specified ID and type.
     *
     * @param id The ID of the employee to delete.
     * @param type The type of employee (e.g., regular, manager, etc.).
     */
    suspend fun delete(id: String, type: WorkRole)

    /**
     * Deletes a bank account associated with an employee based on employee ID, bank ID, and employee type.
     *
     * @param employeeId The ID of the employee whose bank account will be deleted.
     * @param bankId The ID of the bank account to delete.
     * @param type The type of employee (e.g., regular, manager, etc.).
     */
    suspend fun deleteBankAcc(employeeId: String, bankId: String, type: WorkRole)

    /**
     * Saves a bank account for an employee.
     *
     * @param bankAccDto The DTO containing bank account information.
     * @param type The type of employee (e.g., regular, manager, etc.).
     */
    suspend fun saveBankAccount(bankAccDto: BankAccountDto, type: WorkRole)

    /**
     * Updates the main account ID associated with an employee.
     *
     * @param employeeId The ID of the employee whose main account ID will be updated.
     * @param oldMainAccId The ID of the current main account to be replaced (nullable).
     * @param newMainAccId The ID of the new main account.
     * @param type The type of employee (e.g., regular, manager, etc.).
     */
    suspend fun updateMainAccount(
        employeeId: String,
        oldMainAccId: String?,
        newMainAccId: String,
        type: WorkRole
    )



}

interface EmployeeReadInterface {

    /**
     * Retrieves a list of employees associated with a master UID.
     *
     * @param masterUid The master UID to identify which employees to retrieve.
     * @param flow If true, indicates to use a continuous flow of data updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Employee objects or an Error with an exception.
     */
    suspend fun fetchEmployeeListByMasterUid(masterUid: String, flow: Boolean = false)
    : LiveData<Response<List<Employee>>>

    /**
     * Retrieves an employee by their ID and type.
     *
     * @param id The ID of the employee to retrieve.
     * @param type The type of employee (e.g., regular, manager, etc.).
     * @param flow If true, indicates to use a continuous flow of data updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with the Employee object or an Error with an exception.
     */
    suspend fun fetchById(id: String, type: WorkRole, flow: Boolean = false)
    : LiveData<Response<Employee>>

    /**
     * Retrieves bank accounts associated with an employee based on employee ID and type.
     *
     * @param id The ID of the employee whose bank accounts to retrieve.
     * @param type The type of employee (e.g., regular, manager, etc.).
     * @param flow If true, indicates to use a continuous flow of data updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of BankAccount objects or an Error with an exception.
     */
    suspend fun getEmployeeBankAccounts(id: String, type: WorkRole, flow: Boolean = false)
            : LiveData<Response<List<BankAccount>>>

    /**
     * Retrieves a specific bank account associated with an employee based on employee ID, bank ID, and type.
     *
     * @param employeeId The ID of the employee.
     * @param bankId The ID of the bank account to retrieve.
     * @param type The type of employee (e.g., regular, manager, etc.).
     * @param flow If true, indicates to use a continuous flow of data updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with the BankAccount object or an Error with an exception.
     */
    suspend fun getBankAccountById(
        employeeId: String,
        bankId: String,
        type: WorkRole,
        flow: Boolean = false
    ): LiveData<Response<BankAccount>>

}