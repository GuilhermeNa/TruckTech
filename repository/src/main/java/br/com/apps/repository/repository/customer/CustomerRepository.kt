package br.com.apps.repository.repository.customer

class CustomerRepository(
    private val read: CustomerRead
) : CustomerRepositoryI {

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getCustomerListByMasterUid(masterUid: String, flow: Boolean) =
        read.getCustomerListByMasterUid(masterUid, flow)

}