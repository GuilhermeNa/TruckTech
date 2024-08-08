package br.com.apps.repository.repository.advance

class AdvanceRepository(private val read: AdvanceReadImpl) : AdvanceRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchAdvanceListByEmployeeId(id: String, flow: Boolean) =
        read.fetchAdvanceListByEmployeeId(id, flow)

    override suspend fun fetchAdvanceListByEmployeeIds(ids: List<String>, flow: Boolean) =
        read.fetchAdvanceListByEmployeeIds(ids, flow)

    override suspend fun fetchAdvanceById(id: String, flow: Boolean) =
        read.fetchAdvanceById(id, flow)

    override suspend fun fetchAdvanceByIds(ids: List<String>, flow: Boolean) =
        read.fetchAdvanceByIds(ids, flow)

}

