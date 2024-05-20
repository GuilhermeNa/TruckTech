package br.com.apps.repository.repository.loan

import androidx.lifecycle.LiveData
import br.com.apps.model.model.payroll.Loan
import br.com.apps.repository.util.Response

interface LoanRepositoryI: LoanWriteI, LoanReadI

interface LoanWriteI

interface LoanReadI {

    suspend fun getLoanListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Loan>>>

    suspend fun getLoanListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Loan>>>

}

