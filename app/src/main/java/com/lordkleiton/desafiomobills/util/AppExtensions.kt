package com.lordkleiton.desafiomobills.util

import java.text.DecimalFormat

fun Number.toCurrency(): String {
    return DecimalFormat.getCurrencyInstance().format(this)
}