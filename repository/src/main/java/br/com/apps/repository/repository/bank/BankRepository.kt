package br.com.apps.repository.repository.bank

import androidx.lifecycle.LiveData
import br.com.apps.model.model.bank.Bank
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DEFAULT_BANKS
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.toBankList
import com.google.firebase.firestore.FirebaseFirestore

class BankRepository(fireStore: FirebaseFirestore): BankInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_DEFAULT_BANKS)

    override suspend fun getBankList(): LiveData<Response<List<Bank>>> {
        val listener = collection
        return listener.onComplete { it.toBankList() }
    }

}