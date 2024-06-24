package br.com.apps.repository.repository.cost_help

class TravelAidRepository(private val read: TravelAidRead) : TravelAidI {

    override suspend fun getCostHelpByDriverIdAndIsNotDiscountedYet(
        employeeId: String,
        flow: Boolean
    ) = read.getCostHelpByDriverIdAndIsNotDiscountedYet(employeeId, flow)

    override suspend fun getTravelAidListByTravelId(travelId: String, flow: Boolean) =
        read.getTravelAidListByTravelId(travelId, flow)

}