package com.lordkleiton.desafiomobills.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.lordkleiton.desafiomobills.model.Despesa
import com.lordkleiton.desafiomobills.repository.ExpensesRepository
import com.lordkleiton.desafiomobills.util.AppConst.TAG

class ExpensesViewModel : ViewModel() {
    private val repository = ExpensesRepository()
    private val expenses: MutableLiveData<Map<String, Despesa>> = MutableLiveData()
    private val userId = Firebase.auth.currentUser!!.uid

    fun find(): LiveData<Map<String, Despesa>> {
        repository.find().addOnSuccessListener { data ->
            val safeList = data.documents.filterNotNull().map { it.id to it.toObject<Despesa>()!! }
            val aux = mutableMapOf<String, Despesa>()

            safeList.forEach {
                aux[it.first] = it.second
            }

            expenses.value = aux
        }

        return expenses
    }

    fun save(data: Despesa): LiveData<Map<String, Despesa>> {
        val newData = Despesa(data.valor, data.descricao, data.data, userId, data.pago)

        repository.save(newData).addOnSuccessListener {
            val aux = expenses.value?.toMutableMap() ?: mutableMapOf()

            aux[it.id] = newData

            expenses.value = aux
        }

        return expenses
    }

    fun delete(id: String): LiveData<Map<String, Despesa>> {
        repository.delete(id).addOnCompleteListener {
            val aux = expenses.value?.toMutableMap() ?: mutableMapOf()

            aux.remove(id)

            expenses.value = aux
        }

        return expenses
    }

    fun update(id: String, data: Despesa): LiveData<Map<String, Despesa>> {
        val newData = Despesa(data.valor, data.descricao, data.data, userId, data.pago)

        repository.update(id, newData).addOnSuccessListener {
            val aux = expenses.value?.toMutableMap() ?: mutableMapOf()

            aux[id] = newData

            expenses.value = aux
        }

        return expenses
    }
}