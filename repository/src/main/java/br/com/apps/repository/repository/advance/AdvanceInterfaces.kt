package br.com.apps.repository.repository.advance

import androidx.lifecycle.LiveData
import br.com.apps.model.model.payroll.Advance
import br.com.apps.repository.util.Response

interface AdvanceRepositoryInterface : AdvanceReadInterface

interface AdvanceReadInterface {

    /**
     * Fetches the [Advance] dataSet for the specified employee ID.
     *
     * @param employeeId The ID of the [Employee].
     * @param isPaid if the [Advance] is already paid.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Advance] list.
     */
    suspend fun getAdvanceListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Advance>>>

    /**
     * Fetches the [Advance] dataSet for the specified employee ID list.
     *
     * @param employeeIdList The ID list of the [Employee]'s.
     * @param isPaid if the [Advance] is already paid.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Advance] list.
     */
    suspend fun getAdvanceListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Advance>>>

}
