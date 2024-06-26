package br.com.apps.trucktech.util.state

sealed class State {
    object Loading: State()
    object Loaded: State()
    object Empty: State()
    object Updating: State()
    data class Error(val error: Exception) : State()

    object Deleting: State()
    object Deleted: State()
    object Adding: State()
    object Added: State()
}

interface StateI {

    fun showLoading()

    fun showLoaded()

    fun showEmpty()

    fun showUpdating()

    fun showError(e: Exception)

}

interface StateDeleteI {

    fun showDeleting()

    fun showDeleted()

}

interface StateAddI {

    fun showAdding()

    fun showAdded()

}

interface StatePreviewI {

    fun showWriteOptions()

    fun hideWriteOptions()

}

interface StateDarkLayerI {

    fun hasDarkLayer(hasLayer: Boolean)

}