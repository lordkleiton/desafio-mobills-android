package com.lordkleiton.desafiomobills.model

import com.google.firebase.Timestamp
import java.util.*

class Receita(
    valor: Long = 0,
    descricao: String = "",
    data: Timestamp = Timestamp(Date()),
    val recebido: Boolean = false,
) : BaseData(valor, descricao, data)
