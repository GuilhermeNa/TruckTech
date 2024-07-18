package br.com.apps.model.expressions

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

/**
 * Formats a BigDecimal value as a currency string in Brazilian Portuguese (BRL).
 *
 * @return String representation of the BigDecimal formatted as currency.
 */
fun BigDecimal.toCurrencyPtBr(): String {
    val formater = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
    return formater.format(this)
}

/**
 * Formats a BigDecimal value as a decimal number string in Brazilian Portuguese format.
 *
 * @return String representation of the BigDecimal formatted as a decimal number.
 */
fun BigDecimal.toNumberDecimalPtBr(): String {
    val formater = NumberFormat.getNumberInstance(Locale("pt", "br"))
    return formater.format(this)
}

/**
 * Converts a BigDecimal value to its percentage representation.
 *
 * @return BigDecimal value divided by 100, rounded to two decimal places.
 */
fun BigDecimal.toPercentValue(): BigDecimal {
    return this.divide(BigDecimal(100), 2, RoundingMode.HALF_EVEN)
}