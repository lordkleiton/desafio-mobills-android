package com.lordkleiton.desafiomobills.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import com.lordkleiton.desafiomobills.model.Receita
import com.lordkleiton.desafiomobills.repository.IncomesRepository

class IncomesViewModel : ViewModel() {
    private val repository = IncomesRepository()
    private val incomes: MutableLiveData<List<Receita>> = MutableLiveData()

    fun find(): LiveData<List<Receita>> {
        repository.find().addOnSuccessListener { data ->
            val aux = data.documents.filterNotNull().map { it.toObject<Receita>()!! }

            incomes.value = aux
        }

        return incomes
    }

    fun save(data: Receita): LiveData<List<Receita>> {
        repository.save(data).addOnSuccessListener {
            val aux = (incomes.value ?: listOf()).toMutableList().apply { add(data) }

            incomes.value = aux.toList()
        }

        return incomes
    }
}