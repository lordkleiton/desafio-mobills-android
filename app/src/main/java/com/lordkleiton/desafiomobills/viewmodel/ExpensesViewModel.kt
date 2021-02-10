package com.lordkleiton.desafiomobills.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import com.lordkleiton.desafiomobills.model.Despesa
import com.lordkleiton.desafiomobills.repository.ExpensesRepository

class ExpensesViewModel : ViewModel() {
    private val repository = ExpensesRepository()
    private val expenses: MutableLiveData<List<Despesa>> = MutableLiveData()

    fun find(): LiveData<List<Despesa>> {
        repository.find().addOnSuccessListener { data ->
            val aux = data.documents.filterNotNull().map { it.toObject<Despesa>()!! }

            expenses.value = aux
        }

        return expenses
    }

    fun save(data: Despesa): LiveData<List<Despesa>> {
        repository.save(data).addOnSuccessListener {
            val aux = (expenses.value ?: listOf()).toMutableList().apply { add(data) }

            expenses.value = aux.toList()
        }

        return expenses
    }
}