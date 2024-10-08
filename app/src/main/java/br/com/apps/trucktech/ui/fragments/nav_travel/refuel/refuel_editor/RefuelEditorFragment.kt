package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_editor

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.expressions.getCompleteDateInPtBr
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.util.toDate
import br.com.apps.repository.util.CONNECTION_FAILURE
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRefuelEditorBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.util.DeviceCapabilities
import br.com.apps.trucktech.util.MonetaryMaskUtil
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceSave
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceShow
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Abastecimento"

class RefuelEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRefuelEditorBinding? = null
    private val binding get() = _binding!!

    private val args: RefuelEditorFragmentArgs by navArgs()
    private val vmData by lazy {
        RefuelEVMData(
            masterUid = mainActVM.loggedUser.masterUid,
            truckId = mainActVM.loggedUser.truckId,
            travelId = args.travelId,
            driverId = mainActVM.loggedUser.driverId,
            refuelId = args.refuelId,
            permission = mainActVM.loggedUser.accessLevel
        )
    }
    private val viewModel: RefuelEditorViewModel by viewModel { parametersOf(vmData) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRefuelEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initDateViewClickListener()
        initTextWatcher()
    }

    private fun initTextWatcher() {
        binding.apply {
            fragRefuelEditorOdometer.run {
                addTextChangedListener(MonetaryMaskUtil(this))
            }

            fragRefuelEditorAmountLiters.run {
                addTextChangedListener(MonetaryMaskUtil(this))
            }

            fragRefuelEditorValuePerLiter.run {
                addTextChangedListener(MonetaryMaskUtil(this))
            }

            fragRefuelEditorTotalValue.run {
                addTextChangedListener(MonetaryMaskUtil(this))
            }

        }
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentRefuelEditorToolbar.toolbar,
            menuId = R.menu.menu_editor,
            toolbarTextView = binding.fragmentRefuelEditorToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    override fun initMenuClickListeners(toolbar: Toolbar) {
        super.initMenuClickListeners(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_editor_save -> {
                    saveIconClicked()
                    true
                }

                else -> false
            }
        }
    }

    /**
     * Initialize the Date picker.
     */
    private fun initDateViewClickListener() {
        binding.fragRefuelEditorDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecione a data")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            val calendarPositiveListener = MaterialPickerOnPositiveButtonClickListener<Long> {
                viewModel.newDateHaveBeenSelected(it)
            }
            val calendarDismissListener = DialogInterface.OnDismissListener {
                binding.fragRefuelEditorBlackLayer.visibility = GONE
                datePicker.clearOnPositiveButtonClickListeners()
                datePicker.clearOnDismissListeners()
            }

            datePicker.addOnPositiveButtonClickListener(calendarPositiveListener)
            datePicker.addOnDismissListener(calendarDismissListener)
            binding.fragRefuelEditorBlackLayer.visibility = VISIBLE
            datePicker.show(childFragmentManager, "tag")

        }
    }

    /**
     * Try to save an [Refuel].
     *  1. Validate the fields.
     *  2. Convert to DTO.
     *  3. Send DTO to be saved.
     */
    private fun saveIconClicked() {
        binding.apply {

            val station = fragRefuelEditorStation.text.toString()
            val odometer = fragRefuelEditorOdometer.text.toString()
            val amountLiters = fragRefuelEditorAmountLiters.text.toString()
            val valuePerLiter = fragRefuelEditorValuePerLiter.text.toString()
            val totalValue = fragRefuelEditorTotalValue.text.toString()
            val isComplete = fragRefuelEditorCheckbox.isChecked

            var fieldsAreValid = true
            if (station.isBlank()) {
                fragRefuelEditorStation.error = "Preencha o posto"
                fieldsAreValid = false
            }
            if (odometer.isBlank()) {
                fragRefuelEditorOdometer.error = "Preencha a quilometragem"
                fieldsAreValid = false
            }
            if (amountLiters.isBlank()) {
                fragRefuelEditorAmountLiters.error = "Preencha a quantidade de litros"
                fieldsAreValid = false
            }
            if (valuePerLiter.isBlank()) {
                fragRefuelEditorValuePerLiter.error = "Preencha o valor do litro"
                fieldsAreValid = false
            }
            if (totalValue.isBlank()) {
                fragRefuelEditorTotalValue.error = "Preencha o valor total"
                fieldsAreValid = false
            }

            if (fieldsAreValid) {
                val viewDto = RefuelDto(
                    date = viewModel.date.value!!.toDate(),
                    station = station,
                    odometerMeasure = odometer.formatPriceSave().toDouble(),
                    amountLiters = amountLiters.formatPriceSave().toDouble(),
                    valuePerLiter = valuePerLiter.formatPriceSave().toDouble(),
                    totalValue = totalValue.formatPriceSave().toDouble(),
                    isCompleteRefuel = isComplete
                )
                save(viewDto)
            }
        }
    }

    private fun save(viewDto: RefuelDto) {
        DeviceCapabilities.hasNetworkConnection(requireContext()).let { isConnected ->
            when (isConnected) {
                true -> {
                    viewModel.save(viewDto).observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is Response.Error -> {
                                requireView().snackBarRed(FAILED_TO_SAVE)
                                Log.e(TAG_DEBUG, response.exception.message.toString())
                            }

                            is Response.Success -> {
                                requireView().snackBarGreen(SUCCESSFULLY_SAVED)
                                requireView().popBackStack()
                            }

                        }
                    }
                }

                false -> {
                    requireView().snackBarRed(CONNECTION_FAILURE)
                }
            }
        }
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes date for screen changes.
     *
     *   - Observes refuelData to bind [Refuel] if the user is editing.
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                is Response.Success -> response.data?.let { bind(it) }
            }
        }

        viewModel.date.observe(viewLifecycleOwner) { localDate ->
            binding.fragRefuelEditorDate.text = localDate.getCompleteDateInPtBr()
        }
    }

    private fun bind(refuel: Refuel) {
        binding.apply {
            fragRefuelEditorStation.setText(refuel.station)
            fragRefuelEditorOdometer.setText(refuel.odometerMeasure.formatPriceShow())
            fragRefuelEditorAmountLiters.setText(refuel.amountLiters.formatPriceShow())
            fragRefuelEditorValuePerLiter.setText(refuel.valuePerLiter.formatPriceShow())
            fragRefuelEditorTotalValue.setText(refuel.totalValue.formatPriceShow())
            refuel.isCompleteRefuel.let { fragRefuelEditorCheckbox.isChecked = it }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}




