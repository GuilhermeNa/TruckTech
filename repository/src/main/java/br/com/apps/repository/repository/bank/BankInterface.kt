package br.com.apps.repository.repository.bank

import androidx.lifecycle.LiveData
import br.com.apps.model.model.bank.Bank
import br.com.apps.repository.util.Response

interface BankInterface {

    /**
     * Fetch a list of banks registered in the system.
     *
     * @return A LiveData object containing the result of the operation.
     */
    suspend fun fetchBankList(): LiveData<Response<List<Bank>>>

}