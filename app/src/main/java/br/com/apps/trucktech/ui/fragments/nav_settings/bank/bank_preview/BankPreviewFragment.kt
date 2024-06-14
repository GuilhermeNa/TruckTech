package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_preview

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import br.com.apps.model.IdHolder
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.repository.util.FAILED_TO_REMOVE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentBankPreviewBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Conta bancária"

/**
 * A simple [Fragment] subclass.
 * Use the [BankPreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BankPreviewFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentBankPreviewBinding? = null
    private val binding get() = _binding!!

    private val args: BankPreviewFragmentArgs by navArgs()
    private val idHolder by lazy {
        IdHolder(
            driverId = mainActVM.loggedUser.driverId,
            bankAccountId = args.bankId
        )
    }
    private val viewModel: BankPreviewViewModel by viewModel { parametersOf(idHolder) }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW
    //--------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBankPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    //--------------------------------------------------------------------------------------------//
    //  ON VIEW CREATED
    //--------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragBankPreviewToolbar.toolbar,
            menuId = R.menu.menu_preview,
            toolbarTextView = binding.fragBankPreviewToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    override fun initMenuClickListeners(toolbar: Toolbar) {
        super.initMenuClickListeners(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_preview_edit -> {
                    requireView().navigateTo(
                        BankPreviewFragmentDirections
                            .actionBankPreviewFragmentToBankEditorFragment(args.bankId)
                    )
                    true
                }

                R.id.menu_preview_delete -> {
                    showAlertDialog()
                    true
                }

                else -> false
            }
        }
    }

    private fun showAlertDialog() {
        viewModel.requestDarkLayer()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Removendo conta")
            .setMessage("Você confirma a remoção desta conta?")
            .setPositiveButton("Ok") { _, _ -> deleteBankAccount() }
            .setNegativeButton("Cancelar") { _, _ -> }
            .setOnDismissListener { viewModel.dismissDarkLayer() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun deleteBankAccount() {
        lifecycleScope.launch {
            viewModel.delete().observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Error -> requireView().snackBarRed(FAILED_TO_REMOVE)
                    is Response.Success -> {
                        requireView().snackBarOrange(SUCCESSFULLY_REMOVED)
                        requireView().popBackStack()
                    }
                }
            }
        }
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes bankData for bind.
     *   - Observes darkLayer to manage the interaction.
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                is Response.Success -> response.data?.let { bind(it) }
            }
        }

        viewModel.darkLayer.observe(viewLifecycleOwner) { isRequested ->
            when (isRequested) {
                true -> binding.fragBankPreviewDarkLayer.visibility = VISIBLE
                false -> binding.fragBankPreviewDarkLayer.visibility = GONE
            }
        }

    }

    private fun bind(data: BankPFData) {
        binding.apply {
            fragBankPreviewImage.loadImageThroughUrl(data.urlImage, requireContext())
            fragBankPreviewBank.text = data.bankAcc.bankName
            fragBankPreviewBranch.text = data.bankAcc.branch.toString()
            fragBankPreviewNumber.text = data.bankAcc.accNumber.toString()
            fragBankPreviewType.text = data.bankAcc.getTypeDescription()
            fragBankPreviewPix.text = data.bankAcc.pix
        }
    }

//--------------------------------------------------------------------------------------------//
//  ON DESTROY VIEW
//--------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}