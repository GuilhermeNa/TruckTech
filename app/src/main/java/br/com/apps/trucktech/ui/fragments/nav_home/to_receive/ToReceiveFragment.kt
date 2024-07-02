package br.com.apps.trucktech.ui.fragments.nav_home.to_receive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.apps.trucktech.databinding.FragmentToReceiveBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TOOLBAR_TITLE = "Valores a receber"

class ToReceiveFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentToReceiveBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ToReceiveViewModel by viewModel()

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToReceiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPaymentsPanel()
        initRefundsPanel()
        initDiscountsPanel()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentToReceiveToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentToReceiveToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    private fun initPaymentsPanel() {
        binding.fragmentToReceivePanelPayments.apply {
            panelToReceiveFragmentPaymentsImage.loadImageThroughUrl(
                viewModel.paymentsUrlImage
            )
            panelToReceiveFragmentCard.setOnClickListener {
                it.navigateTo(ToReceiveFragmentDirections.actionToReceiveFragmentToPaymentFragment())
            }
        }
    }

    private fun initRefundsPanel() {
        binding.fragmentToReceivePanelRefunds.apply {
            panelToReceiveFragmentRefundImage.loadImageThroughUrl(
                viewModel.refundsUrlImage
            )
            panelToReceiveFragmentRefundCard.setOnClickListener {
                it.navigateTo(ToReceiveFragmentDirections.actionToReceiveFragmentToRefundFragment())
            }
        }
    }

    private fun initDiscountsPanel() {
        binding.fragmentToReceivePanelDiscounts.apply {
            panelToReceiveFragmentDiscountsImage.loadImageThroughUrl(
                viewModel.discountsUrlImage
            )
            panelToReceiveFragmentDiscountsCard.setOnClickListener {
                it.navigateTo(ToReceiveFragmentDirections.actionToReceiveFragmentToDiscountFragment())
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