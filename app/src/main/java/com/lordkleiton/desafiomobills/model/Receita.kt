package com.lordkleiton.desafiomobills.model

import java.math.BigDecimal
import java.util.*

data class Receita(
    val valor: BigDecimal = BigDecimal(0),
    val descricao: String = "",
    val data: Date = Date(),
    val recebido: Boolean = false
)
