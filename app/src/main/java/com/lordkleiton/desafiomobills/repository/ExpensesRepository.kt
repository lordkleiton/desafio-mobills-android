package com.lordkleiton.desafiomobills.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lordkleiton.desafiomobills.model.Despesa
import com.lordkleiton.desafiomobills.util.AppConst.COLLECTION_EXPENSES

class ExpensesRepository {
    private val store = Firebase.firestore
    private val collection = store.collection(COLLECTION_EXPENSES)

    fun save(data: Despesa) = collection.add(data)

    fun find() = collection.get()

    fun delete(id: String) = collection.document(id).delete()

    fun update(id: String, data: Despesa) = collection.document(id).set(data)
}