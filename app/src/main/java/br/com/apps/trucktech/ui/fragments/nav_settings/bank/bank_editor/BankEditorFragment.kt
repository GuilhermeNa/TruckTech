package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_editor

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.model.IdHolder
import br.com.apps.model.factory.BankAccountFactory
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.model.model.payment_method.PixType
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.FAILED_TO_SAVE
import br.com.apps.repository.Response
import br.com.apps.repository.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentBankEditorBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Adicionando Conta"

/**
 * A simple [Fragment] subclass.
 * Use the [BankEditorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BankEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentBankEditorBinding? = null
    private val binding get() = _binding!!

    private val args: BankEditorFragmentArgs by navArgs()
    private val idHolder by lazy {
        IdHolder(
            masterUid = sharedViewModel.userData.value?.user?.masterUid,
            driverId = sharedViewModel.userData.value!!.user!!.employeeId,
            bankAccountId = args.bankId
        )
    }
    private val viewModel: BankEditorViewModel by viewModel { parametersOf(idHolder) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBankEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initSaveButton()
        initAutoCompleteAdapter()
    }

    /**
     * Init auto complete adapter
     */
    private fun initAutoCompleteAdapter() {
        val dataSet = viewModel.descriptionList
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            dataSet
        )
        val autoComplete = binding.fragmentBankEditorAutoComplete
        autoComplete.setAdapter(adapter)
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragBankEditorToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragBankEditorToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Init state manager
     */
    private fun initStateManager() {
        viewModel.bankData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                is Response.Success -> response.data?.let { bind(it) }
            }
        }
    }

    private fun bind(bankAccount: BankAccount) {
        binding.apply {
            fragBankEditorBank.setText(bankAccount.bankName)
            fragBankEditorBranch.setText(bankAccount.branch.toString())
            fragBankEditorAccNumber.setText(bankAccount.accNumber.toString())
            fragmentBankEditorAutoComplete.setText(bankAccount.getTypeDescription())
            fragBankEditorPix.setText(bankAccount.pix)
        }
    }

    /**
     * Init save Button
     */
    private fun initSaveButton() {
        binding.apply {
            fragBankEditorButton.setOnClickListener {
                setClickRangeTimer(it, 1000)

                fragmentBankEditorAutoComplete.error = null
                cleanEditTextError(
                    fragBankEditorBank,
                    fragBankEditorBranch,
                    fragBankEditorAccNumber,
                    fragBankEditorPix
                )

                val bankName = fragBankEditorBank.text.toString()
                val branch = fragBankEditorBranch.text.toString()
                val accNumber = fragBankEditorAccNumber.text.toString()
                val type = fragmentBankEditorAutoComplete.text.toString()
                val pix = fragBankEditorPix.text.toString()

                var fieldsAreValid = true
                if (bankName.isBlank()) {
                    fragBankEditorBank.error = "Preencha nome da instituição"
                    fieldsAreValid = false
                }
                if (branch.isBlank()) {
                    fragBankEditorBranch.error = "Preencha agência"
                    fieldsAreValid = false
                }
                if (accNumber.isBlank()) {
                    fragBankEditorAccNumber.error = "Preencha número da conta"
                    fieldsAreValid = false
                }
                if (!PixType.isValidString(type)) {
                    fragmentBankEditorAutoComplete.error = "Tipo de chave inválido"
                    fieldsAreValid = false
                }
                if (pix.isBlank()) {
                    fragBankEditorPix.error = "Preencha a chave"
                    fieldsAreValid = false
                }

                if (fieldsAreValid) {
                    val mappedFields = hashMapOf(
                        Pair(BankAccountFactory.TAG_BANK_NAME, bankName),
                        Pair(BankAccountFactory.TAG_BRANCH, branch),
                        Pair(BankAccountFactory.TAG_ACC_NUMBER, accNumber),
                        Pair(BankAccountFactory.TAG_PIX, pix),
                        Pair(BankAccountFactory.TAG_PIX_TYPE, PixType.getTypeInString(type)),
                    )
                    saveBankAccount(mappedFields)
                }

            }
        }
    }

    private fun saveBankAccount(mappedFields: HashMap<String, String>) {
        viewModel.save(mappedFields).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    requireView().snackBarRed(FAILED_TO_SAVE)
                    Log.e(TAG_DEBUG, response.exception.message.toString() )
                }
                is Response.Success -> {
                    requireView().snackBarGreen(SUCCESSFULLY_SAVED)
                    requireView().popBackStack()
                }

            }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}