package br.com.apps.repository.repository.loan

import androidx.lifecycle.LiveData
import br.com.apps.model.model.payroll.Loan
import br.com.apps.repository.util.Response

interface LoanRepositoryInterface : LoanWriteInterface, LoanReadInterface

interface LoanWriteInterface

interface LoanReadInterface {

    /**
     * Retrieves a list of [Loan]'s associated with an employee ID and payment status.
     *
     * @param id The ID of the employee to retrieve loans for.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Loan objects or an Error with an exception.
     */
    suspend fun fetchLoanListByEmployeeId(id: String, flow: Boolean = false)
            : LiveData<Response<List<Loan>>>

    /**
     * Retrieves a list of [Loan]'s associated with multiple employee IDs and payment status.
     *
     * @param ids The list of employee IDs to retrieve loans for.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Loan objects or an Error with an exception.
     */
    suspend fun fetchLoanListByEmployeeIds(ids: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Loan>>>

    /**
     * Retrieves a [Loan] by id.
     *
     * @param id The id of the [Loan].
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a Loan object or an Error with an exception.
     */
    suspend fun fetchLoanById(id: String, flow: Boolean = false)
            : LiveData<Response<Loan>>

    suspend fun fetchLoanByIds(ids: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Loan>>>

}

