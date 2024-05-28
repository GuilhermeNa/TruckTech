package br.com.apps.trucktech.ui.fragments.nav_documents.documents_list

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentTravelsBinding
import br.com.apps.trucktech.util.state.StateI

class TravelsListState(private val binding: FragmentTravelsBinding) : StateI {

    override fun showLoading() {
        binding.travelsFragmentRecycler.visibility = GONE
        binding.fragTravelFab.visibility = GONE
        binding.fragTravelListDarkLayer.visibility = GONE
        binding.fragTravelsBoxError.layout.visibility = GONE
        binding.fragTravelsBoxError.error.visibility = GONE
        binding.fragTravelsBoxError.empty.visibility = GONE
    }

    override fun showLoaded() {
        binding.travelsFragmentRecycler.visibility = VISIBLE
        binding.fragTravelFab.visibility = VISIBLE
        binding.fragTravelListDarkLayer.visibility = GONE
        binding.fragTravelsBoxError.layout.visibility = GONE
        binding.fragTravelsBoxError.error.visibility = GONE
        binding.fragTravelsBoxError.empty.visibility = GONE
    }

    override fun showEmpty() {
        binding.travelsFragmentRecycler.visibility = GONE
        binding.fragTravelFab.visibility = VISIBLE
        binding.fragTravelListDarkLayer.visibility = GONE
        binding.fragTravelsBoxError.layout.visibility = VISIBLE
        binding.fragTravelsBoxError.error.visibility = GONE
        binding.fragTravelsBoxError.empty.visibility = VISIBLE
    }

    override fun showError(e: Exception) {
        binding.travelsFragmentRecycler.visibility = GONE
        binding.fragTravelFab.visibility = VISIBLE
        binding.fragTravelListDarkLayer.visibility = GONE
        binding.fragTravelsBoxError.layout.visibility = VISIBLE
        binding.fragTravelsBoxError.error.visibility = VISIBLE
        binding.fragTravelsBoxError.empty.visibility = GONE
        Log.e(TAG_DEBUG, e.message.toString())
        e.printStackTrace()
    }

}