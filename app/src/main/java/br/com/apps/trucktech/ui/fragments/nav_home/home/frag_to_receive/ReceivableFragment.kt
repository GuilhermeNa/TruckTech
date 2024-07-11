package br.com.apps.trucktech.ui.fragments.nav_home.home.frag_to_receive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import br.com.apps.trucktech.databinding.FragmentReceivableBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentForMainAct
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeFragmentDirections
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TO_RECEIVE_BOX_DESCRIPTION =
    "Este é o resumo do seu saldo a receber, clique em 'detalhes' para saber mais."

/**
 * A simple [Fragment] subclass.
 * Use the [ReceivableFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReceivableFragment : BaseFragmentForMainAct() {

    private var _binding: FragmentReceivableBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReceivableViewModel by viewModel()

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceivableBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
    }

    private fun initStateManager() {
        if (viewModel.isFirstBoot) launchFirstBoot()
        else launchAfterFirstBoot()
    }

    private fun launchAfterFirstBoot() {
        mainActVM.cachedTravels.observe(viewLifecycleOwner) {
            viewModel.updateData(it, mainActVM.getAdvances(), mainActVM.getLoans())
                .run { bind(this) }
        }

        mainActVM.cachedAdvances.observe(viewLifecycleOwner) {
            viewModel.updateData(mainActVM.getTravels(), it, mainActVM.getLoans())
                .run { bind(this) }
        }

        mainActVM.cachedLoans.observe(viewLifecycleOwner) {
            viewModel.updateData(mainActVM.getTravels(), mainActVM.getAdvances(), it)
                .run { bind(this) }
        }
    }

    private fun launchFirstBoot() {
        lifecycleScope.launch {
            val travels = mainActVM.cachedTravels.asFlow().first()
            val loans = mainActVM.cachedLoans.asFlow().first()
            val advances = mainActVM.cachedAdvances.asFlow().first()
            viewModel.initFragmentData(travels, loans, advances)?.run {
                bind(this)
            }
        }
    }

    private fun bind(data: ReceivableFData) {
        binding.apply {
            panelToReceiveValue.text = data.calculateLiquidReceivable().toCurrencyPtBr()

            val commissionPercent = data.calculateCommissionPercent()
            val expendPercent = data.calculateExpendPercent()
            val discountPercent = data.calculateDiscountPercent()

            panelToReceiveProgressBar.progress = expendPercent + commissionPercent
            panelToReceiveProgressBar.secondaryProgress = commissionPercent
            panelToReceivePercentualCommission.text = "$commissionPercent%"
            panelToReceivePercentualRefund.text = "$expendPercent%"
            panelToReceiveLayoutPercentualExpend.run {
                if (expendPercent > 0) {
                    this.visibility = View.VISIBLE
                    val myParam = this.layoutParams as ConstraintLayout.LayoutParams
                    val bias = viewModel.toFloat(commissionPercent)
                    myParam.horizontalBias = bias
                    this.layoutParams = myParam
                } else {
                    this.visibility = View.GONE
                }
            }

            if (discountPercent > 0) {
                panelToReceiveLayoutPercentualDiscount.visibility = View.VISIBLE
                panelToReceivePercentualDiscounts.text = "${discountPercent}%"
                panelToReceiveNegativeBar.run {
                    val myParam = this.layoutParams as ConstraintLayout.LayoutParams
                    val percentWidth = viewModel.toFloat(discountPercent)
                    myParam.matchConstraintPercentWidth = percentWidth
                    this.layoutParams = myParam
                    visibility = View.VISIBLE
                }
            }

            panelToReceiveNumberOfFreights.text = data.getFreightAmount()
            panelToReceiveNumberOfExpends.text = data.getExpendAmount()
            panelToReceiveNumberOfDiscounts.text = data.getDiscountAmount()
            panelToReceiveDescription.text = TO_RECEIVE_BOX_DESCRIPTION
            panelToReceiveButton.setOnClickListener {
                it.navigateTo(HomeFragmentDirections.actionHomeFragmentToToReceiveFragment())
            }
        }
    }

//---------------------------------------------------------------------------------------------//
// ON DESTROY VIEW
//---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

