package com.lordkleiton.desafiomobills.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lordkleiton.desafiomobills.model.Despesa
import com.lordkleiton.desafiomobills.util.AppConst.COLLECTION_EXPENSES
import com.lordkleiton.desafiomobills.util.AppConst.FIELD_USER_ID

class ExpensesRepository {
    private val userId = Firebase.auth.currentUser!!.uid
    private val store = Firebase.firestore
    private val collection = store.collection(COLLECTION_EXPENSES)

    fun save(data: Despesa) = collection.add(data)

    fun find() = collection.whereEqualTo(FIELD_USER_ID, userId).get()

    fun delete(id: String) = collection.document(id).delete()

    fun update(id: String, data: Despesa) = collection.document(id).set(data)
}