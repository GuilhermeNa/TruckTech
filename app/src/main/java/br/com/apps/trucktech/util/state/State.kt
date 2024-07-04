package br.com.apps.trucktech.util.state

sealed class State {
    object Loading: State()
    object Loaded: State()

    object Empty: State()
    data class Error(val error: Exception) : State()

    object Updating: State()

    object Deleting: State()
    object Deleted: State()

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