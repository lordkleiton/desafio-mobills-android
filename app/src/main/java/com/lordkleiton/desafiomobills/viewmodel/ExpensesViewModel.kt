package com.lordkleiton.desafiomobills.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import com.lordkleiton.desafiomobills.model.Despesa
import com.lordkleiton.desafiomobills.repository.ExpensesRepository

class ExpensesViewModel : ViewModel() {
    private val repository = ExpensesRepository()
    private val expenses: MutableLiveData<Map<String, Despesa>> = MutableLiveData()

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
        repository.save(data).addOnSuccessListener {
            val aux = expenses.value?.toMutableMap() ?: mutableMapOf()

            aux[it.id] = data

            expenses.value = aux
        }

        return expenses
    }
}