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
import br.com.apps.model.IdHolder
import br.com.apps.model.factory.RefuelFactory
import br.com.apps.model.model.travel.Refuel
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.FAILED_TO_SAVE
import br.com.apps.repository.Response
import br.com.apps.repository.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentRefuelEditorBinding
import br.com.apps.trucktech.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Abastecimento"

class RefuelEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRefuelEditorBinding? = null
    private val binding get() = _binding!!

    private val args: RefuelEditorFragmentArgs by navArgs()
    private val idHolder by lazy {
        IdHolder(
            masterUid = sharedViewModel.userData.value?.user?.masterUid,
            truckId = sharedViewModel.userData.value?.truck?.id,
            travelId = args.travelId,
            refuelId = args.refuelId,
            driverId = sharedViewModel.userData.value?.user?.employeeId
        )
    }
    private val viewModel: RefuelFragmentViewModel by viewModel { parametersOf(idHolder) }

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
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
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

            cleanEditTextError(
                fragRefuelEditorStation,
                fragRefuelEditorOdometer,
                fragRefuelEditorAmountLiters,
                fragRefuelEditorValuePerLiter,
                fragRefuelEditorTotalValue
            )

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
                val mappedFields = hashMapOf(
                    Pair(RefuelFactory.TAG_DATE, viewModel.date.value.toString()),
                    Pair(RefuelFactory.TAG_STATION, station),
                    Pair(RefuelFactory.TAG_ODOMETER, odometer),
                    Pair(RefuelFactory.TAG_AMOUNT_LITERS, amountLiters),
                    Pair(RefuelFactory.TAG_VALUE_PER_LITER, valuePerLiter),
                    Pair(RefuelFactory.TAG_TOTAL_VALUE, totalValue),
                    Pair(RefuelFactory.TAG_IS_COMPLETE, isComplete.toString())
                )
                save(mappedFields)
            }
        }
    }

    private fun save(mappedFields: HashMap<String, String>) {
        viewModel.save(mappedFields).observe(viewLifecycleOwner) { response ->
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

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes date for screen changes.
     *
     *   - Observes refuelData to bind [Refuel] if the user is editing.
     */
    private fun initStateManager() {
        viewModel.refuelData.observe(viewLifecycleOwner) { response ->
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
            fragRefuelEditorOdometer.setText(refuel.odometerMeasure?.toPlainString())
            fragRefuelEditorAmountLiters.setText(refuel.amountLiters?.toPlainString())
            fragRefuelEditorValuePerLiter.setText(refuel.valuePerLiter?.toPlainString())
            fragRefuelEditorTotalValue.setText(refuel.totalValue?.toPlainString())
            refuel.isCompleteRefuel?.let { fragRefuelEditorCheckbox.isChecked = it }
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




