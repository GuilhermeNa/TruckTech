package br.com.apps.trucktech.ui.fragments.nav_home.discount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.databinding.FragmentDiscountBinding
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters.AdvanceRecyclerAdapter
import br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters.CostHelpRecyclerAdapter
import br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters.LoanRecyclerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TOOLBAR_TITLE = "Descontos"

class DiscountFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentDiscountBinding? = null
    private val binding get() = _binding!!
    private var stateHandler: DiscountState? = null

    private var aidAdapter: CostHelpRecyclerAdapter? = null
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
        var isLoansEmpty = false
        var isAdvancesEmpty = false
        var isAidsEmpty = false
        fun isEmptyData() = isLoansEmpty && isAdvancesEmpty && isAidsEmpty

        mainActVM.cachedLoans.value?.let { loans ->
            when (loans.isEmpty()) {
                true -> {
                    stateHandler?.hideLoans()
                    isLoansEmpty = true
                }

                false -> {
                    stateHandler?.showLoans()
                    loanAdapter?.update(loans)
                }
            }
        }

        mainActVM.cachedAdvances.value?.let { advances ->
            when (advances.isEmpty()) {
                true -> {
                    stateHandler?.hideAdvances()
                    isAdvancesEmpty = true
                }

                false -> {
                    stateHandler?.showAdvances()
                    advanceAdapter?.update(advances)
                }
            }
        }

        mainActVM.cachedAids.value?.let { aids ->
            when (aids.isEmpty()) {
                true -> {
                    stateHandler?.hideCostHelps()
                    isAidsEmpty = true
                }

                false -> {
                    stateHandler?.showCostHelps()
                    aidAdapter?.update(aids)
                }
            }
        }

        if (isEmptyData()) stateHandler?.showEmpty()

    }

    private fun initPanelCostHelp() {
        val recyclerView = binding.fragmentDiscountPanelCostHelp.panelCostHelpRecycler
        aidAdapter = CostHelpRecyclerAdapter(requireContext())
        recyclerView.adapter = aidAdapter
    }

    /**
     * Initialize Advance Recycler View
     */
    private fun initPanelAdvance() {
        val recyclerView = binding.fragmentDiscountPanelAdvance.panelAdvanceRecycler
        advanceAdapter = AdvanceRecyclerAdapter(requireContext())
        recyclerView.adapter = advanceAdapter

        val divider = DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    /**
     * Initialize Loan Recycler View
     */
    private fun initPanelLoan() {
        val recyclerView = binding.fragmentDiscountPanelLoan.panelLoanRecycler
        loanAdapter = LoanRecyclerAdapter(requireContext())
        recyclerView.adapter = loanAdapter
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        stateHandler = null
        aidAdapter = null
        advanceAdapter = null
        loanAdapter = null
        _binding = null

    }

}