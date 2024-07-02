package br.com.apps.trucktech.expressions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun BigDecimal.toCurrencyPtBr(): String {
    val formater = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
    return formater.format(this)
}

fun BigDecimal.toNumberDecimalPtBr(): String {
    val formater = NumberFormat.getNumberInstance(Locale("pt", "br"))
    return formater.format(this)
}