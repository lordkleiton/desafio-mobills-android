package com.lordkleiton.desafiomobills.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import com.lordkleiton.desafiomobills.model.Receita
import com.lordkleiton.desafiomobills.repository.IncomesRepository

class IncomesViewModel : ViewModel() {
    private val repository = IncomesRepository()
    private val incomes: MutableLiveData<Map<String, Receita>> = MutableLiveData()

    fun find(): LiveData<Map<String, Receita>> {
        repository.find().addOnSuccessListener { data ->
            val safeList = data.documents.filterNotNull().map { it.id to it.toObject<Receita>()!! }
            val aux = mutableMapOf<String, Receita>()

            safeList.forEach {
                aux[it.first] = it.second
            }

            incomes.value = aux
        }

        return incomes
    }

    fun save(data: Receita): LiveData<Map<String, Receita>> {
        repository.save(data).addOnSuccessListener {
            val aux = incomes.value?.toMutableMap() ?: mutableMapOf()

            aux[it.id] = data

            incomes.value = aux
        }

        return incomes
    }
}