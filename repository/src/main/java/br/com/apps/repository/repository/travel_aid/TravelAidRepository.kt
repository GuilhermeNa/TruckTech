package br.com.apps.repository.repository.travel_aid

class TravelAidRepository(private val read: TravelAidReadImpl) : TravelAidInterface {

    override suspend fun fetchTravelAidByDriverIdAndIsNotDiscountedYet(
        employeeId: String,
        flow: Boolean
    ) = read.fetchTravelAidByDriverIdAndIsNotDiscountedYet(employeeId, flow)

    override suspend fun fetchTravelAidListByTravelId(travelId: String, flow: Boolean) =
        read.fetchTravelAidListByTravelId(travelId, flow)

    override suspend fun fetchTravelAidListByTravelIds(travelIdList: List<String>, flow: Boolean) =
        read.fetchTravelAidListByTravelIds(travelIdList, flow)

    override suspend fun fetchTravelAidListByDriverId(driverId: String, flow: Boolean) =
        read.fetchTravelAidListByDriverId(driverId, flow)

}