package com.lordkleiton.desafiomobills.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Number.toCurrency(): String {
    return DecimalFormat.getCurrencyInstance().format(this)
}

fun Long.byHundred(): Double {
    return this / 100.0
}

fun Date.formatDate(): String {
    return SimpleDateFormat.getDateInstance().format(this)
}