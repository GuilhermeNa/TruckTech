package br.com.apps.trucktech.ui.fragments.nav_travel.travels

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.FAILED_TO_REMOVE
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_REMOVED
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentTravelsBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.navigateWithSafeArgs
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.activities.main.VisualComponents
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_travel.travels.private_adapters.TravelsListRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.DateRecyclerAdapter
import br.com.apps.trucktech.util.state.State
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.security.InvalidParameterException

private const val TOOLBAR_TITLE = "Viagens"

/**
 * A simple [Fragment] subclass.
 * Use the [TravelsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TravelsListFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentTravelsBinding? = null
    private val binding get() = _binding!!
    private var stateHandler: TravelsListState? = null

    private val vmData by lazy {
        TravelLVMData(
            masterUid = mainActVM.loggedUser.masterUid,
            driverId = mainActVM.loggedUser.driverId,
            truckId = mainActVM.loggedUser.truckId
        )
    }
    private val viewModel: TravelsListViewModel by viewModel { parametersOf(vmData) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateHandler = TravelsListState(binding)
        initSwipeRefresh()
        initStateManager()
        initFab()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentTravelsToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentTravelsToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadData()
        }
    }

    /**
     * Init Fab for create a new Travel
     */
    private fun initFab() {
        binding.fragTravelFab.setOnClickListener {
            viewModel.dialogRequested()

            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.icon_logout)
                .setTitle("Nova viagem")
                .setMessage("Você confirma a adição de uma nova viagem?")
                .setPositiveButton("Ok") { _, _ -> createNewTravel() }
                .setNegativeButton("Cancelar") { _, _ -> }
                .setOnDismissListener { viewModel.dialogDismissed() }
                .create().apply {
                    window?.setGravity(Gravity.CENTER)
                    show()
                }
        }
    }

    private fun createNewTravel() {
        viewModel.createAndSave().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    requireView().snackBarRed(FAILED_TO_SAVE)
                    response.exception.printStackTrace()
                }

                is Response.Success -> {
                    requireView().snackBarGreen(SUCCESSFULLY_SAVED)
                    viewModel.loadData()
                }
            }
        }
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes travelData for update the recyclerView.
     *   - Observes darkLayer to manage the interaction.
     *   - Observes bottomNav to manage the interaction.
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            binding.swipeRefresh.isRefreshing = false
            data?.let { initRecyclerView(it) }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                State.Loading -> stateHandler?.showLoading()
                State.Loaded -> stateHandler?.showLoaded()
                State.Empty -> stateHandler?.showEmpty()
                is State.Error -> stateHandler?.showError(state.error)
                State.Deleting -> stateHandler?.showDeleting()
                State.Deleted -> stateHandler?.showDeleted()
                else -> {}
            }
        }

        viewModel.dialog.observe(viewLifecycleOwner) { dialog ->
            when (dialog) {
                true -> {
                    binding.fragTravelListDarkLayer.visibility = VISIBLE
                    mainActVM.setComponents(VisualComponents(hasBottomNavigation = false))
                }

                false -> {
                    binding.fragTravelListDarkLayer.visibility = GONE
                    mainActVM.setComponents(VisualComponents(hasBottomNavigation = true))
                }
            }
        }
    }

    /**
     * Initialize the RecyclerView.
     *
     * Options:
     *  - clickListener -> navigation
     *  - context menu -> delete
     */
    private fun initRecyclerView(dataSet: List<Travel>) {
        initConcatAdapter(initAdaptersData(dataSet))
    }

    private fun initConcatAdapter(adapters: List<RecyclerView.Adapter<out RecyclerView.ViewHolder>>) {
        val concatAdapter = ConcatAdapter(adapters)
        val recyclerView = binding.travelsFragmentRecycler
        recyclerView.adapter = concatAdapter
    }

    private fun initAdaptersData(dataSet: List<Travel>): List<RecyclerView.Adapter<out RecyclerView.ViewHolder>> {
        return dataSet
            .sortedBy { it.initialDate }
            .reversed()
            .groupBy {
                it.initialDate?.getMonthAndYearInPtBr()
                    ?: throw InvalidParameterException("Date is null")
            }
            .map { createAdapters(it) }
            .flatten()
    }

    private fun createAdapters(itemsMap: Map.Entry<String, List<Travel>>) = listOf(
        DateRecyclerAdapter(requireContext(), listOf(itemsMap.key)),
        TravelsListRecyclerAdapter(
            requireContext(),
            itemsMap.value,
            itemCLickListener = {
                requireView().navigateWithSafeArgs(
                    TravelsListFragmentDirections.actionTravelsFragmentToTravelPreviewFragment(
                        it
                    )
                )
            },
            deleteClickListener = ::showAlertDialog
        )
    )

    private fun showAlertDialog(travel: Travel) {
        viewModel.dialogRequested()

        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle("Removendo viagem")
            .setMessage("Você realmente deseja remover esta viagem e todos os seus itens?")
            .setPositiveButton("Ok") { _, _ -> delete(travel) }
            .setNegativeButton("Cancelar") { _, _ -> }
            .setOnDismissListener { viewModel.dialogDismissed() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun delete(travel: Travel) {
        lifecycleScope.launch {
            viewModel.delete(travel).observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Success -> {
                        requireView().snackBarOrange(SUCCESSFULLY_REMOVED)
                        viewModel.loadData()
                    }

                    is Response.Error -> {
                        requireView().snackBarRed(FAILED_TO_REMOVE)
                    }
                }
            }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON RESUME
    //---------------------------------------------------------------------------------------------//

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        binding.swipeRefresh.isRefreshing = false
        stateHandler = null
        _binding = null
    }

}