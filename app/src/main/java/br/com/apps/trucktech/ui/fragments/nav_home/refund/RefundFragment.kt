package br.com.apps.trucktech.ui.fragments.nav_home.refund

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.model.model.travel.Outlay
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.trucktech.databinding.FragmentRefundBinding
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.expressions.toBold
import br.com.apps.trucktech.expressions.toUnderline
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.public_adapters.ToReceiveRecyclerAdapter

private const val TOOLBAR_TITLE = "Reembolsos"

class RefundFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRefundBinding? = null
    private val binding get() = _binding!!
    private var adapter: ToReceiveRecyclerAdapter<Outlay>? = null

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
        initRecyclerView()
        mainActVM.cachedOutlayToPay.observe(viewLifecycleOwner) { data ->
            when {
                data == null -> {
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                    binding.layout.visibility = View.GONE
                    binding.layoutError.apply {
                        layout.visibility = View.VISIBLE
                        error.visibility = View.VISIBLE
                        empty.visibility = View.GONE
                    }
                }

                data.isEmpty() -> {
                    binding.apply {
                        layout.visibility = View.GONE
                        layoutError.apply {
                            layout.visibility = View.VISIBLE
                            error.visibility = View.GONE
                            empty.visibility = View.VISIBLE
                        }
                    }
                }

                else -> {
                    bindText(data)
                    adapter?.update(data)
                    binding.apply {
                        layout.visibility = View.VISIBLE
                        layoutError.apply {
                            layout.visibility = View.GONE
                            error.visibility = View.GONE
                            empty.visibility = View.GONE
                        }
                    }
                }

            }
        }

    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentRefundToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentRefundToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    private fun bindText(dataSet: List<Outlay>) {
        val numberOfExpends = dataSet.size
        val totalExpends = dataSet.sumOf { it.value }

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
        adapter = ToReceiveRecyclerAdapter(requireContext())
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