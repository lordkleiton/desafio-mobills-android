package com.lordkleiton.desafiomobills.model

import com.google.firebase.Timestamp
import java.io.Serializable

open class BaseData(
    val valor: Long,
    val descricao: String,
    val data: Timestamp,
) : Serializable {
    open fun toJson() = mapOf("valor" to valor, "descricao" to descricao, "data" to data)
}
