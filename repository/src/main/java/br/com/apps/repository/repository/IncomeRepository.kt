package br.com.apps.repository.repository

import com.google.firebase.firestore.FirebaseFirestore

private const val FIRESTORE_COLLECTION_INCOMES = "incomes"
private const val MASTER_USER_ID = "uid"

class IncomeRepository(private val fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_INCOMES)



}