package br.com.apps.trucktech.ui.fragments.nav_home.discount

import android.view.View.GONE
import android.view.View.VISIBLE
import br.com.apps.trucktech.databinding.FragmentDiscountBinding

class DiscountState(private val binding: FragmentDiscountBinding) {

    fun showLoading() {
        binding.apply {
            fragmentDiscountPanelCostHelp.layout.visibility = GONE
            fragmentDiscountPanelLoan.layout.visibility = GONE
            fragmentDiscountPanelAdvance.layout.visibility = GONE

            layoutError.apply {
                layout.visibility = GONE
                error.visibility = GONE
                empty.visibility = GONE
            }
        }
    }

    fun showCostHelps() {
        binding.fragmentDiscountPanelCostHelp.layout.visibility = VISIBLE
    }

    fun hideCostHelps() {
        binding.fragmentDiscountPanelCostHelp.layout.visibility = GONE
    }

    fun showAdvances() {
        binding.fragmentDiscountPanelAdvance.layout.visibility = VISIBLE
    }

    fun hideAdvances() {
        binding.fragmentDiscountPanelAdvance.layout.visibility = GONE
    }

    fun showLoans() {
        binding.fragmentDiscountPanelLoan.layout.visibility = VISIBLE
    }

    fun hideLoans() {
        binding.fragmentDiscountPanelLoan.layout.visibility = GONE
    }

    fun showError(e: Exception) {
        binding.apply {
            fragmentDiscountPanelLoan.layout.visibility = GONE
            fragmentDiscountPanelAdvance.layout.visibility = GONE

            layoutError.apply {
                layout.visibility = VISIBLE
                error.visibility = VISIBLE
                empty.visibility = GONE
            }
        }
    }

    fun showEmpty() {
        binding.apply {
            fragmentDiscountPanelCostHelp.layout.visibility = GONE
            fragmentDiscountPanelLoan.layout.visibility = GONE
            fragmentDiscountPanelAdvance.layout.visibility = GONE

            layoutError.apply {
                layout.visibility = VISIBLE
                error.visibility = GONE
                empty.visibility = VISIBLE
            }
        }
    }

}