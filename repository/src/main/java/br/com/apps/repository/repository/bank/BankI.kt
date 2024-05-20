package br.com.apps.repository.repository.bank

import androidx.lifecycle.LiveData
import br.com.apps.model.model.bank.Bank
import br.com.apps.repository.util.Response

interface BankI {

    suspend fun getBankList(): LiveData<Response<List<Bank>>>

}