package com.lordkleiton.desafiomobills.view.recyclerview.listener

import com.lordkleiton.desafiomobills.model.Receita

interface IncomesActionListener {
    fun onEdit(current: Pair<String, Receita>)

    fun onDelete(id: String)
}