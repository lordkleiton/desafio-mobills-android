package com.lordkleiton.desafiomobills.model

import com.google.firebase.Timestamp

open class BaseData
    (
    val valor: Long,
    val descricao: String,
    val data: Timestamp,
    val userId: String
)