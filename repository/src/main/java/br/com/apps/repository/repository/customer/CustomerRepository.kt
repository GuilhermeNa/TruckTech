package br.com.apps.repository.repository.customer

class CustomerRepository(
    private val read: CustomerReadImpl
) : CustomerRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchCustomerListByMasterUid(uid: String, flow: Boolean) =
        read.fetchCustomerListByMasterUid(uid, flow)

    override suspend fun fetchCustomerById(id: String, flow: Boolean) =
        read.fetchCustomerById(id, flow)

}