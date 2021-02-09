package com.lordkleiton.desafiomobills.model

import java.math.BigDecimal
import java.util.*

data class Despesa(
    val valor: BigDecimal = BigDecimal(0),
    val descricao: String = "",
    val data: Date = Date(),
    val pago: Boolean = false
)
