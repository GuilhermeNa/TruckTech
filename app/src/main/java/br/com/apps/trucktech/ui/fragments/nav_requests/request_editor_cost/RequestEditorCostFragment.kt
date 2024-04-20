package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_cost

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.model.factory.RequestItemDtoFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.Label.Companion.getListOfTitles
import br.com.apps.model.model.request.request.RequestItemType
import br.com.apps.repository.FAILED_TO_SALVE
import br.com.apps.repository.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.databinding.FragmentRequestEditorCostBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.math.BigDecimal

private const val TOOLBAR_TITLE = "Requisição de custo"

/**
 * A simple [Fragment] subclass.
 * Use the [RequestEditorCostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RequestEditorCostFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestEditorCostBinding? = null
    private val binding get() = _binding!!

    private val args: RequestEditorCostFragmentArgs by navArgs()
    private val masterUid by lazy {
        sharedViewModel.userData.value?.user?.masterUid
    }
    private val viewModel: RequestEditorCostFragmentViewModel by viewModel {
        parametersOf(
            masterUid,
            args.requestId,
            args.costId
        )
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            viewModel.loadData()
    }

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
     * Init state manager
     */
    private fun initStateManager() {
        viewModel.loadedRequest.observe(viewLifecycleOwner) {
            it?.let { resource ->
                if (resource.error == null) {
                    args.costId?.let { bind() }
                } else {
                    requireView().snackBarRed(resource.error!!)
                }
            }
        }

        viewModel.loadedLabelsData.observe(viewLifecycleOwner) {
            it?.let { resource ->
                if (resource.error == null) {
                    initAutoCompleteAdapter(resource.data)
                }
            }
        }

        viewModel.requestItemSaved.observe(viewLifecycleOwner) {
            it?.let { resource ->
                if (resource.error == null) {
                    requireView().snackBarGreen(SUCCESSFULLY_SAVED)
                    requireView().popBackStack()
                } else {
                    requireView().snackBarRed(FAILED_TO_SALVE)
                }
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
            viewModel.requestItem?.let {
                viewModel.getLabelDescription()?.let { text ->
                    fragmentRequestEditorCostAutoComplete.setText(text)
                }

                it.value?.let { value ->
                    fragmentRequestEditorCostValue.setText(value.toPlainString())
                }
            }
        }
    }

    /**
     * Save button
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
                    val requestItemDto =
                        if (viewModel.requestItem != null) {
                            viewModel.requestItem?.let { item ->
                                item.value = BigDecimal(value)
                                item.labelId = labelId
                                item.toDto()
                            }
                        } else {
                            RequestItemDtoFactory.create(
                                value = value,
                                labelId = labelId!!,
                                type = RequestItemType.COST
                            ).toDto()
                        }

                    requestItemDto?.let { itemDto ->
                        viewModel.saveButtonClicked(itemDto)
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
        _binding = null
    }

}