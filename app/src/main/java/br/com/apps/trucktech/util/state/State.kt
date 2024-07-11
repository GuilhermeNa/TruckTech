package br.com.apps.trucktech.util.state

sealed class State {
    object Loading: State()
    object Loaded: State()
    object Empty: State()
    data class Error(val error: Exception) : State()
}

interface StateI {

    fun showLoading()

    fun showLoaded()

    fun showEmpty()

    fun showError(e: Exception)

}

interface StatePreviewI {

    fun showWriteOptions()

    fun hideWriteOptions()

}

interface StateDarkLayerI {

    fun hasDarkLayer(hasLayer: Boolean)

}