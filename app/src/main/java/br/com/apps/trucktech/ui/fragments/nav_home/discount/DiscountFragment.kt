package br.com.apps.trucktech.ui.fragments.nav_home.discount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.databinding.FragmentDiscountBinding
import br.com.apps.trucktech.sampleAdvancesList
import br.com.apps.trucktech.sampleLoanList
import br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters.AdvanceRecyclerAdapter
import br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters.LoanRecyclerAdapter
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar

private const val TOOLBAR_TITLE = "Descontos"

/**
 * A simple [Fragment] subclass.
 * Use the [DiscountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscountFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentDiscountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiscountFragmentViewModel by viewModels()

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
    }

    private fun initPanelAdvance() {
        val recyclerView = binding.fragmentDiscountPanelAdvance.panelAdvanceRecycler
        val adapter = AdvanceRecyclerAdapter(requireContext(), sampleAdvancesList)
        recyclerView.adapter = adapter

        val divider = DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    private fun initPanelLoan() {
        val recyclerView = binding.fragmentDiscountPanelLoan.panelLoanRecycler
        val adapter = LoanRecyclerAdapter(requireContext(), sampleLoanList)
        recyclerView.adapter = adapter
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

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}