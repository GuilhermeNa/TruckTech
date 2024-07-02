package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_refuel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentRequestEditorRefuelBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.util.MonetaryMaskUtil
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatIntSave
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatIntShow
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceSave
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceShow
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

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
    private val vmData by lazy {
        RequestEditorRefuelVmData(
            requestId = args.requestId,
            refuelReqId = args.refuelId,
            permission = mainActVM.loggedUser.permissionLevelType
        )
    }
    private val viewModel: RequestEditorRefuelViewModel by viewModel { parametersOf(vmData) }

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
        initTextMask()
    }

    private fun initTextMask() {
        binding.apply {
            fragmentRequestEditorKm.run { addTextChangedListener(MonetaryMaskUtil(this)) }
            fragmentRequestEditorValue.run { addTextChangedListener(MonetaryMaskUtil(this)) }
        }
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentRequestEditorFreightToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentRequestEditorFreightToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes refuelData for bind.
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {}
                is Response.Success -> response.data?.let { bind(it) }
            }
        }

    }

    fun bind(requestItem: RequestItem) {
        binding.apply {
            requestItem.let {
                it.kmMarking?.let { km ->
                    fragmentRequestEditorKm.setText(km.formatIntShow())
                }
                it.value?.let { value ->
                    fragmentRequestEditorValue.setText(value.formatPriceShow())
                }
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
            fragmentRequestEditorFreightButton.setOnClickListener {
                setClickRangeTimer(it, 1000)

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
                    val viewDto = RequestItemDto(
                        kmMarking = kmMarking.formatIntSave().toInt(),
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
                    Log.e(TAG_DEBUG, response.exception.message.toString())
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