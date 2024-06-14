package br.com.apps.trucktech.ui.fragments.nav_requests.requests_list

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentRequestsListBinding
import br.com.apps.trucktech.util.state.StateI

class RequestsListState(private val binding: FragmentRequestsListBinding): StateI {

    override fun showLoading() {}

    override fun showLoaded() {
        binding.fragmentRequestsListHeaderRecycler.visibility = VISIBLE
        binding.fragmentRequestsListFab.visibility = VISIBLE
        binding.fragRequestsBoxError.layout.visibility = GONE
        binding.fragRequestsBoxError.empty.visibility = GONE
        binding.fragRequestsBoxError.error.visibility = GONE
    }

    override fun showEmpty() {
        binding.fragRequestsBoxError.layout.visibility = VISIBLE
        binding.fragRequestsBoxError.empty.visibility = VISIBLE
        binding.fragRequestsBoxError.error.visibility = GONE
        binding.fragmentRequestsListFab.visibility = VISIBLE
    }

    override fun showError(e: Exception) {
        binding.fragRequestsBoxError.layout.visibility = VISIBLE
        binding.fragRequestsBoxError.error.visibility = VISIBLE
        binding.fragRequestsBoxError.empty.visibility = GONE
        binding.fragmentRequestsListFab.visibility = VISIBLE
        Log.e(TAG_DEBUG, e.message.toString())
        e.printStackTrace()
    }

    override fun showDeleting() {
        binding.boxLoading.apply {
            layout.visibility = VISIBLE
        }
    }

    override fun showDeleted() {
        binding.boxLoading.apply {
            layout.visibility = GONE
        }
    }

}