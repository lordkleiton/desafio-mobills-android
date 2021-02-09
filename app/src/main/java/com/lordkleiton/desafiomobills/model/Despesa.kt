package com.lordkleiton.desafiomobills.model

import java.math.BigDecimal
import java.util.*

class Despesa(
    valor: BigDecimal = BigDecimal(0),
    descricao: String = "",
    data: Date = Date(),
    val pago: Boolean = false
) : BaseData(valor, descricao, data)