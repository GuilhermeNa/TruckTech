package br.com.apps.trucktech.ui.fragments.nav_home.discount

import android.view.View.GONE
import android.view.View.VISIBLE
import br.com.apps.trucktech.databinding.FragmentDiscountBinding

class DiscountState(private val binding: FragmentDiscountBinding) : DiscountFStateI {

    override fun showLoading() {
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

    override fun showCostHelps() {
        binding.fragmentDiscountPanelCostHelp.layout.visibility = VISIBLE
    }

    override fun hideCostHelps() {
        binding.fragmentDiscountPanelCostHelp.layout.visibility = GONE
    }

    override fun showAdvances() {
        binding.fragmentDiscountPanelAdvance.layout.visibility = VISIBLE
    }

    override fun hideAdvances() {
        binding.fragmentDiscountPanelAdvance.layout.visibility = GONE
    }

    override fun showLoans() {
        binding.fragmentDiscountPanelLoan.layout.visibility = VISIBLE
    }

    override fun hideLoans() {
        binding.fragmentDiscountPanelLoan.layout.visibility = GONE
    }

    override fun showError(e: Exception) {
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

    override fun showEmpty() {
        binding.apply {
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

interface DiscountFStateI {

    fun showLoading()

    fun showCostHelps()

    fun hideCostHelps()

    fun showAdvances()

    fun hideAdvances()

    fun showLoans()

    fun hideLoans()

    fun showEmpty()

    fun showError(e: Exception)


}

sealed class DiscountFState() {
    object Loading : DiscountFState()
    data class Loaded(val hasCostHelps: Boolean, val hasAdvances: Boolean, val hasLoans: Boolean) :
        DiscountFState()

    object Empty : DiscountFState()
    data class Error(val error: Exception) : DiscountFState()
}