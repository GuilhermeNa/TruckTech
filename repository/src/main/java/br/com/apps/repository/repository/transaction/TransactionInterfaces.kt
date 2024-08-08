package br.com.apps.repository.repository.transaction

import androidx.lifecycle.LiveData
import br.com.apps.model.model.finance.FinancialRecord
import br.com.apps.model.model.finance.Transaction
import br.com.apps.repository.util.Response

interface TransactionInterface : TransactionReadInterface

interface TransactionReadInterface {

    /**
     * Fetches the [Transaction] dataSet for the specified [FinancialRecord] ID.
     *
     * @param id The ID of the [FinancialRecord].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Transaction] list.
     */
    suspend fun fetchTransactionsByParentId(id: String, flow: Boolean = false)
            : LiveData<Response<List<Transaction>>>

    /**
     * Fetches the [Transaction] by ID.
     *
     * @param id The ID of the [Transaction].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Transaction].
     */
    suspend fun fetchTransactionById(id: String, flow: Boolean = false)
            : LiveData<Response<Transaction>>

    suspend fun fetchTransactionsByParentIds(ids: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Transaction>>>

}

