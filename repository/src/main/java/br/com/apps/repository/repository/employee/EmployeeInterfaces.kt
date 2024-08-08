package br.com.apps.repository.repository.employee

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.enums.WorkRole
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
     */
    suspend fun delete(id: String)

}

interface EmployeeReadInterface {

    /**
     * Retrieves an employee by their ID and type.
     *
     * @param id The ID of the employee to retrieve.
     * @param flow If true, indicates to use a continuous flow of data updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with the Employee object or an Error with an exception.
     */
    suspend fun fetchById(id: String, flow: Boolean = false)
            : LiveData<Response<Employee>>

    /**
     * Retrieves a list of employees associated with a master UID and its role.
     *
     * @param masterUid The master UID to identify which employees to retrieve.
     * @param role the work role of the employee.
     * @param flow If true, indicates to use a continuous flow of data updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Employee objects or an Error with an exception.
     */
    suspend fun fetchEmployeesByMasterUidAndRole(
        masterUid: String,
        role: WorkRole,
        flow: Boolean = false
    ): LiveData<Response<List<Employee>>>

}