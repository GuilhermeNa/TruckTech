package br.com.apps.trucktech.ui.fragments.nav_home.payment

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.trucktech.databinding.FragmentPaymentBinding
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.expressions.toBold
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.trucktech.expressions.toUnderline
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.public_adapters.ToReceiveRecyclerAdapter
import br.com.apps.trucktech.util.state.State
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TOOLBAR_TITLE = "Pagamentos"

class PaymentFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentViewModel by viewModel()
    private var adapter: ToReceiveRecyclerAdapter<Freight>? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
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
            hasNavigation = true,
            toolbar = binding.fragmentPaymentToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentPaymentToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Initialize the state manager and observes [viewModel] data.
     *
     *   - Observes freightData for update the recyclerView and bind
     */
    private fun initStateManager() {
        mainActVM.cachedTravels.observe(viewLifecycleOwner) { travels ->
            travels?.let { t ->

                viewModel.updateData(t).let {  freight ->
                    if(freight.isNotEmpty()) {
                        bindText(freight)
                        adapter?.update(freight)
                    }
                }

            } ?: viewModel.setState(State.Error(NullPointerException()))

        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {
                    binding.apply {
                        layout.visibility = GONE

                        layoutError.apply {
                            layout.visibility = GONE
                            error.visibility = GONE
                            empty.visibility = GONE
                        }
                    }

                }

                is State.Loaded -> {
                    binding.apply {
                        layout.visibility = VISIBLE

                        binding.layoutError.apply {
                            layout.visibility = GONE
                            error.visibility = GONE
                            empty.visibility = GONE
                        }
                    }
                }

                is State.Empty -> {
                    binding.apply {
                        layout.visibility = GONE

                        binding.layoutError.apply {
                            layout.visibility = VISIBLE
                            error.visibility = GONE
                            empty.visibility = VISIBLE
                        }
                    }
                }

                is State.Error -> {
                    state.error.printStackTrace()
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                    binding.apply {
                        layout.visibility = GONE
                        binding.layoutError.apply {
                            layout.visibility = VISIBLE
                            error.visibility = VISIBLE
                            empty.visibility = GONE
                        }
                    }
                }

                else -> {}
            }
        }
    }

    private fun bindText(dataSet: List<Freight>) {
        val numberOfCommissions = dataSet.size
        val totalCommission = dataSet.sumOf { it.getCommissionValue() }

        val commissionsFormatted = SpannableString(
            numberOfCommissions.toString()
        ).toBold().toUnderline()
        val valueFormatted = SpannableString(
            totalCommission.toCurrencyPtBr()
        ).toBold().toUnderline()

        binding.fragmentPaymentDescription.text = buildSpannedString {
            append("Você tem ")
            append(commissionsFormatted)
            append(" comissões a receber, totalizando ")
            append(valueFormatted)
            append(".")
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.fragmentPaymentRecycler
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