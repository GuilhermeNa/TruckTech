package br.com.apps.trucktech.util.state

sealed class State {
    object Loading: State()
    object Loaded: State()
    object Empty: State()
    data class Error(val error: Exception) : State()

    object Deleting: State()
    object Deleted: State()

}

interface StateI {

    fun showLoading()

    fun showLoaded()

    fun showEmpty()

    fun showError(e: Exception)

    fun showDeleting()

    fun showDeleted()
}

interface StatePreviewI {

    fun showWriteOptions()

    fun hideWriteOptions()

}

interface StateDarkLayerI {

    fun hasDarkLayer(hasLayer: Boolean)

}