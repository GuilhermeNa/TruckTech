package br.com.apps.trucktech.ui.fragments.nav_home.refund

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.finance.Cost
import br.com.apps.trucktech.databinding.FragmentRefundBinding
import br.com.apps.trucktech.expressions.toBold
import br.com.apps.trucktech.expressions.toItalic
import br.com.apps.trucktech.expressions.toUnderline
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.public_adapters.ToReceiveRecyclerAdapter

private const val TOOLBAR_TITLE = "Reembolsos"

/**
 * A simple [Fragment] subclass.
 * Use the [RefundFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RefundFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRefundBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RefundFragmentViewModel by viewModels()

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
        _binding = FragmentRefundBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindText()
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

    private fun bindText() {
        val refunds = SpannableString("3")
            .toBold().toItalic().toUnderline()
        val value = SpannableString("R$ 210,00")
            .toBold().toItalic().toUnderline()

        binding.fragmentRefundDescription.text = buildSpannedString {
            append("Você tem ")
            append(refunds)
            append(" despesas a serem reembolsadas, totalizando ")
            append(value)
            append(".")
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.fragmentRefundRecycler
        val adapter = ToReceiveRecyclerAdapter(requireContext(), emptyList<Cost>())
        recyclerView.adapter = adapter

        val divider = DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}