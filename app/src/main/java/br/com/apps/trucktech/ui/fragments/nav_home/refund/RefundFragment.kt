package br.com.apps.trucktech.ui.fragments.nav_home.refund

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.databinding.FragmentRefundBinding
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.expressions.toBold
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.trucktech.expressions.toUnderline
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.public_adapters.ToReceiveRecyclerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Reembolsos"

class RefundFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRefundBinding? = null
    private val binding get() = _binding!!

    private val driverId by lazy {
        sharedViewModel.userData.value?.user?.employeeId
    }
    private val viewModel: RefundViewModel by viewModel { parametersOf(driverId) }
    private var adapter: ToReceiveRecyclerAdapter<Expend>? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRefundBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initRecyclerView()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentRefundToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentRefundToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Initialize the state manager and observes [viewModel] data.
     *
     *   - Observes expendData for update the recyclerView and bind
     */
    private fun initStateManager() {
        viewModel.expendData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    response.exception.printStackTrace()
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                }

                is Response.Success -> {
                    response.data?.let { dataSet ->
                        adapter?.update(dataSet)
                        bindText(dataSet)
                    }
                }
            }
        }
    }

    private fun bindText(dataSet: List<Expend>) {
        val numberOfExpends = dataSet.size
        val totalExpends = dataSet.sumOf { it.value!! }

        val refundsFormatted = SpannableString(
            numberOfExpends.toString()
        ).toBold().toUnderline()
        val valueFormatted = SpannableString(
            totalExpends.toCurrencyPtBr()
        ).toBold().toUnderline()

        binding.fragmentRefundDescription.text =
            if (numberOfExpends > 1) {
                buildSpannedString {
                    append("Você tem ")
                    append(refundsFormatted)
                    append(" despesas a serem reembolsadas, totalizando ")
                    append(valueFormatted)
                    append(".")
                }
            } else {
                buildSpannedString {
                    append("Você tem ")
                    append(refundsFormatted)
                    append(" despesa a ser reembolsada no valor de ")
                    append(valueFormatted)
                    append(".")
                }
            }
    }

    /**
     * Initialize Recycler view.
     */
    private fun initRecyclerView() {
        val recyclerView = binding.fragmentRefundRecycler
        adapter = ToReceiveRecyclerAdapter(requireContext(), emptyList())
        recyclerView.adapter = adapter

        val divider = DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }

}