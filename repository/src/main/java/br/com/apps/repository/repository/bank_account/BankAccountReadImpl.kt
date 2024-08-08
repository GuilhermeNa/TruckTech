package br.com.apps.repository.repository.bank_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EMPLOYEE_BANK_ACCOUNTS
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toBankAccountList
import br.com.apps.repository.util.toBankAccountObject
import br.com.apps.repository.util.validateId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BankAccountReadImpl(fireStore: FirebaseFirestore) : BankAccountReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EMPLOYEE_BANK_ACCOUNTS)

    override suspend fun fetchBankAccsByEmployeeId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<BankAccount>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toBankAccountList() }
            false -> listener.onComplete { it.toBankAccountList() }
        }
    }

    override suspend fun fetchBankAccById(
        id: String,
        flow: Boolean
    ): LiveData<Response<BankAccount>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toBankAccountObject() }
            false -> listener.onComplete { it.toBankAccountObject() }
        }
    }

}