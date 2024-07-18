package br.com.apps.model

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

fun Date.toLocalDateTime(): LocalDateTime {
    val instant = this.toInstant()
    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
}

fun LocalDateTime.toDate(): Date {
    val atZone = this.atZone(ZoneId.systemDefault())
    return Date.from(atZone.toInstant())
}

fun String.toLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    return LocalDateTime.parse(this, formatter)
}
