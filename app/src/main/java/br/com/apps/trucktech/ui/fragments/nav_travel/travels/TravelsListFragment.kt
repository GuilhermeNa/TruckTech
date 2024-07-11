package br.com.apps.trucktech.ui.fragments.nav_travel.travels

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
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
import br.com.apps.trucktech.util.MonetaryMaskUtil
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceSave
import br.com.apps.trucktech.util.state.State
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
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
        stateHandler = TravelsListState(binding, lifecycleScope)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    /**
     * Init Fab for create a new Travel
     */
    private fun initFab() {
        binding.fragTravelFab.setOnClickListener {
            viewModel.setDialog(hasDialog = true)
            var keepDarkLayer = false

            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.icon_add_road)
                .setTitle("Nova viagem")
                .setMessage("Você confirma a adição de uma nova viagem?")
                .setPositiveButton("Ok") { _, _ ->
                    keepDarkLayer = true
                    launchCreatingNewTravel()
                }
                .setNegativeButton("Cancelar") { _, _ -> }
                .setOnDismissListener {
                    if (!keepDarkLayer)
                        viewModel.setDialog(hasDialog = false)
                }
                .create().apply {
                    window?.setGravity(Gravity.CENTER)
                    show()
                }

        }
    }

    private fun launchCreatingNewTravel() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {

                val measure = getLastTravelFinalOdometer() ?: showOdometerMeasureDialog()
                createNewTravel(measure)

            } catch (e: Exception) {
                requireView().snackBarRed(FAILED_TO_SAVE)
            } finally {
                viewModel.setDialog(hasDialog = false)
            }

        }
    }

    private fun getLastTravelFinalOdometer(): Double? {
        val travels = mainActVM.cachedTravels.value ?: return null
        return if (travels.isNotEmpty()) {
            travels.first().finalOdometerMeasurement?.toDouble()
        } else {
            null
        }
    }

    private suspend fun showOdometerMeasureDialog(): Double {
        val deferred = CompletableDeferred<Double>()

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_input_text, null)
        val field = view.findViewById<EditText>(R.id.dialog_edit_text)
        field.addTextChangedListener(MonetaryMaskUtil(field))

        MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .setIcon(R.drawable.icon_adjust)
            .setTitle("Quilometragem inicial")
            .setMessage("Defina a quilometragem inicial para esta viagem:")
            .setPositiveButton("Ok") { _, _ ->
                val text = field.text.toString()
                if (text.isNotBlank()) deferred.complete(text.formatPriceSave().toDouble())
                else deferred.completeExceptionally(InvalidParameterException())
            }
            .setNegativeButton("Cancelar") { _, _ ->
                deferred.completeExceptionally(InvalidParameterException())
            }
            .setOnCancelListener {
                deferred.completeExceptionally(InvalidParameterException())
            }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }

        return deferred.await()
    }

    private fun createNewTravel(odometerMeasurement: Double) {
        viewModel.createAndSave(odometerMeasurement).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    requireView().snackBarRed(FAILED_TO_SAVE)
                }

                is Response.Success -> {

                    requireView().snackBarGreen(SUCCESSFULLY_SAVED)
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
        viewLifecycleOwner.lifecycleScope.launch {
            mainActVM.cachedTravels.asFlow().collect { data ->
                viewModel.updateData(data)?.let { travels ->
                    initRecyclerView(travels)
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (viewModel.state.value != State.Loading) showAfterLoaded()
            when (state) {
                is State.Loading -> stateHandler?.showLoading()
                is State.Loaded -> stateHandler?.showLoaded()
                is State.Empty -> stateHandler?.showEmpty()
                is State.Error -> stateHandler?.showError(state.error)
            }
        }

        viewModel.dialog.observe(viewLifecycleOwner) { hasDialog ->
            when (hasDialog) {
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

    private fun showAfterLoaded() {
        binding.apply {
            boxGif.layout.apply {
                if (visibility == VISIBLE) {
                    visibility = GONE
                    animation = AnimationUtils.loadAnimation(
                        binding.root.context,
                        R.anim.fade_out_and_shrink
                    )
                }
            }

            lifecycleScope.launch {
                delay(250)

                fragTravelFab.apply {
                    if (visibility == GONE) {
                        visibility = VISIBLE
                        animation = AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.slide_in_from_bottom
                        )
                    }
                }

                fragmentTravelsToolbar.toolbar.apply {
                    if (visibility == GONE) {
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
            deleteClickListener = ::showDeleteDialog
        )
    )

    private fun showDeleteDialog(travel: Travel) {
        viewModel.setDialog(hasDialog = true)

        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle("Removendo viagem")
            .setMessage("Você realmente deseja remover esta viagem e todos os seus itens?")
            .setPositiveButton("Ok") { _, _ -> delete(travel) }
            .setNegativeButton("Cancelar") { _, _ -> }
            .setOnDismissListener { viewModel.setDialog(hasDialog = false) }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun delete(travel: Travel) {
        stateHandler?.showDeleting()

        lifecycleScope.launch {
            viewModel.delete(travel).observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Success -> {
                        stateHandler?.showDeleted()
                        requireView().snackBarOrange(SUCCESSFULLY_REMOVED)
                    }

                    is Response.Error -> {
                        requireView().snackBarRed(FAILED_TO_REMOVE)
                    }
                }
            }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        stateHandler = null
        _binding = null
    }

}
