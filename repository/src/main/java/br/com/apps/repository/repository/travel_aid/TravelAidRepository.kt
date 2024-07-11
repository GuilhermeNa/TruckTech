package br.com.apps.repository.repository.travel_aid

class TravelAidRepository(private val read: TravelAidReadImpl) : TravelAidInterface {

    override suspend fun getTravelAidByDriverIdAndIsNotDiscountedYet(
        employeeId: String,
        flow: Boolean
    ) = read.getTravelAidByDriverIdAndIsNotDiscountedYet(employeeId, flow)

    override suspend fun getTravelAidListByTravelId(travelId: String, flow: Boolean) =
        read.getTravelAidListByTravelId(travelId, flow)

    override suspend fun getTravelAidListByTravelIds(travelIdList: List<String>, flow: Boolean) =
        read.getTravelAidListByTravelIds(travelIdList, flow)

    override suspend fun getTravelAidListByDriverId(driverId: String, flow: Boolean) =
        read.getTravelAidListByDriverId(driverId, flow)

}