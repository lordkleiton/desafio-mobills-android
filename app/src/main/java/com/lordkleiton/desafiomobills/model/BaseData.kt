package com.lordkleiton.desafiomobills.model

import java.math.BigDecimal
import java.util.*

open class BaseData(
    val valor: BigDecimal = BigDecimal(0),
    val descricao: String = "",
    val data: Date = Date(),
)
