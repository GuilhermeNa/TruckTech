package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_refuel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.model.IdHolder
import br.com.apps.model.factory.RequestItemFactory
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentRequestEditorRefuelBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
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
    private val idHolder by lazy {
        IdHolder(
            requestId = args.requestId,
            refuelId = args.refuelId
        )
    }
    private val viewModel: RequestEditorRefuelViewModel by viewModel { parametersOf(idHolder) }

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
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes refuelData for bind.
     */
    private fun initStateManager() {
        viewModel.refuelData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {}
                is Response.Success -> response.data?.let { bind() }
            }
        }

    }

    fun bind() {
        binding.apply {
            viewModel.requestItem.let {
                it.kmMarking?.let { km ->
                    fragmentRequestEditorKm.setText(km.toString())
                }
                it.value?.let { value ->
                    fragmentRequestEditorValue.setText(value.toPlainString())
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
                    val mappedFields = hashMapOf(
                        Pair(RequestItemFactory.TAG_KM_MARKING, kmMarking),
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
                    Log.e(TAG_DEBUG, response.exception.message.toString())
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