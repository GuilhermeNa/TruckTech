package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentBankBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.private_adapters.BankFragmentAdapter
import br.com.apps.trucktech.util.State
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Dados Bancários"

/**
 * A simple [Fragment] subclass.
 * Use the [BankListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BankListFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentBankBinding? = null
    private val binding get() = _binding!!

    private val employeeId by lazy {
        mainActVM.loggedUser.driverId
    }
    private val viewModel: BankListFragmentViewModel by viewModel { parametersOf(employeeId) }
    private var adapter: BankFragmentAdapter? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBankBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initStateManager()
        initFab()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentBankToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentBankToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Init recycler view
     */
    private fun initRecyclerView() {
        val recyclerView = binding.fragmentBankRecycler
        adapter = BankFragmentAdapter(
            requireContext(),
            emptyList(),
            clickListener = { id ->
                requireView().navigateTo(
                    BankListFragmentDirections.actionBankFragmentToBankPreviewFragment(id)
                )
            },
            defineNewMainAccount = { newAccId ->
                lifecycleScope.launch {
                    viewModel.updateMainAccount(newAccId)
                }
            }
        )
        recyclerView.adapter = adapter
    }

    /**
     * Init state manager
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> Log.d(TAG_DEBUG, response.exception.message.toString())
                is Response.Success -> {
                    response.data?.let { dataSet ->
                        adapter?.update(dataSet)
                    }
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is State.Loading -> {
                    binding.apply {
                        fragmentBankRecycler.visibility = GONE
                        fragBankBoxEmpty.layout.visibility = GONE
                        fragBankBoxEmpty.error.visibility = GONE
                        fragBankBoxEmpty.empty.visibility = GONE
                    }
                }
                is State.Loaded -> {
                    binding.apply {
                        fragmentBankRecycler.visibility = VISIBLE
                        fragBankBoxEmpty.layout.visibility = GONE
                        fragBankBoxEmpty.error.visibility = GONE
                        fragBankBoxEmpty.empty.visibility = GONE
                    }
                }
                is State.Empty -> {
                    binding.apply {
                        fragmentBankRecycler.visibility = GONE
                        fragBankBoxEmpty.layout.visibility = VISIBLE
                        fragBankBoxEmpty.error.visibility = GONE
                        fragBankBoxEmpty.empty.visibility = VISIBLE
                    }
                }
                is State.Error -> {
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                    binding.apply {
                        fragmentBankRecycler.visibility = GONE
                        fragBankBoxEmpty.layout.visibility = VISIBLE
                        fragBankBoxEmpty.error.visibility = VISIBLE
                        fragBankBoxEmpty.empty.visibility = GONE
                    }
                }
            }
        }

    }

    /**
     * Floating action button
     */
    private fun initFab() {
        binding.fragBankListFab.setOnClickListener {
            it.navigateTo(BankListFragmentDirections.actionBankFragmentToBankEditorFragment(null))
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

}