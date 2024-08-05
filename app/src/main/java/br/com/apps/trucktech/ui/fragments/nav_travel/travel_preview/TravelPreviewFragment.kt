package br.com.apps.trucktech.ui.fragments.nav_travel.travel_preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentTravelPreviewBinding
import br.com.apps.trucktech.expressions.getColorById
import br.com.apps.trucktech.expressions.getColorStateListById
import br.com.apps.model.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.model.expressions.toNumberDecimalPtBr
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val FAILED_WHEN_FINISHING = "Falha ao finalizar viagem"

private const val SUCCESS_WHEN_FINISHING = "Sucesso ao finalizar viagem"


/**
 * A simple [Fragment] subclass.
 * Use the [TravelPreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TravelPreviewFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentTravelPreviewBinding? = null
    private val binding get() = _binding!!
    private var stateHandler: TravelPreviewStateHandler? = null

    private val args: TravelPreviewFragmentArgs by navArgs()
    private val vmData: TravelPreviewVmData by lazy {
        TravelPreviewVmData(
            travelId = args.travelId,
            permission = mainActVM.loggedUser.accessLevel
        )
    }
    private val viewModel: TravelPreviewViewModel by viewModel { parametersOf(vmData) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelPreviewBinding.inflate(inflater, container, false)
        stateHandler = TravelPreviewStateHandler(binding)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAppBarManager()
        initSwipeRefresh()
        initStateManager()
        initFab()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.toolbar,
            menuId = R.menu.menu_travel_preview,
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    override fun initMenuClickListeners(toolbar: Toolbar) {
        super.initMenuClickListeners(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_travel_preview_options -> {
                    requireView().navigateTo(
                        TravelPreviewFragmentDirections
                            .actionTravelPreviewFragmentToRecordsFragment(args.travelId)
                    )
                    true
                }

                else -> false
            }
        }

    }

    /**
     * Init Swipe Refresh
     */
    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.loadData()
            }
        }
    }

    /**
     * Init Fab
     */
    private fun initFab() {
        binding.boxHeader.fab.setOnClickListener {
            viewModel.endTravel().observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Error -> {
                        response.exception.printStackTrace()
                        requireView().snackBarRed(response.exception.message ?: UNKNOWN_EXCEPTION)
                    }

                    is Response.Success -> {
                        requireView().snackBarGreen(SUCCESS_WHEN_FINISHING)
                    }
                }
            }
        }
    }

    /**
     * Init AppBar
     */
    private fun initToolbar() {
        binding.apply {

            toolbar.apply {
                setNavigationIcon(R.drawable.icon_back)
                navigationIcon?.setTint(context.getColorById(R.color.white))

                menu?.run {
                    findItem(R.id.menu_travel_preview_options).iconTintList =
                        requireContext().getColorStateListById(R.color.white)
                }
            }

            collapsingToolbar.run {
                contentScrim =
                    ContextCompat.getDrawable(requireContext(), R.drawable.shape_bg_gradient_orange)
            }

        }
    }

    /**
     * Init State Manager
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            data?.let { t -> bind(t) }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            resetSwipeRefresh()
            when (state) {
                is StateTP.Loading -> {
                    viewModel.setAppBarState(StateAppBarTP.IsDisabled)
                    stateHandler?.showLoading()
                }

                is StateTP.Error -> {
                    viewModel.setAppBarState(StateAppBarTP.IsDisabled)
                    stateHandler?.showError(state.error)
                }

                is StateTP.Empty -> {
                    viewModel.setAppBarState(StateAppBarTP.IsDisabled)
                    stateHandler?.showEmpty()
                }

                is StateTP.Loaded -> {
                    viewModel.appBarState.value?.let { appBarState ->
                        if (appBarState is StateAppBarTP.IsDisabled) {
                            viewModel.setAppBarState(StateAppBarTP.Prepare)
                        }
                    }

                    stateHandler?.showLoaded()
                    when (state) {
                        StateTP.Loaded.AlreadyFinished -> stateHandler?.showWhenFinished()
                        StateTP.Loaded.ReadyForAuth -> stateHandler?.showWhenAlreadyAuthenticated()
                        StateTP.Loaded.AwaitingAuth -> stateHandler?.showWhenAwaitingAuthentication()
                    }

                }

                is StateTP.Finishing -> stateHandler?.showFinishing()

            }
        }

        viewModel.appBarState.observe(viewLifecycleOwner) { appBarState ->
            when (appBarState) {
                is StateAppBarTP.IsDisabled -> {
                    binding.apply {
                        appBar.setExpanded(false)
                        swipeRefresh.isEnabled = true
                    }
                }

                is StateAppBarTP.Prepare -> {
                    binding.appBar.setExpanded(true)
                    viewModel.setAppBarState(StateAppBarTP.IsEnabled.Expanded)
                }

                is StateAppBarTP.IsEnabled -> {
                    when (appBarState) {
                        is StateAppBarTP.IsEnabled.Contracted -> {
                            stateHandler?.hideHeader()
                            binding.swipeRefresh.isEnabled = false
                        }

                        is StateAppBarTP.IsEnabled.Expanded -> {
                            stateHandler?.showHeader()
                            binding.swipeRefresh.isEnabled = true
                        }

                    }
                }

            }
        }

    }

    private fun initAppBarManager() {
        binding.appBar.run {
            var scrollRange = -1
            var isExpanded = viewModel.appBarState.value == StateAppBarTP.IsEnabled.Expanded

            addOnOffsetChangedListener { appBar, yOffSet ->
                if (viewModel.appBarState.value is StateAppBarTP.IsEnabled) {

                    if (scrollRange == -1) {
                        //The first time the view is loaded, the AppBar is 25.6% larger and needs
                        // to be calculated; from the second time onward, it behaves normally.
                        scrollRange = if (viewModel.isFirstBoot) {
                            val range = appBar.totalScrollRange.toDouble()
                            val rangeDifference = range * (0.256)
                            val correctRange = (range - rangeDifference).toInt()
                            -correctRange

                        } else -appBar.totalScrollRange
                    }

                    when {
                        (yOffSet > scrollRange && !isExpanded) -> {
                            viewModel.setAppBarState(StateAppBarTP.IsEnabled.Expanded)
                            isExpanded = true
                        }

                        (yOffSet == scrollRange && isExpanded) -> {
                            viewModel.setAppBarState(StateAppBarTP.IsEnabled.Contracted)
                            isExpanded = false
                        }
                    }
                }
            }
        }
    }

    private fun resetSwipeRefresh() {
        binding.swipeRefresh.apply {
            if (isRefreshing) isRefreshing = false
        }
    }

    private fun bind(t: Travel) {
        binding.apply {

            //DATE
            boxDate.apply {
                boxTravelPreviewInitialDate.text = t.initialDate.getCompleteDateInPtBr()
                boxTravelPreviewFinalDate.text = t.finalDate?.getCompleteDateInPtBr() ?: ""
            }

            //ODOMETER
            boxOdometer.apply {
                boxTravelPreviewInitialMeasure.text =
                    t.initialOdometerMeasurement.toNumberDecimalPtBr() + " - km"
                boxTravelPreviewFinalMeasure.apply {
                    visibility =
                        if (t.finalOdometerMeasurement == null) {
                            GONE
                        } else {
                            text = t.finalOdometerMeasurement!!.toNumberDecimalPtBr() + " - km"
                            VISIBLE
                        }
                }
            }

            //HEADER
            boxHeader.apply {
                val percent = t.getTravelAuthenticationPercent().toInt()
                balance.text = t.getLiquidValue().toCurrencyPtBr()
                progressBar.progress = percent
                progressValue.text = "$percent% completo"
            }

            //AIDS
            boxAid.apply {
                amountField.text = t.getListSize(Travel.AID).toString()
                valueField.text = t.getListTotalValue(Travel.AID).toCurrencyPtBr()
            }

            //FREIGHTS
            boxFreight.apply {
                amountField.text = t.getListSize(Travel.FREIGHT).toString()
                valueField.text = t.getListTotalValue(Travel.FREIGHT).toCurrencyPtBr()
                commissionField.text = t.getCommissionValue().toCurrencyPtBr()
            }

            //REFUELS
            boxRefuel.apply {
                amountField.text = t.getListSize(Travel.REFUEL).toString()
                valueField.text = t.getListTotalValue(Travel.REFUEL).toCurrencyPtBr()
            }

            //EXPENDS
            boxExpend.apply {
                amountField.text = t.getListSize(Travel.EXPEND).toString()
                valueField.text = t.getListTotalValue(Travel.EXPEND).toCurrencyPtBr()
            }

        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON RESUME
    //---------------------------------------------------------------------------------------------//

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
        if (viewModel.appBarState.value == StateAppBarTP.IsEnabled.Contracted) {
            binding.appBar.setExpanded(false)
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
