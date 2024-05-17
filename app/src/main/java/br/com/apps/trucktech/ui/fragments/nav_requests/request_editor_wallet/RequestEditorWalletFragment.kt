package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_wallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import br.com.apps.model.IdHolder
import br.com.apps.model.factory.RequestItemFactory
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentRequestEditorWalletBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Requisição de valores"

class RequestEditorWalletFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestEditorWalletBinding? = null
    private val binding get() = _binding!!

    private val args: RequestEditorWalletFragmentArgs by navArgs()
    private val idHolder by lazy { IdHolder(requestId = args.requestId, walletId = args.walletId) }
    private val viewModel: RequestEditorWalletFragmentViewModel by viewModel { parametersOf(idHolder) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestEditorWalletBinding.inflate(inflater, container, false)
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
            toolbar = binding.fragmentRequestEditorWalletToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentRequestEditorWalletToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes itemData for bind.
     */
    private fun initStateManager() {
        viewModel.itemData.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Error -> {
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                    Log.e(TAG_DEBUG, response.exception.message.toString())
                }
                is Response.Success -> response.data?.let { bind() }
            }
        }
    }

    private fun bind() {
        binding.apply {
            viewModel.requestItem.value?.let { value ->
                fragmentRequestEditorWalletValue.setText(value.toPlainString())
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
            fragmentRequestEditorWalletButton.setOnClickListener {
                setClickRangeTimer(it, 1000)

                cleanEditTextError(fragmentRequestEditorWalletValue)

                val value = fragmentRequestEditorWalletValue.text.toString()

                var fieldsAreValid = true
                if (value.isBlank()) {
                    fragmentRequestEditorWalletValue.error = "Preencha o valor"
                    fieldsAreValid = false
                }

                if (fieldsAreValid) {
                    val mappedFields = hashMapOf(
                        Pair(RequestItemFactory.TAG_VALUE, value)
                    )

                    save(mappedFields)

                }
            }
        }
    }

    private fun save(mappedFields: HashMap<String, String>) {
        viewModel.save(mappedFields).observe(viewLifecycleOwner) { response ->
            when(response) {
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