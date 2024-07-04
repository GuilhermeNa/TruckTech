package br.com.apps.repository.repository.income

import br.com.apps.repository.util.FIRESTORE_COLLECTION_INCOMES
import com.google.firebase.firestore.FirebaseFirestore

class IncomeRepository(private val fireStore: FirebaseFirestore): IncomeRepositoryInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_INCOMES)




}