package br.com.apps.trucktech.ui.fragments.nav_requests.request_preview

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRequestPreviewBinding
import br.com.apps.trucktech.util.state.StateI
import br.com.apps.trucktech.util.state.StatePreviewI

class RequestPreviewState(private val binding: FragmentRequestPreviewBinding) : StateI,
    StatePreviewI {

    override fun showWriteOptions() {
        binding.apply {
            fragmentRequestPreviewToolbar.toolbar.apply {
                menu.findItem(R.id.menu_preview_delete).isEnabled = true
                menu.findItem(R.id.menu_preview_delete).isVisible = true
                menu.findItem(R.id.menu_preview_edit).isEnabled = true
                menu.findItem(R.id.menu_preview_edit).isVisible = true
            }
        }
    }

    override fun hideWriteOptions() {
        binding.apply {
            fragmentRequestPreviewToolbar.toolbar.apply {
                menu.findItem(R.id.menu_preview_delete).isEnabled = false
                menu.findItem(R.id.menu_preview_delete).isVisible = false
                menu.findItem(R.id.menu_preview_edit).isEnabled = false
                menu.findItem(R.id.menu_preview_edit).isVisible = false
            }
        }
    }

    fun showDarkLayer() {
        binding.fragRequestPreviewDarkLayer.visibility = View.VISIBLE
    }

    fun hideDarkLayer() {
        binding.fragRequestPreviewDarkLayer.visibility = View.GONE
    }

    override fun showLoading() {
        binding.boxLoading.layout.visibility = VISIBLE
    }

    override fun showLoaded() {
        showAfterLoading()
    }

    override fun showEmpty() {}

    override fun showUpdating() {}

    override fun showError(e: Exception) {
        showAfterLoading()
        binding.boxError.apply {
            layout.visibility = VISIBLE
            error.visibility = VISIBLE
        }
    }

    private fun showAfterLoading() {
        binding.boxLoading.layout.apply {
            if (visibility == VISIBLE) visibility = GONE
        }
    }

}