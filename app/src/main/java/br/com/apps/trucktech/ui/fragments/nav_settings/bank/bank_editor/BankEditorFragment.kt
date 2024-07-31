package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_editor

import android.R
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.payment_method.PixType
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentBankEditorBinding
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher
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
    private val vmData by lazy {
        BankEVmData(
            masterUid = mainActVM.loggedUser.masterUid,
            employeeId = mainActVM.loggedUser.driverId,
            bankAccountId = args.bankId
        )
    }
    private val viewModel: BankEditorViewModel by viewModel { parametersOf(vmData) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBankEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initSaveButton()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
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
        viewModel.data.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                is Response.Success -> {
                    response.data?.apply {
                        bankAcc?.let { bind(it) }
                        initBankAutoComplete(bankList)
                        initPixAutoComplete(pixList)
                    }
                }
            }
        }
    }

    private fun initBankAutoComplete(bankList: List<Bank>) {
        val bankNames = bankList.map { it.name }
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            bankNames
        )
        val autoComplete = binding.fragBankEditorBankAutoComplete
        autoComplete.setAdapter(adapter)
    }

    private fun initPixAutoComplete(pixList: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            pixList
        )
        val autoComplete = binding.fragmentBankEditorAutoComplete
        autoComplete.setAdapter(adapter)

        var watcher: MaskedWatcher? = null
        autoComplete.setOnItemClickListener { _, _, position, _ ->
            watcher?.let { binding.fragBankEditorPix.removeTextChangedListener(watcher) }
            binding.fragBankEditorPix.setText("")

            watcher = when (position) {
                0 -> binding.fragBankEditorPix.run {
                    inputType = InputType.TYPE_CLASS_PHONE
                    MaskedWatcher(MaskedFormatter("(##) # ####-####"), this)
                }


                1 -> binding.fragBankEditorPix.run {
                    inputType = InputType.TYPE_CLASS_PHONE
                    MaskedWatcher(MaskedFormatter("##.###.###/####-##"), this)
                }


                2 -> binding.fragBankEditorPix.run {
                    inputType = InputType.TYPE_CLASS_PHONE
                    MaskedWatcher(MaskedFormatter("###.###.###-##"), this)
                }


                3 -> binding.fragBankEditorPix.run {
                    inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    null
                }

                else -> null

            }

            watcher?.let { binding.fragBankEditorPix.addTextChangedListener(it) }

        }

    }

    private fun bind(bankAccount: BankAccount) {
        binding.apply {
            fragBankEditorBankAutoComplete.setText(bankAccount.getBankName())
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
                fragBankEditorBankAutoComplete.error = null
                cleanEditTextError(fragBankEditorBranch, fragBankEditorAccNumber, fragBankEditorPix)

                val bankName = fragBankEditorBankAutoComplete.text.toString()
                val branch = fragBankEditorBranch.text.toString()
                val accNumber = fragBankEditorAccNumber.text.toString()
                val type = fragmentBankEditorAutoComplete.text.toString()
                val pix = fragBankEditorPix.text.toString()

                var fieldsAreValid = true

                if (!viewModel.validateBank(bankName)) {
                    fragBankEditorBankAutoComplete.error = "Preencha nome da instituição"
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
                    val viewDto = BankAccountDto(
                        bankId = viewModel.getBankId(bankName),
                        branch = branch.toInt(),
                        accNumber = accNumber.toInt(),
                        pixType = PixType.getTypeInString(type),
                        pix = pix,
                        mainAccount = false,
                    )
                    saveBankAccount(viewDto)
                }

            }
        }
    }

    private fun saveBankAccount(viewDto: BankAccountDto) {
        viewModel.save(viewDto).observe(viewLifecycleOwner) { response ->
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
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}