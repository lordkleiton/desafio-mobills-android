package com.lordkleiton.desafiomobills.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lordkleiton.desafiomobills.model.Receita
import com.lordkleiton.desafiomobills.util.AppConst.COLLECTION_INCOMES

class IncomesRepository {
    private val store = Firebase.firestore
    private val collection = store.collection(COLLECTION_INCOMES)

    fun save(data: Receita) = collection.add(data)

    fun find() = collection.get()

    fun delete(id: String) = collection.document(id).delete()

    fun update(id: String, updated: Receita) = collection.document(id).set(updated)
}