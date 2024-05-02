package br.com.apps.trucktech.ui.fragments.nav_travel.cost.cost_editor

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import br.com.apps.model.IdHolder
import br.com.apps.model.model.label.Label.Companion.containsByName
import br.com.apps.model.model.label.Label.Companion.getIdByName
import br.com.apps.model.model.label.Label.Companion.getListOfTitles
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Expend.Companion.TAG_COMPANY
import br.com.apps.model.model.travel.Expend.Companion.TAG_DATE
import br.com.apps.model.model.travel.Expend.Companion.TAG_DESCRIPTION
import br.com.apps.model.model.travel.Expend.Companion.TAG_LABEL_ID
import br.com.apps.model.model.travel.Expend.Companion.TAG_VALUE
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.FAILED_TO_SALVE
import br.com.apps.repository.Response
import br.com.apps.repository.SUCCESSFULLY_SAVED
import br.com.apps.repository.UNKNOWN_EXCEPTION
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentExpendEditorBinding
import br.com.apps.trucktech.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.expressions.toast
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Custo"

class ExpendEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentExpendEditorBinding? = null
    private val binding get() = _binding!!

    private val args: ExpendEditorFragmentArgs by navArgs()
    private val idHolder by lazy {
        IdHolder(
            masterUid = sharedViewModel.userData.value?.user?.masterUid,
            truckId = sharedViewModel.userData.value?.truck?.id,
            driverId = sharedViewModel.userData.value?.user?.employeeId,
            expendId = args.costId,
            travelId = args.travelId
        )
    }
    private val viewModel: ExpendEditorFragmentViewModel by viewModel { parametersOf(idHolder) }

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
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
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
     * Try to save an [Expend].
     *  1. Validate the fields.
     *  2. Convert to DTO.
     *  3. Send DTO to be saved.
     */
    private fun saveIconClicked() {
        binding.apply {

            cleanEditTextError(
                fragExpendEditorCompany,
                fragExpendEditorValue,
                fragExpendEditorDescription
            )
            fragExpendEditorAutoComplete.error = null

            val type = fragExpendEditorAutoComplete.text.toString()
            val company = fragExpendEditorCompany.text.toString()
            val value = fragExpendEditorValue.text.toString()
            val description = fragExpendEditorDescription.text.toString()


            var fieldsAreValid = true
            if (type.isBlank()) {
                fragExpendEditorAutoComplete.error = "Preencha o tipo"
                fieldsAreValid = false
            }
            if (!viewModel.labelList.containsByName(type)) {
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
                val mappedFields = hashMapOf(
                    Pair(TAG_LABEL_ID, viewModel.labelList.getIdByName(type)!!),
                    Pair(TAG_COMPANY, company),
                    Pair(TAG_DATE, viewModel.date.value.toString()),
                    Pair(TAG_DESCRIPTION, description),
                    Pair(TAG_VALUE, value)
                )
                save(mappedFields)
            }
        }
    }

    private fun save(mappedFields: HashMap<String, String>) {
        try {
            val dto = viewModel.getExpendDto(mappedFields)
            viewModel.save(dto).observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Error -> {
                        response.exception.printStackTrace()
                        requireView().snackBarRed(FAILED_TO_SALVE)
                    }

                    is Response.Success -> requireView().apply {
                            snackBarGreen(SUCCESSFULLY_SAVED)
                            popBackStack()
                        }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            requireView().snackBarRed(UNKNOWN_EXCEPTION)
        }
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes date for screen changes.
     *
     *   - Observes labelData to initialize auto-complete EditText.
     *
     *   - Observes expendData to bind [Expend] if the user is editing.
     */
    private fun initStateManager() {
        viewModel.labelData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    requireContext().toast(FAILED_TO_LOAD_DATA)
                    response.exception.printStackTrace()
                }

                is Response.Success -> initAutoCompleteAdapter()
            }
        }

        viewModel.expendData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    response.exception.printStackTrace()
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                }

                is Response.Success -> {
                    response.data?.let { bind(it) }
                }
            }
        }

        viewModel.date.observe(viewLifecycleOwner) { localDate ->
            binding.fragExpendEditorDate.text = localDate.getCompleteDateInPtBr()
        }
    }

    private fun initAutoCompleteAdapter() {
        val dataSet = viewModel.labelList.getListOfTitles()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            dataSet
        )
        val autoComplete = binding.fragExpendEditorAutoComplete
        autoComplete.setAdapter(adapter)
    }

    private fun bind(expend: Expend) {
        binding.apply {
            fragExpendEditorAutoComplete.setText(expend.label?.name)
            fragExpendEditorCompany.setText(expend.company)
            fragExpendEditorValue.setText(expend.value?.toPlainString())
            fragExpendEditorDescription.setText(expend.description)
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