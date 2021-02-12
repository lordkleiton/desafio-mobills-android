package com.lordkleiton.desafiomobills.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.lordkleiton.desafiomobills.model.Receita
import com.lordkleiton.desafiomobills.repository.IncomesRepository

class IncomesViewModel : ViewModel() {
    private val repository = IncomesRepository()
    private val incomes: MutableLiveData<Map<String, Receita>> = MutableLiveData()
    private val userId = Firebase.auth.currentUser!!.uid

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
        val newData = Receita(data.valor, data.descricao, data.data, userId, data.recebido)

        repository.save(newData).addOnSuccessListener {
            val aux = incomes.value?.toMutableMap() ?: mutableMapOf()

            aux[it.id] = newData

            incomes.value = aux
        }

        return incomes
    }

    fun delete(id: String): LiveData<Map<String, Receita>> {
        repository.delete(id).addOnCompleteListener {
            val aux = incomes.value?.toMutableMap() ?: mutableMapOf()

            aux.remove(id)

            incomes.value = aux
        }

        return incomes
    }

    fun update(id: String, data: Receita): LiveData<Map<String, Receita>> {
        val newData = Receita(data.valor, data.descricao, data.data, userId, data.recebido)

        repository.update(id, newData).addOnSuccessListener {
            val aux = incomes.value?.toMutableMap() ?: mutableMapOf()

            aux[id] = newData

            incomes.value = aux
        }

        return incomes
    }
}