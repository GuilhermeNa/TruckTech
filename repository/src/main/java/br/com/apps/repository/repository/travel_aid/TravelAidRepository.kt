package br.com.apps.repository.repository.travel_aid

class TravelAidRepository(private val read: TravelAidReadImpl) : TravelAidInterface {

    override suspend fun fetchTravelAidListByTravelId(id: String, flow: Boolean) =
        read.fetchTravelAidListByTravelId(id, flow)

    override suspend fun fetchTravelAidListByTravelIds(ids: List<String>, flow: Boolean) =
        read.fetchTravelAidListByTravelIds(ids, flow)

    override suspend fun fetchTravelAidListByDriverId(id: String, flow: Boolean) =
        read.fetchTravelAidListByDriverId(id, flow)

}