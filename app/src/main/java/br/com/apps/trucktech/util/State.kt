package br.com.apps.trucktech.util

sealed class State {
    object Loading: State()
    object Loaded: State()
    object Empty: State()
    data class Error(val error: Exception) : State()
}
