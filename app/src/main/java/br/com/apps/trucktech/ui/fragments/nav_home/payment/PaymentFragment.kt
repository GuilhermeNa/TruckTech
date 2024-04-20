package br.com.apps.trucktech.ui.fragments.nav_home.payment

import android.os.Bundle
import android.text.SpannableString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.databinding.FragmentPaymentBinding
import br.com.apps.trucktech.expressions.toBold
import br.com.apps.trucktech.expressions.toItalic
import br.com.apps.trucktech.expressions.toUnderline
import br.com.apps.trucktech.sampleFreightForCommission
import br.com.apps.trucktech.ui.public_adapters.ToReceiveRecyclerAdapter
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar

private const val TOOLBAR_TITLE = "Pagamentos"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentFragmentViewModel by viewModels()

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
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
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
            toolbar = binding.fragmentPaymentToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentPaymentToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    private fun bindText() {
        val commissionsFormatted = SpannableString("3")
            .toBold().toItalic().toUnderline()
        val value = SpannableString("R$ 1.580,00")
            .toBold().toItalic().toUnderline()

        binding.fragmentPaymentDescription.text = buildSpannedString {
            append("Você tem ")
            append(commissionsFormatted)
            append(" comissões a receber, totalizando ")
            append(value)
            append(".")
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.fragmentPaymentRecycler
        val adapter = ToReceiveRecyclerAdapter(context = requireContext(), dataSet = sampleFreightForCommission)
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