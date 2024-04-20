package br.com.apps.trucktech.expressions

fun <K, V> Map<K, V>.getKeyByValue(value: V): K? {
    return this.entries.find { it.value == value }?.key
}