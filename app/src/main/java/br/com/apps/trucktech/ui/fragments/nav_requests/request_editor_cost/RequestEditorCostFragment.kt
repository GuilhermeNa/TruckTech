package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_cost

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.navArgs
import br.com.apps.model.IdHolder
import br.com.apps.model.factory.RequestItemFactory
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.Label.Companion.getListOfTitles
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.FAILED_TO_SAVE
import br.com.apps.repository.Response
import br.com.apps.repository.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentRequestEditorCostBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Requisição de custo"

class RequestEditorCostFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestEditorCostBinding? = null
    private val binding get() = _binding!!

    private val args: RequestEditorCostFragmentArgs by navArgs()
    private val idHolder by lazy {
        IdHolder(
            masterUid = sharedViewModel.userData.value?.user?.masterUid,
            requestId = args.requestId,
            expendId = args.costId
        )
    }
    private val viewModel: RequestEditorCostFragmentViewModel by viewModel { parametersOf(idHolder) }

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
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
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
        viewModel.labelData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                    Log.e(TAG_DEBUG, response.exception.message.toString())
                }

                is Response.Success -> response.data?.let { initAutoCompleteAdapter(it) }
            }
        }

        viewModel.itemData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                    Log.e(TAG_DEBUG, response.exception.message.toString())
                }

                is Response.Success -> response.data?.let { bind() }
            }
        }
    }

    private fun initAutoCompleteAdapter(labels: List<Label>) {
        val dataSet = labels.getListOfTitles()
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            dataSet
        )
        val autoComplete = binding.fragmentRequestEditorCostAutoComplete
        autoComplete.setAdapter(adapter)
    }

    fun bind() {
        binding.apply {
            viewModel.requestItem.let { item ->
                fragmentRequestEditorCostAutoComplete.setText(viewModel.getLabelDescriptionById())
                fragmentRequestEditorCostValue.setText(item.value?.toPlainString())
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

                cleanEditTextError(fragmentRequestEditorCostValue)
                fragmentRequestEditorCostAutoComplete.error = null

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
                    val mappedFields = hashMapOf(
                        Pair(RequestItemFactory.TAG_LABEL_ID, labelId),
                        Pair(RequestItemFactory.TAG_VALUE, value)
                    )

                    save(mappedFields)

                }
            }
        }
    }

    private fun save(mappedFields: HashMap<String, String>) {
        viewModel.save(mappedFields).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    requireView().snackBarRed(FAILED_TO_SAVE)
                    Log.e(TAG_DEBUG, response.exception.toString())
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