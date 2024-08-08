package br.com.apps.repository.repository.advance

import androidx.lifecycle.LiveData
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.payroll.Advance
import br.com.apps.repository.util.Response

interface AdvanceRepositoryInterface : AdvanceReadInterface

interface AdvanceReadInterface {

    /**
     * Fetches the [Advance] dataSet for the specified employee ID.
     *
     * @param id The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Advance] list.
     */
    suspend fun fetchAdvanceListByEmployeeId(id: String, flow: Boolean = false)
            : LiveData<Response<List<Advance>>>

    /**
     * Fetches the [Advance] dataSet for the specified employee ID list.
     *
     * @param ids The ID list of the [Employee]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Advance] list.
     */
    suspend fun fetchAdvanceListByEmployeeIds(ids: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Advance>>>

    /**
     * Fetches the [Advance] dataSet for the specified employee ID list.
     *
     * @param ids The ID list of the [Advance].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Advance].
     */
    suspend fun fetchAdvanceById(id: String, flow: Boolean = false)
            : LiveData<Response<Advance>>

    suspend fun fetchAdvanceByIds(ids: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Advance>>>

}
