package br.com.apps.repository.repository.transaction

class TransactionRepository(private val read: TransactionReadImpl) : TransactionInterface {

    override suspend fun fetchTransactionsByParentId(id: String, flow: Boolean) =
        read.fetchTransactionsByParentId(id, flow)

    override suspend fun fetchTransactionById(id: String, flow: Boolean) =
        read.fetchTransactionById(id, flow)

    override suspend fun fetchTransactionsByParentIds(ids: List<String>, flow: Boolean) =
        read.fetchTransactionsByParentIds(ids, flow)

}