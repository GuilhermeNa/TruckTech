package br.com.apps.trucktech.ui.fragments.nav_home.discount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.Response
import br.com.apps.trucktech.databinding.FragmentDiscountBinding
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters.AdvanceRecyclerAdapter
import br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters.LoanRecyclerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Descontos"

/**
 * A simple [Fragment] subclass.
 * Use the [DiscountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscountFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentDiscountBinding? = null
    private val binding get() = _binding!!

    private val employeeId = sharedViewModel.userData.value?.user?.employeeId
    private val viewModel: DiscountViewModel by viewModel { parametersOf(employeeId) }

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
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPanelAdvance()
        initPanelLoan()
        initStateManager()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
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
        viewModel.advanceData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    response.exception.printStackTrace()
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                }

                is Response.Success -> {
                    response.data?.let { dataSet ->
                        advanceAdapter?.update(dataSet)
                    }
                }
            }
        }

        viewModel.loanData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    response.exception.printStackTrace()
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                }

                is Response.Success -> {
                    response.data?.let { dataSet ->
                        loanAdapter?.update(dataSet)
                    }
                }
            }
        }
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
        advanceAdapter = null
        loanAdapter = null
        _binding = null

    }

}