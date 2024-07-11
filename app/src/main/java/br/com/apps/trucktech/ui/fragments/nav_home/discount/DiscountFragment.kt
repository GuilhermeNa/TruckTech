package br.com.apps.trucktech.ui.fragments.nav_home.discount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.databinding.FragmentDiscountBinding
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters.AdvanceRecyclerAdapter
import br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters.CostHelpRecyclerAdapter
import br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters.LoanRecyclerAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Descontos"

class DiscountFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentDiscountBinding? = null
    private val binding get() = _binding!!
    private var stateHandler: DiscountState? = null

    private val employeeId by lazy { mainActVM.loggedUser.driverId }
    private val viewModel: DiscountViewModel by viewModel { parametersOf(employeeId) }

    private var costHelpAdapter: CostHelpRecyclerAdapter? = null
    private var advanceAdapter: AdvanceRecyclerAdapter? = null
    private var loanAdapter: LoanRecyclerAdapter? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscountBinding.inflate(inflater, container, false)
        stateHandler = DiscountState(binding)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPanelCostHelp()
        initPanelAdvance()
        initPanelLoan()
        initStateManager()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentDiscountToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentDiscountToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes advanceData for update the recyclerView
     *   - Observes loanData for update the recyclerView
     */
    private fun initStateManager() {
        getCachedData()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DiscountFState.Loading -> stateHandler?.showLoading()

                is DiscountFState.Loaded -> {
                    if (state.hasAdvances) stateHandler?.showAdvances()
                    else stateHandler?.hideAdvances()

                    if (state.hasLoans) stateHandler?.showLoans()
                    else stateHandler?.hideLoans()

                    if(state.hasCostHelps) stateHandler?.showCostHelps()
                    else stateHandler?.hideCostHelps()
                }

                is DiscountFState.Empty -> stateHandler?.showEmpty()

                is DiscountFState.Error -> {
                    state.error.printStackTrace()
                    stateHandler?.showError(state.error)
                }

            }
        }
    }

    private fun getCachedData() {
        lifecycleScope.launch {
            val loans = mainActVM.cachedLoans.asFlow().first() ?: emptyList()
            val advances = mainActVM.cachedAdvances.asFlow().first() ?: emptyList()
            val travels = mainActVM.cachedTravels.asFlow().first() ?: emptyList()

            viewModel.initFragmentData(loans, advances, travels).let { data ->
                data.apply {
                    if(costHelps.isNotEmpty()) costHelpAdapter?.update(costHelps)

                    if (advances.isNotEmpty()) advanceAdapter?.update(advances)

                    if (loans.isNotEmpty()) loanAdapter?.update(loans)

                }

            }

        }
    }

    private fun initPanelCostHelp() {
        val recyclerView = binding.fragmentDiscountPanelCostHelp.panelCostHelpRecycler
        costHelpAdapter = CostHelpRecyclerAdapter(requireContext())
        recyclerView.adapter = costHelpAdapter
    }

    /**
     * Initialize Advance Recycler View
     */
    private fun initPanelAdvance() {
        val recyclerView = binding.fragmentDiscountPanelAdvance.panelAdvanceRecycler
        advanceAdapter = AdvanceRecyclerAdapter(requireContext(), emptyList())
        recyclerView.adapter = advanceAdapter

        val divider = DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    /**
     * Initialize Loan Recycler View
     */
    private fun initPanelLoan() {
        val recyclerView = binding.fragmentDiscountPanelLoan.panelLoanRecycler
        loanAdapter = LoanRecyclerAdapter(requireContext(), emptyList())
        recyclerView.adapter = loanAdapter
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        stateHandler = null
        costHelpAdapter = null
        advanceAdapter = null
        loanAdapter = null
        _binding = null

    }

}