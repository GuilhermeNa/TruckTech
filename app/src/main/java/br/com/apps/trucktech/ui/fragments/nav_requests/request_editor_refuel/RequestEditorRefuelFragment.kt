package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_refuel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.model.factory.RequestItemDtoFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.request.request.RequestItemType
import br.com.apps.repository.FAILED_TO_SALVE
import br.com.apps.repository.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.databinding.FragmentRequestEditorRefuelBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.model.label.DEFAULT_REFUEL_LABEL_ID
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.math.BigDecimal

private const val TOOLBAR_TITLE = "Requisição de Abastecimento"

/**
 * A simple [Fragment] subclass.
 * Use the [RequestEditorRefuelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RequestEditorRefuelFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestEditorRefuelBinding? = null
    private val binding get() = _binding!!

    private val args: RequestEditorRefuelFragmentArgs by navArgs()
    private val viewModel: RequestEditorRefuelViewModel by viewModel {
        parametersOf(
            args.requestId,
            args.refuelId
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
        _binding = FragmentRequestEditorRefuelBinding.inflate(inflater, container, false)
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
            toolbar = binding.fragmentRequestEditorFreightToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentRequestEditorFreightToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * State Manager is configured here
     */
    private fun initStateManager() {
        viewModel.loadedRequestData.observe(viewLifecycleOwner) {
            it?.let { resource ->
                if (resource.error == null) {
                    args.refuelId?.let { bind() }
                } else {
                    requireView().snackBarRed(resource.error!!)
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

    fun bind() {
        binding.apply {
            viewModel.requestItem?.let {
                it.kmMarking?.let { km ->
                    fragmentRequestEditorKm.setText(km.toString())
                }
                it.value?.let { value ->
                    fragmentRequestEditorValue.setText(value.toPlainString())
                }
            }
        }
    }

    private fun initSaveButton() {
        binding.apply {
            fragmentRequestEditorFreightButton.setOnClickListener {
                setClickRangeTimer(it, 1000)

                cleanEditTextError(
                    fragmentRequestEditorKm,
                    fragmentRequestEditorValue
                )

                val kmMarking = fragmentRequestEditorKm.text.toString()
                val value = fragmentRequestEditorValue.text.toString()

                var fieldsAreValid = true
                if (kmMarking.isBlank()) {
                    fragmentRequestEditorKm.error = "Preencha a quilometragem"
                    fieldsAreValid = false
                }
                if (value.isBlank()) {
                    fragmentRequestEditorValue.error = "Preencha o valor"
                    fieldsAreValid = false
                }

                if (fieldsAreValid) {
                    val requestItemDto = viewModel.requestItem?.also { item ->
                        item.kmMarking = kmMarking.toInt()
                        item.value = BigDecimal(value)
                    }?.toDto()
                        ?: RequestItemDtoFactory.create(
                            kmMarking = kmMarking,
                            value = value,
                            labelId = DEFAULT_REFUEL_LABEL_ID,
                            type = RequestItemType.REFUEL
                        ).toDto()

                    viewModel.saveButtonClicked(requestItemDto)
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