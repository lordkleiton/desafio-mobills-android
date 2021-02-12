package com.lordkleiton.desafiomobills.model

import com.google.firebase.Timestamp
import java.util.*

class Receita(
    valor: Long = 0,
    descricao: String = "",
    data: Timestamp = Timestamp(Date()),
    userId: String = "",
    val recebido: Boolean = false,
) : BaseData(valor, descricao, data, userId)
