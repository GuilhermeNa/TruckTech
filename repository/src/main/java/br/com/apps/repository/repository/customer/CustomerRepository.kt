package br.com.apps.repository.repository.customer

class CustomerRepository(
    private val read: CustomerReadImpl
) : CustomerRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getCustomerListByMasterUid(masterUid: String, flow: Boolean) =
        read.getCustomerListByMasterUid(masterUid, flow)

    override suspend fun getCustomerById(id: String, flow: Boolean) =
        read.getCustomerById(id, flow)

}