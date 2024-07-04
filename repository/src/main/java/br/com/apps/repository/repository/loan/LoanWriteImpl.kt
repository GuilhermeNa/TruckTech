package br.com.apps.repository.repository.loan

import br.com.apps.repository.util.FIRESTORE_COLLECTION_LOANS
import com.google.firebase.firestore.FirebaseFirestore

class LoanWriteImpl(fireStore: FirebaseFirestore): LoanWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_LOANS)


}