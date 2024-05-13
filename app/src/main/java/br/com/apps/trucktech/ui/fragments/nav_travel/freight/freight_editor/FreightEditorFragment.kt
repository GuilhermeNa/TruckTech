package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import br.com.apps.model.IdHolder
import br.com.apps.model.factory.FreightFactory
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.FAILED_TO_SAVE
import br.com.apps.repository.Response
import br.com.apps.repository.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentFreightEditorBinding
import br.com.apps.trucktech.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Frete"

class FreightEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentFreightEditorBinding? = null
    private val binding get() = _binding!!

    private val args: FreightEditorFragmentArgs by navArgs()
    private val idHolder by lazy {
        IdHolder(
            masterUid = sharedViewModel.userData.value?.user?.masterUid,
            truckId = sharedViewModel.userData.value?.truck?.id,
            travelId = args.travelId,
            freightId = args.freightId,
            driverId = sharedViewModel.userData.value?.user?.employeeId
        )
    }
    private val viewModel: FreightEditorViewModel by viewModel { parametersOf(idHolder) }

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
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
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

            cleanEditTextError(
                fragFreightEditorCompany,
                fragFreightEditorOrigin,
                fragFreightEditorDestiny,
                fragFreightEditorCargo,
                fragFreightEditorWeight,
                fragFreightEditorValue
            )

            val company = fragFreightEditorCompany.text.toString()
            val origin = fragFreightEditorOrigin.text.toString()
            val destiny = fragFreightEditorDestiny.text.toString()
            val cargo = fragFreightEditorCargo.text.toString()
            val weight = fragFreightEditorWeight.text.toString()
            val value = fragFreightEditorValue.text.toString()

            var fieldsAreValid = true
            if (company.isBlank()) {
                fragFreightEditorCompany.error = "Preencha o carregador"
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
                val mappedFields = hashMapOf(
                    Pair(FreightFactory.TAG_ORIGIN, origin),
                    Pair(FreightFactory.TAG_COMPANY, company),
                    Pair(FreightFactory.TAG_DESTINY, destiny),
                    Pair(FreightFactory.TAG_CARGO, cargo),
                    Pair(FreightFactory.TAG_WEIGHT, weight),
                    Pair(FreightFactory.TAG_VALUE, value),
                    Pair(FreightFactory.TAG_LOADING_DATE, viewModel.date.value.toString())
                )

                save(mappedFields)

            }

        }
    }

    private fun save(mappedFields: HashMap<String, String>) {
        viewModel.save(mappedFields).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    response.exception.printStackTrace()
                    requireView().snackBarRed(FAILED_TO_SAVE)
                }
                is Response.Success -> requireView().apply {
                    clearMenu()
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
        viewModel.freightData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                is Response.Success -> bind()
            }
        }

        viewModel.date.observe(viewLifecycleOwner) { localDate ->
            binding.fragFreightEditorDate.text = localDate.getCompleteDateInPtBr()
        }
    }

    private fun bind() {
        val freight = viewModel.freight
        binding.apply {
            fragFreightEditorCompany.setText(freight.company)
            fragFreightEditorOrigin.setText(freight.origin)
            fragFreightEditorDestiny.setText(freight.destiny)
            fragFreightEditorCargo.setText(freight.cargo)
            fragFreightEditorWeight.setText(freight.weight?.toPlainString())
            fragFreightEditorValue.setText(freight.value?.toPlainString())
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