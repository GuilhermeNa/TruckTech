package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.Customer
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentFreightEditorBinding
import br.com.apps.trucktech.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.util.MonetaryMaskUtil
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceSave
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceShow
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Frete"

class FreightEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentFreightEditorBinding? = null
    private val binding get() = _binding!!

    private val args: FreightEditorFragmentArgs by navArgs()
    private val vmData by lazy {
        FreightEVMData(
            masterUid = mainActVM.loggedUser.masterUid,
            truckId = mainActVM.loggedUser.truckId,
            travelId = args.travelId,
            driverId = mainActVM.loggedUser.driverId,
            freightId = args.freightId,
            commissionPercentual = mainActVM.loggedUser.commissionPercentual,
            permissionLevel = mainActVM.loggedUser.permissionLevelType
        )
    }
    private val viewModel: FreightEditorViewModel by viewModel { parametersOf(vmData) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFreightEditorBinding.inflate(inflater, container, false)
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
            fragFreightEditorWeight.run {
                addTextChangedListener(MonetaryMaskUtil(this))
            }

            fragFreightEditorValue.run {
                addTextChangedListener(MonetaryMaskUtil(this))
            }
        }
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentFreightEditorToolbar.toolbar,
            menuId = R.menu.menu_editor,
            toolbarTextView = binding.fragmentFreightEditorToolbar.toolbarText,
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
        binding.fragFreightEditorDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecione a data")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            val calendarPositiveListener = MaterialPickerOnPositiveButtonClickListener<Long> {
                viewModel.newDateHaveBeenSelected(it)
            }
            val calendarDismissListener = DialogInterface.OnDismissListener {
                binding.fragFreightEditorBlackLayer.visibility = View.GONE
                datePicker.clearOnPositiveButtonClickListeners()
                datePicker.clearOnDismissListeners()
            }

            datePicker.addOnPositiveButtonClickListener(calendarPositiveListener)
            datePicker.addOnDismissListener(calendarDismissListener)
            binding.fragFreightEditorBlackLayer.visibility = View.VISIBLE
            datePicker.show(childFragmentManager, "tag")

        }
    }

    /**
     * Try to save an [Freight].
     *  1. Validate the fields.
     *  2. Convert to DTO.
     *  3. Send DTO to be saved.
     */
    private fun saveIconClicked() {
        binding.apply {

            val customer = fragFreightEditorCustomerAc.text.toString()
            val origin = fragFreightEditorOrigin.text.toString()
            val destiny = fragFreightEditorDestiny.text.toString()
            val cargo = fragFreightEditorCargo.text.toString()
            val weight = fragFreightEditorWeight.text.toString()
            val value = fragFreightEditorValue.text.toString()

            var fieldsAreValid = true
            if (!viewModel.validateCustomer(customer)) {
                fragFreightEditorCustomerAc.error = "Preencha o cliente"
                fieldsAreValid = false
            }
            if (origin.isBlank()) {
                fragFreightEditorOrigin.error = "Preencha a cidade de origem"
                fieldsAreValid = false
            }
            if (destiny.isBlank()) {
                fragFreightEditorDestiny.error = "Preencha a cidade de destino"
                fieldsAreValid = false
            }
            if (cargo.isBlank()) {
                fragFreightEditorCargo.error = "Preencha a carga"
                fieldsAreValid = false
            }
            if (weight.isBlank()) {
                fragFreightEditorWeight.error = "Preencha o peso"
                fieldsAreValid = false
            }
            if (value.isBlank()) {
                fragFreightEditorValue.error = "Preencha o valor"
                fieldsAreValid = false
            }

            if (fieldsAreValid) {
                val viewDto = FreightDto(
                    customer = customer,
                    origin = origin,
                    destiny = destiny,
                    cargo = cargo,
                    weight = weight.formatPriceSave().toDouble(),
                    value = value.formatPriceSave().toDouble()
                )

                save(viewDto)

            }
        }
    }

    private fun save(viewDto: FreightDto) {
        viewModel.save(viewDto).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    response.exception.printStackTrace()
                    requireView().snackBarRed(FAILED_TO_SAVE)
                }

                is Response.Success -> requireView().apply {
                    snackBarGreen(SUCCESSFULLY_SAVED)
                    popBackStack()
                }
            }
        }

    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes date for screen changes.
     *
     *   - Observes freightData to bind [Freight] if the user is editing.
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            data.apply {
                initCustomerAutoComplete(customerList)
                freight?.let { bind(it) }
            }
        }

        viewModel.date.observe(viewLifecycleOwner) { localDate ->
            binding.fragFreightEditorDate.text = localDate.getCompleteDateInPtBr()
        }

    }

    private fun initCustomerAutoComplete(customerList: List<Customer>) {
        val customersNames = customerList.map { it.name }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            customersNames
        )

        val autoComplete = binding.fragFreightEditorCustomerAc
        autoComplete.setAdapter(adapter)

        autoComplete.setOnItemClickListener { _, _, _, _ -> autoComplete.error = null }

    }

    private fun bind(freight: Freight) {
        binding.apply {
            fragFreightEditorCustomerAc.setText(freight.customer?.name)
            fragFreightEditorOrigin.setText(freight.origin)
            fragFreightEditorDestiny.setText(freight.destiny)
            fragFreightEditorCargo.setText(freight.cargo)
            fragFreightEditorWeight.setText(freight.weight.formatPriceShow())
            fragFreightEditorValue.setText(freight.value.formatPriceShow())
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}