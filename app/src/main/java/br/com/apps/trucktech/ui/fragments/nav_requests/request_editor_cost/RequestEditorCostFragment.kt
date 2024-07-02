package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_cost

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.navArgs
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.Label.Companion.getListOfTitles
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.databinding.FragmentRequestEditorCostBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.util.MonetaryMaskUtil
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceSave
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceShow
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Requisição de custo"

class RequestEditorCostFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestEditorCostBinding? = null
    private val binding get() = _binding!!

    private val args: RequestEditorCostFragmentArgs by navArgs()
    private val vmData by lazy {
        RequestEditorCostVmData(
            masterUid = mainActVM.loggedUser.masterUid,
            requestId = args.requestId,
            costReqId = args.costId,
            permission = mainActVM.loggedUser.permissionLevelType
        )
    }
    private val viewModel: RequestEditorCostFragmentViewModel by viewModel { parametersOf(vmData) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestEditorCostBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initSaveButton()
        initTextMask()
    }

    private fun initTextMask() {
        binding.fragmentRequestEditorCostValue.run { addTextChangedListener(MonetaryMaskUtil(this)) }
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentRequestEditorCostToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentRequestEditorCostToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes labelData for create the adapter for autocomplete.
     *   - Observes itemData for bind.
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            data.labels.run { initAutoCompleteAdapter(this) }
            data.item?.run { bind(this) }
        }
    }

    private fun initAutoCompleteAdapter(labels: List<Label>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            labels.getListOfTitles()
        )

        val autoComplete = binding.fragmentRequestEditorCostAutoComplete
        autoComplete.setAdapter(adapter)

        autoComplete.setOnItemClickListener { _, _, _, _ ->
            autoComplete.error = null
        }

    }

    fun bind(requestItem: RequestItem) {
        binding.apply {
            requestItem.let { item ->
                fragmentRequestEditorCostAutoComplete.setText(viewModel.getLabelDescriptionById())
                fragmentRequestEditorCostValue.setText(item.value?.formatPriceShow())
            }
        }
    }

    /**
     * Try to save an [RequestItem].
     *  1. Validate the fields.
     *  2. Create a hashMap with the data.
     *  3. Send it to the viewModel for saving.
     */
    private fun initSaveButton() {
        binding.apply {
            fragmentRequestEditorCostButton.setOnClickListener {
                setClickRangeTimer(it, 1000)

                val autoCompleteText = fragmentRequestEditorCostAutoComplete.text.toString()
                val value = fragmentRequestEditorCostValue.text.toString()
                val labelId = viewModel.getLabelIdByName(autoCompleteText)

                var fieldsAreValid = true
                if (autoCompleteText.isBlank()) {
                    fragmentRequestEditorCostAutoComplete.error = "Preencha o tipo de custo"
                    fieldsAreValid = false
                }
                if (labelId == null) {
                    fragmentRequestEditorCostAutoComplete.error = "Tipo de custo inválido"
                    fieldsAreValid = false
                }
                if (value.isBlank()) {
                    fragmentRequestEditorCostValue.error = "Preencha o valor"
                    fieldsAreValid = false
                }

                if (fieldsAreValid) {
                    val viewDto = RequestItemDto(
                        labelId = labelId,
                        value = value.formatPriceSave().toDouble()
                    )

                    save(viewDto)

                }
            }
        }
    }

    private fun save(viewDto: RequestItemDto) {
        viewModel.save(viewDto).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    requireView().snackBarRed(FAILED_TO_SAVE)
                    response.exception.printStackTrace()
                }

                is Response.Success -> {
                    requireView().snackBarGreen(SUCCESSFULLY_SAVED)
                    requireView().popBackStack()
                }
            }
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