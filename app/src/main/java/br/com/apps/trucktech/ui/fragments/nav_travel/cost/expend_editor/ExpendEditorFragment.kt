package br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_editor

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import br.com.apps.model.dto.travel.OutlayDto
import br.com.apps.model.expressions.getCompleteDateInPtBr
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.Label.Companion.containsByName
import br.com.apps.model.model.travel.Outlay
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentExpendEditorBinding
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

private const val TOOLBAR_TITLE = "Custo"

class ExpendEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentExpendEditorBinding? = null
    private val binding get() = _binding!!

    private val args: ExpendEditorFragmentArgs by navArgs()
    private val vmData by lazy {
        ExpendEVMData(
            masterUid = mainActVM.loggedUser.masterUid,
            truckId = mainActVM.loggedUser.truckId,
            driverId = mainActVM.loggedUser.driverId,
            travelId = args.travelId,
            expendId = args.costId,
            permission = mainActVM.loggedUser.accessLevel
        )
    }
    private val viewModel: ExpendEditorViewModel by viewModel { parametersOf(vmData) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpendEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragExpendEditorDescription.setLines(3)
        initStateManager()
        initDateViewClickListener()
        initTextWatcher()
    }

    private fun initTextWatcher() {
        binding.fragExpendEditorValue.run {
            addTextChangedListener(MonetaryMaskUtil(this))
        }
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentCostEditorToolbar.toolbar,
            menuId = R.menu.menu_editor,
            toolbarTextView = binding.fragmentCostEditorToolbar.toolbarText,
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
        binding.fragExpendEditorDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            val calendarPositiveListener = MaterialPickerOnPositiveButtonClickListener<Long> {
                viewModel.newDateHaveBeenSelected(it)
            }
            val calendarDismissListener = DialogInterface.OnDismissListener {
                binding.fragExpendEditorBlackLayer.visibility = View.GONE
                datePicker.clearOnPositiveButtonClickListeners()
                datePicker.clearOnDismissListeners()
            }

            datePicker.addOnPositiveButtonClickListener(calendarPositiveListener)
            datePicker.addOnDismissListener(calendarDismissListener)
            binding.fragExpendEditorBlackLayer.visibility = View.VISIBLE
            datePicker.show(childFragmentManager, "tag")

        }
    }

    /**
     * Try to save an [Outlay].
     *  1. Validate the fields.
     *  2. Convert to DTO.
     *  3. Send DTO to be saved.
     */
    private fun saveIconClicked() {
        binding.apply {

            val name = fragExpendEditorAutoComplete.text.toString()
            val company = fragExpendEditorCompany.text.toString()
            val value = fragExpendEditorValue.text.toString()
            val description = fragExpendEditorDescription.text.toString()
            val isPaidByDriver = fragExpendPaidByEmployeeCheckbox.isChecked.toString()

            var fieldsAreValid = true
            if (name.isBlank()) {
                fragExpendEditorAutoComplete.error = "Preencha o tipo"
                fieldsAreValid = false
            }
            if (!viewModel.data.value!!.labelList.containsByName(name)) {
                fragExpendEditorAutoComplete.error = "Nome inválido"
                fieldsAreValid = false
            }
            if (company.isBlank()) {
                fragExpendEditorCompany.error = "Preencha a empresa"
                fieldsAreValid = false
            }
            if (value.isBlank()) {
                fragExpendEditorValue.error = "Preencha o valor"
                fieldsAreValid = false
            }
            if (description.isBlank()) {
                fragExpendEditorDescription.error = "Preencha a descrição"
                fieldsAreValid = false
            }

            if (fieldsAreValid) {
                val viewDto = OutlayDto(
                    labelId = viewModel.getLabelId(name),
                    date = viewModel.getDate(),
                    company = company,
                    value = value.formatPriceSave().toDouble(),
                    description = description,
                    isPaidByEmployee = isPaidByDriver.toBoolean()
                )
                save(viewDto)
            }

        }
    }

    private fun save(viewDto: OutlayDto) {
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
     *   - Observes labelData to initialize auto-complete EditText.
     *
     *   - Observes expendData to bind [Outlay] if the user is editing.
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            data.apply {
                initAutoCompleteAdapter(labelList)
                outlay?.run { bind(this) }
            }
        }

        viewModel.date.observe(viewLifecycleOwner) { localDate ->
            binding.fragExpendEditorDate.text = localDate.getCompleteDateInPtBr()
        }
    }

    private fun initAutoCompleteAdapter(labelList: List<Label>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            labelList.mapNotNull { it.name }
        )

        val autoComplete = binding.fragExpendEditorAutoComplete
        autoComplete.setAdapter(adapter)

        autoComplete.setOnItemClickListener { _, _, _, _ -> autoComplete.error = null }

    }

    private fun bind(outlay: Outlay) {
        binding.apply {
            fragExpendEditorAutoComplete.setText(outlay.label?.name)
            fragExpendEditorCompany.setText(outlay.company)
            fragExpendEditorValue.setText(outlay.value.formatPriceShow())
            fragExpendEditorDescription.setText(outlay.description)
            outlay.isPaidByEmployee.let { fragExpendPaidByEmployeeCheckbox.isChecked = it }
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