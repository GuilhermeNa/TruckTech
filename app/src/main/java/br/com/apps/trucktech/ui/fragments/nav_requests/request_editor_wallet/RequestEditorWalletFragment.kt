package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_wallet

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
import br.com.apps.trucktech.databinding.FragmentRequestEditorWalletBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.math.BigDecimal

private const val TOOLBAR_TITLE = "Requisição de valores"

/**
 * A simple [Fragment] subclass.
 * Use the [RequestEditorWalletFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RequestEditorWalletFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestEditorWalletBinding? = null
    private val binding get() = _binding!!

    private val args: RequestEditorWalletFragmentArgs by navArgs()
    private val viewModel: RequestEditorWalletFragmentViewModel by viewModel {
        parametersOf(
            args.requestId,
            args.walletId
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

    private fun initStateManager() {
        viewModel.loadedRequestData.observe(viewLifecycleOwner) {
            it?.let { resource ->
                if (resource.error == null) {
                    args.walletId?.let { bind() }
                } else {
                    requireView().snackBarRed(resource.error!!)
                }
            }
        }

        viewModel.requestItemSaved.observe(viewLifecycleOwner) {
            it?.let { resource ->
                if(resource.error == null) {
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
            viewModel.requestItem?.value?.let { value ->
                fragmentRequestEditorWalletValue.setText(value.toPlainString())
            }
        }
    }

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
                    val requestItemDto =
                        if (viewModel.requestItem != null) {
                            viewModel.requestItem?.let { item ->
                                item.value = BigDecimal(value)
                                item.toDto()
                            }
                        } else {
                            RequestItemDtoFactory.create(
                                value = value,
                                type = RequestItemType.WALLET
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