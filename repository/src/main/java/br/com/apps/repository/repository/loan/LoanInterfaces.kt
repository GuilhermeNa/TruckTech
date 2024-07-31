package br.com.apps.repository.repository.loan

import androidx.lifecycle.LiveData
import br.com.apps.model.model.payroll.Loan
import br.com.apps.repository.util.Response

interface LoanRepositoryInterface: LoanWriteInterface, LoanReadInterface

interface LoanWriteInterface

interface LoanReadInterface {

    /**
     * Retrieves a list of [Loan]'s associated with an employee ID and payment status.
     *
     * @param employeeId The ID of the employee to retrieve loans for.
     * @param isPaid If true, filters loans that are paid; if false, filters loans that are unpaid.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Loan objects or an Error with an exception.
     */
    suspend fun fetchLoanListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Loan>>>

    /**
     * Retrieves a list of [Loan]'s associated with multiple employee IDs and payment status.
     *
     * @param employeeIdList The list of employee IDs to retrieve loans for.
     * @param isPaid If true, filters loans that are paid; if false, filters loans that are unpaid.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Loan objects or an Error with an exception.
     */
    suspend fun fetchLoanListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Loan>>>

}

