package br.com.apps.trucktech.expressions

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * This method get the the month abbreviated in Portuguese-Br.
 *
 * example: January = jan
 * @return a string with the month
 */
fun LocalDateTime.getMonthInPtBrAbbreviated(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM", Locale("pt", "BR"))
    val monthInString = this.format(formatter)
    return monthInString.replace(".", "")
}

/**
 * This method get the the month in Portuguese-Br.
 *
 * example:
 * January = janeiro
 * @return a string with the month
 */
fun LocalDateTime.getMonthInPtBr(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM", Locale("pt", "BR"))
    return format(formatter)
}

/**
 * This method get the the month and year in Portuguese-Br.
 *
 * example:
 * January, 2023 = janeiro 2024
 * @return a string with the month and the year
 */
fun LocalDateTime.getMonthAndYearInPtBr(): String {
    val month = this.getMonthInPtBr()
    val year = this.year.toString()

    val stringBuilder = StringBuilder()
    return stringBuilder
        .append(month)
        .append(" de ")
        .append(year)
        .toString()
}

/**
 * This method format the date and add a "0" before if it has just one digit.
 *
 * example: 1 = 01
 * @return a string with the formatted day
 */
fun LocalDateTime.getDayFormatted(): String {
    val dayOfMonth = this.dayOfMonth
    if (dayOfMonth < 10) {
        return "0$dayOfMonth"
    }
    return dayOfMonth.toString()
}

/**
 * This method format the month and add a "0" before if it has just one digit.
 *
 * example: 1 = 01
 * @return a string with the formatted month
 */
fun LocalDateTime.getMonthFormatted(): String {
    val month = this.monthValue
    return if (month < 10) "0$month"
    else month.toString()
}

/**
 * This method get the day, month and year in Portuguese-Br.
 *
 * example:
 * January, 2023 = 11 de janeiro de 2024
 * @return a string representing the date
 */
fun LocalDateTime.getCompleteDateInPtBr(): String {
    val day = this.getDayFormatted()
    val month = this.getMonthInPtBr()
    val year = this.year.toString()

    val stringBuilder = StringBuilder()
    return stringBuilder
        .append(day)
        .append(" de ")
        .append(month)
        .append(" de ")
        .append(year)
        .toString()
}

/**
 * This method format the year and get last 2 digits.
 *
 * example: 2023 = 23
 * @return a string with the formatted year
 */
fun LocalDateTime.getYearReference(): String {
    val year = this.year.toString()
    return year.substring(2)
}

fun String.toLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    return LocalDateTime.parse(this, formatter)
}

fun LocalDateTime.atBrZone(): LocalDateTime {
    val zone = ZoneId.of("America/Sao_Paulo")
    return this.atZone(zone).toLocalDateTime()

}


