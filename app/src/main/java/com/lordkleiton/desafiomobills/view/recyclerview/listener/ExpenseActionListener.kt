package com.lordkleiton.desafiomobills.view.recyclerview.listener

import com.lordkleiton.desafiomobills.model.Despesa

interface ExpenseActionListener {
    fun onEdit(current: Despesa, id: String) {}

    fun onDelete(id: String) {}
}