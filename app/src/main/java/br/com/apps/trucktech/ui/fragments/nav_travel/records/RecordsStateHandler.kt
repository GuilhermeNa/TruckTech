package br.com.apps.trucktech.ui.fragments.nav_travel.records

import android.view.View.GONE
import android.view.View.VISIBLE
import br.com.apps.trucktech.databinding.FragmentRecordsBinding

class RecordsStateHandler(private val binding: FragmentRecordsBinding) {

    fun showError(e: Exception) {
        e.printStackTrace()
        binding.apply {
            recordsFragmentTabLayout.visibility = GONE
            layout.visibility = GONE
            boxError.apply {
                layout.visibility = VISIBLE
                error.visibility = VISIBLE
            }
        }
    }

    fun showLoadedWhenUnfinished() {
        binding.recordsFragmentFab.visibility = VISIBLE
    }

    fun showLoadedWhenIsFinished() {
        binding.recordsFragmentFab.visibility = GONE
    }

}