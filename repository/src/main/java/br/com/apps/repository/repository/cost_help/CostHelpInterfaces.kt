package br.com.apps.repository.repository.cost_help

import androidx.lifecycle.LiveData
import br.com.apps.model.model.payroll.TravelAid
import br.com.apps.repository.util.Response

interface CostHelpI : CostHelpReadI

interface CostHelpReadI {

    suspend fun getCostHelpByDriverIdAndIsNotDiscountedYet(
        employeeId: String,
        flow: Boolean = false
    ): LiveData<Response<List<TravelAid>>>

}