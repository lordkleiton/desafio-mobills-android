package com.lordkleiton.desafiomobills.model

import java.math.BigDecimal
import java.util.*

class Receita(
    valor: BigDecimal = BigDecimal(0),
    descricao: String = "",
    data: Date = Date(),
    val recebido: Boolean = false
) : BaseData(valor, descricao, data)
