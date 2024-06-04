package br.com.apps.trucktech.ui.fragments.nav_home.home

import android.view.View.GONE
import android.view.View.VISIBLE
import br.com.apps.trucktech.databinding.FragmentHomeBinding

private const val FAILED_TO_LOAD = "Falha ao carregar dados do usuário"

class HomeState(private val binding: FragmentHomeBinding) {

    fun showLoading() {
        binding.apply {
            boxHeader.layout.visibility = GONE
            fragReceivable.visibility = GONE
            fragPerformance.visibility = GONE
            boxFines.layout.visibility = GONE
            boxTimeline.layout.visibility = GONE
            loadingScreen.layout.visibility = VISIBLE
        }
    }

    fun showError() {
        binding.apply {
            loadingScreen.layout.visibility = GONE

            boxError.apply {
                error.text = FAILED_TO_LOAD
                layout.visibility = VISIBLE
                error.visibility = VISIBLE
            }
        }

    }

    fun showLoaded() {
        binding.apply {
            boxHeader.layout.visibility = VISIBLE
            fragReceivable.visibility = VISIBLE
            fragPerformance.visibility = VISIBLE
            boxFines.layout.visibility = VISIBLE
            boxTimeline.layout.visibility = VISIBLE
            loadingScreen.layout.visibility = GONE
            swipeRefresh.isRefreshing = false
        }
    }

}