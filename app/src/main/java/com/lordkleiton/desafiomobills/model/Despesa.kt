package com.lordkleiton.desafiomobills.model

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

class Despesa(
    valor: Long = 0,
    descricao: String = "",
    data: Timestamp = Timestamp(Date()),
    userId: String = "",
    val pago: Boolean = false
) : BaseData(valor, descricao, data, userId)