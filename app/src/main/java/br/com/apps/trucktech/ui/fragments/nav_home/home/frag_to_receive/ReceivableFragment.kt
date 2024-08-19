package br.com.apps.trucktech.ui.fragments.nav_home.home.frag_to_receive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.trucktech.databinding.FragmentReceivableBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentForMainAct
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeFragmentDirections
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal
import java.math.RoundingMode

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

        when (viewModel.isFirstBoot) {
            true -> launchFirstBoot()
            false -> launch()
        }

    }


    private fun launchFirstBoot() {
        lifecycleScope.launch {
            viewModel.processData(
                mainActVM.cachedLoans.asFlow().first(),
                mainActVM.cachedAdvances.asFlow().first(),
                mainActVM.cachedOutlayToPay.asFlow().first(),
                mainActVM.cachedFreightToPay.asFlow().first()
            )?.run {
                viewModel.setFirstBoot()
                bind(this)
            }
        }
    }

    private fun launch() {
        mainActVM.cachedFreightToPay.observe(viewLifecycleOwner) {
            viewModel.processData(
                loans = mainActVM.getLoans(),
                advances = mainActVM.getAdvances(),
                outlays = mainActVM.getOutlays(),
                freights = it
            )?.run {
                bind(this)
            }
        }

        mainActVM.cachedAdvances.observe(viewLifecycleOwner) {
            viewModel.processData(
                loans = mainActVM.getLoans(),
                advances = it,
                outlays = mainActVM.getOutlays(),
                freights = mainActVM.getFreights()
            )?.run {
                bind(this)
            }
        }

        mainActVM.cachedLoans.observe(viewLifecycleOwner) {
            viewModel.processData(
                loans = it,
                advances = mainActVM.getAdvances(),
                outlays = mainActVM.getOutlays(),
                freights = mainActVM.getFreights()
            )?.run {
                bind(this)
            }
        }
    }

    private fun bind(data: ReceivableFData) {
        binding.apply {
            panelToReceiveValue.text = data.liquid.toCurrencyPtBr()

            panelToReceiveProgressBar.progress = data.outlayPerc + data.commissionPerc
            panelToReceiveProgressBar.secondaryProgress = data.commissionPerc
            panelToReceivePercentualCommission.text = "${data.commissionPerc}%"
            panelToReceivePercentualRefund.text = "${data.outlayPerc}%"
            panelToReceiveLayoutPercentualExpend.run {
                if (data.outlayPerc > 0) {
                    this.visibility = View.VISIBLE
                    val myParam = this.layoutParams as ConstraintLayout.LayoutParams
                    val bias = toFloat(data.commissionPerc)
                    myParam.horizontalBias = bias
                    this.layoutParams = myParam
                } else {
                    this.visibility = View.GONE
                }
            }

            if (data.discountPerc > 0) {
                panelToReceiveLayoutPercentualDiscount.visibility = View.VISIBLE
                panelToReceivePercentualDiscounts.text = "${data.discountPerc}%"
                panelToReceiveNegativeBar.run {
                    val myParam = this.layoutParams as ConstraintLayout.LayoutParams
                    val percentWidth = toFloat(data.discountPerc)
                    myParam.matchConstraintPercentWidth = percentWidth
                    this.layoutParams = myParam
                    visibility = View.VISIBLE
                }
            }

            panelToReceiveNumberOfFreights.text = data.commissionSize
            panelToReceiveNumberOfExpends.text = data.outlaySize
            panelToReceiveNumberOfDiscounts.text = data.discountSize
            panelToReceiveDescription.text = TO_RECEIVE_BOX_DESCRIPTION
            panelToReceiveButton.setOnClickListener {
                it.navigateTo(HomeFragmentDirections.actionHomeFragmentToToReceiveFragment())
            }
        }
    }

    private fun toFloat(commissionPercent: Int): Float {
        return BigDecimal(commissionPercent)
            .divide(BigDecimal(100), 2, RoundingMode.HALF_EVEN)
            .toFloat()
    }

//---------------------------------------------------------------------------------------------//
// ON DESTROY VIEW
//---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

