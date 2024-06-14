package br.com.apps.repository.repository.cost_help

class CostHelpRepository(private val read: CostHelpRead) : CostHelpI {

    override suspend fun getCostHelpByDriverIdAndIsNotDiscountedYet(
        employeeId: String,
        flow: Boolean
    ) =  read.getCostHelpByDriverIdAndIsNotDiscountedYet(employeeId, flow)

}