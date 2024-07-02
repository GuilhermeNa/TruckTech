package br.com.apps.repository.repository.travel_aid

class TravelAidRepository(private val read: TravelAidRead) : TravelAidI {

    override suspend fun getCostHelpByDriverIdAndIsNotDiscountedYet(
        employeeId: String,
        flow: Boolean
    ) = read.getCostHelpByDriverIdAndIsNotDiscountedYet(employeeId, flow)

    override suspend fun getTravelAidListByTravelId(travelId: String, flow: Boolean) =
        read.getTravelAidListByTravelId(travelId, flow)

    override suspend fun getTravelAidListByTravelIds(travelIdList: List<String>, flow: Boolean) =
        read.getTravelAidListByTravelIds(travelIdList, flow)

}