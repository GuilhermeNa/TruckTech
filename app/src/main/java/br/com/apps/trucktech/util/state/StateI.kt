package br.com.apps.trucktech.util.state

interface StateI {

    fun showLoading()

    fun showLoaded()

    fun showEmpty()

    fun showError(e: Exception)

}
