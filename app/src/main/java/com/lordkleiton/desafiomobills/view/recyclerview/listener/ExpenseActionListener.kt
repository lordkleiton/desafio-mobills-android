package com.lordkleiton.desafiomobills.view.recyclerview.listener

import com.lordkleiton.desafiomobills.model.Despesa

interface ExpenseActionListener {
    fun onEdit(current: Pair<String, Despesa>)

    fun onDelete(id: String)
}