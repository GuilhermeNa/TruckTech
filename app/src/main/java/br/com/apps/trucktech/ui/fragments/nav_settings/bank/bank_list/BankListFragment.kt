package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.com.apps.repository.util.FAILED_TO_LOAD_BANKS
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentBankBinding
import br.com.apps.trucktech.exceptions.NoBanksFoundException
import br.com.apps.trucktech.expressions.loadGif
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.private_adapters.BankFragmentAdapter
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.delay
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
        initStateManager()
        initRecyclerView()
        initFab()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
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
            context = requireContext(),
            _clickedPos = viewModel.adapterClickedPos,
            data = viewModel.data.value ?: BankLFData(emptyList(), emptyList()),
            clickListener = { id, clickedPos ->
                viewModel.setAdapterPos(clickedPos)
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
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state != State.Loading) showAfterLoading()

            when (state) {
                is State.Loading -> {
                    binding.apply {
                        boxGif.loadingGif.loadGif(R.drawable.gif_bank, requireContext())
                        fragmentBankRecycler.visibility = GONE
                        fragBankListFab.visibility = GONE
                        fragmentBankToolbar.toolbar.visibility = GONE
                    }
                }

                is State.Loaded -> {
                    binding.apply {
                        fragBankBoxEmpty.apply {
                            if (layout.visibility == VISIBLE) {
                                layout.visibility = GONE
                                error.visibility = GONE
                                empty.visibility = GONE
                            }
                        }
                        fragmentBankRecycler.apply {
                            if (this.visibility == GONE) {
                                val layoutAnim = AnimationUtils.loadLayoutAnimation(
                                    requireContext(),
                                    R.anim.layout_controller_animation_slide_in_left
                                )
                                lifecycleScope.launch {
                                    delay(500)
                                    visibility = VISIBLE
                                    layoutAnimation = layoutAnim
                                }
                            }
                        }
                    }
                }

                is State.Empty -> {
                    binding.apply {
                        fragmentBankRecycler.visibility = GONE
                        fragBankBoxEmpty.apply {
                            layout.visibility = VISIBLE
                            error.visibility = GONE
                            empty.visibility = VISIBLE
                        }
                    }
                }

                is State.Error -> {
                    val message = when (state.error) {
                        is NoBanksFoundException -> FAILED_TO_LOAD_BANKS
                        else -> UNKNOWN_EXCEPTION
                    }
                    requireView().snackBarRed(message)
                    binding.apply {
                        fragmentBankRecycler.visibility = GONE
                        fragBankBoxEmpty.apply {
                            layout.visibility = VISIBLE
                            error.visibility = VISIBLE
                            empty.visibility = GONE
                        }
                    }
                }

                else -> {}
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { data ->
            data?.let {
                if (viewModel.state.value == State.Loaded) {
                    val itemRemoved = it.bankAccList.size < adapter!!.data.size
                    val itemAdded = it.bankAccList.size > adapter!!.data.size

                    when {
                        itemRemoved -> adapter?.remove()

                        itemAdded -> {
                            val item =
                                it.bankAccList.subtract(adapter!!.data.toSet()).singleOrNull()
                            item?.let { account -> adapter?.add(account) }
                        }

                        else -> adapter?.update(it)
                    }

                } else {
                    adapter?.update(it)
                }
            }
        }

    }

    private fun showAfterLoading() {
        binding.apply {
            boxGif.layout.apply {
                if (this.visibility == VISIBLE) {
                    visibility = GONE
                    animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_and_shrink)
                }
            }

            lifecycleScope.launch {
                delay(250)

                fragBankListFab.apply {
                    if (this.visibility == GONE) {
                        visibility = VISIBLE
                        animation = AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.slide_in_from_bottom
                        )
                    }
                }

                fragmentBankToolbar.toolbar.apply {
                    if (this.visibility == GONE) {
                        visibility = VISIBLE
                        animation = AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.slide_in_from_top
                        )
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