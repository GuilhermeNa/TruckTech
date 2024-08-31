package br.com.apps.trucktech.util.state

sealed class DataEvent<out T> {
    data class Initialize<T>(val data: T) : DataEvent<T>()
    data class Insert<T>(val item: T) : DataEvent<T>()
    data class Remove<T>(val item: T) : DataEvent<T>()
    data class Update<T>(val item: T) : DataEvent<T>()
}
