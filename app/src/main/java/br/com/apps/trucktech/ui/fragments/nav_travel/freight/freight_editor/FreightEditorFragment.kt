package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.expressions.getCompleteDateInPtBr
import br.com.apps.model.model.Customer
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.CONNECTION_FAILURE
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.KEY_RESULT
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentFreightEditorBinding
import br.com.apps.trucktech.expressions.hideKeyboard
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.activities.CameraActivity
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.image.ImageFragment
import br.com.apps.trucktech.util.DeviceCapabilities
import br.com.apps.trucktech.util.MonetaryMaskUtil
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceSave
import br.com.apps.trucktech.util.MonetaryMaskUtil.Companion.formatPriceShow
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

private const val TOOLBAR_TITLE = "Frete"

class FreightEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentFreightEditorBinding? = null
    private val binding get() = _binding!!

    private val args: FreightEditorFragmentArgs by navArgs()
    private val vmData by lazy {
        FreightEVMData(
            masterUid = mainActVM.loggedUser.masterUid,
            truckId = mainActVM.loggedUser.truckId,
            travelId = args.travelId,
            driverId = mainActVM.loggedUser.driverId,
            freightId = args.freightId,
            commissionPercentual = mainActVM.loggedUser.commissionPercentual,
            permissionLevel = mainActVM.loggedUser.accessLevel
        )
    }
    private val viewModel: FreightEditorViewModel by viewModel { parametersOf(vmData) }

    private val activityResultLauncherForInvoice =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val data = result.data!!
                processActivityLauncherResultForInvoice(data)
            }
        }

    private val activityResultLauncherForTicket =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val data = result.data!!
                processActivityLauncherResultForTicket(data)
            }
        }


    private fun processActivityLauncherResultForInvoice(data: Intent) {
        try {
            data.getStringExtra(KEY_RESULT)?.let { filePath ->
                val file = File(filePath)
                if (file.exists()) {
                    viewModel.setInvoiceByteArray(file.readBytes())
                    file.delete()
                }
            }

        } catch (e: Exception) {
            Log.e(
                TAG_DEBUG,
                "FreightEditorFragment, processActivityLauncherResult: ${e.message.toString()}"
            )
        }
    }

    private fun processActivityLauncherResultForTicket(data: Intent) {
        try {
            data.getStringExtra(KEY_RESULT)?.let { filePath ->
                val file = File(filePath)
                if (file.exists()) {
                    viewModel.setTicketByteArray(file.readBytes())
                    file.delete()
                }
            }

        } catch (e: Exception) {
            Log.e(
                TAG_DEBUG,
                "FreightEditorFragment, processActivityLauncherResult: ${e.message.toString()}"
            )
        }
    }



    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFreightEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBoxClickListeners()
        initOnTouchListener()
        initStateManager()
        initDateViewClickListener()
        initTextWatcher()
    }

    private fun initBoxClickListeners(): Unit = with(binding) {
        fragFeBoxInvoice.run {
            layoutWaitingUpload.setOnClickListener {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                activityResultLauncherForInvoice.launch(intent)
            }
            layoutEdit.setOnClickListener {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                activityResultLauncherForInvoice.launch(intent)
            }
            layoutDelete.setOnClickListener {
                viewModel.setInvoiceByteArray(null)
            }
            image.setOnClickListener {
                viewModel.getInvoiceImage()?.let { imgData ->
                    val bundle = Bundle()
                    when (imgData) {
                        is ByteArray -> bundle.putByteArray(
                            ImageFragment.ARG_URL_IMAGE,
                            imgData
                        )

                        is String -> bundle.putString(
                            ImageFragment.ARG_URL_IMAGE,
                            imgData
                        )
                    }
                    Navigation.findNavController(it)
                        .navigate(R.id.action_global_image_fragment, bundle)
                }
            }
        }

        fragFeBoxTicket.run {
            layoutWaitingUpload.setOnClickListener {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                activityResultLauncherForTicket.launch(intent)
            }
            layoutEdit.setOnClickListener {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                activityResultLauncherForInvoice.launch(intent)
            }
            layoutDelete.setOnClickListener {
                viewModel.setTicketByteArray(null)
            }
            image.setOnClickListener {
                viewModel.getTicketImage()?.let { imgData ->
                    val bundle = Bundle()
                    when (imgData) {
                        is ByteArray -> bundle.putByteArray(
                            ImageFragment.ARG_URL_IMAGE,
                            imgData
                        )

                        is String -> bundle.putString(
                            ImageFragment.ARG_URL_IMAGE,
                            imgData
                        )
                    }
                    Navigation.findNavController(it)
                        .navigate(R.id.action_global_image_fragment, bundle)
                }
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initOnTouchListener() {
        binding.run {
            fragFeLayout.setOnTouchListener { _, _ ->
                hideKeyboard()
                true
            }
        }
    }

    private fun initTextWatcher() {
        binding.apply {
            fragFreightEditorWeight.run {
                addTextChangedListener(MonetaryMaskUtil(this))
            }

            fragFreightEditorValue.run {
                addTextChangedListener(MonetaryMaskUtil(this))
            }
        }
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentFreightEditorToolbar.toolbar,
            menuId = R.menu.menu_editor,
            toolbarTextView = binding.fragmentFreightEditorToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    override fun initMenuClickListeners(toolbar: Toolbar) {
        super.initMenuClickListeners(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_editor_save -> {
                    saveIconClicked()
                    true
                }

                else -> false
            }
        }
    }

    /**
     * Initialize the Date picker.
     */
    private fun initDateViewClickListener() {
        binding.fragFreightEditorDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecione a data")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            val calendarPositiveListener = MaterialPickerOnPositiveButtonClickListener<Long> {
                viewModel.newDateHaveBeenSelected(it)
            }
            val calendarDismissListener = DialogInterface.OnDismissListener {
                binding.fragFreightEditorBlackLayer.visibility = View.GONE
                datePicker.clearOnPositiveButtonClickListeners()
                datePicker.clearOnDismissListeners()
            }

            datePicker.addOnPositiveButtonClickListener(calendarPositiveListener)
            datePicker.addOnDismissListener(calendarDismissListener)
            binding.fragFreightEditorBlackLayer.visibility = View.VISIBLE
            datePicker.show(childFragmentManager, "tag")

        }
    }

    /**
     * Try to save an [Freight].
     *  1. Validate the fields.
     *  2. Convert to DTO.
     *  3. Send DTO to be saved.
     */
    private fun saveIconClicked() {
        binding.apply {

            val customer = fragFreightEditorCustomerAc.text.toString()
            val origin = fragFreightEditorOrigin.text.toString()
            val destiny = fragFreightEditorDestiny.text.toString()
            val cargo = fragFreightEditorCargo.text.toString()
            val weight = fragFreightEditorWeight.text.toString()
            val value = fragFreightEditorValue.text.toString()

            var fieldsAreValid = true
            if (!viewModel.validateCustomer(customer)) {
                fragFreightEditorCustomerAc.error = "Preencha o cliente"
                fieldsAreValid = false
            }
            if (origin.isBlank()) {
                fragFreightEditorOrigin.error = "Preencha a cidade de origem"
                fieldsAreValid = false
            }
            if (destiny.isBlank()) {
                fragFreightEditorDestiny.error = "Preencha a cidade de destino"
                fieldsAreValid = false
            }
            if (cargo.isBlank()) {
                fragFreightEditorCargo.error = "Preencha a carga"
                fieldsAreValid = false
            }
            if (weight.isBlank()) {
                fragFreightEditorWeight.error = "Preencha o peso"
                fieldsAreValid = false
            }
            if (value.isBlank()) {
                fragFreightEditorValue.error = "Preencha o valor"
                fieldsAreValid = false
            }

            if (fieldsAreValid) {
                val viewDto = FreightDto(
                    customerId = viewModel.getCustomerId(customer),
                    origin = origin,
                    destiny = destiny,
                    cargo = cargo,
                    weight = weight.formatPriceSave().toDouble(),
                    value = value.formatPriceSave().toDouble()
                )

                save(viewDto)

            }
        }
    }

    private fun save(viewDto: FreightDto) {
        DeviceCapabilities.hasNetworkConnection(requireContext()).let { isConnected ->
            when (isConnected) {
                true -> {
                    viewModel.save(viewDto).observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is Response.Error -> {
                                response.exception.printStackTrace()
                                requireView().snackBarRed(FAILED_TO_SAVE)
                            }

                            is Response.Success -> requireView().apply {
                                snackBarGreen(SUCCESSFULLY_SAVED)
                                popBackStack()
                            }
                        }
                    }
                }

                false -> {
                    requireView().snackBarRed(CONNECTION_FAILURE)
                }
            }
        }
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes date for screen changes.
     *
     *   - Observes freightData to bind [Freight] if the user is editing.
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            data.apply {
                initCustomerAutoComplete(customerList)
                freight?.let { bind(it) }
            }
        }

        viewModel.date.observe(viewLifecycleOwner) { localDate ->
            binding.fragFreightEditorDate.text = localDate.getCompleteDateInPtBr()
        }

        viewModel.invoiceImage.observe(viewLifecycleOwner) { image ->
            image?.let { img ->
                binding.fragFeBoxInvoice.run {
                    this.image.loadImageThroughUrl(img.getData())
                    if (layoutWaitingUpload.visibility == VISIBLE) {
                        layoutWaitingUpload.visibility = GONE
                    }
                    if (layoutAlreadyUploaded.visibility == GONE) {
                        layoutAlreadyUploaded.visibility = VISIBLE
                    }
                }
            } ?: binding.fragFeBoxInvoice.run {
                if (layoutWaitingUpload.visibility == GONE) {
                    layoutWaitingUpload.visibility = VISIBLE
                }
                if (layoutAlreadyUploaded.visibility == VISIBLE) {
                    layoutAlreadyUploaded.visibility = GONE
                }
            }
        }

        viewModel.ticketImage.observe(viewLifecycleOwner) { image ->
            image?.let { img ->
                binding.fragFeBoxTicket.run {
                    this.image.loadImageThroughUrl(img.getData())
                    if (layoutWaitingUpload.visibility == VISIBLE) {
                        layoutWaitingUpload.visibility = GONE
                    }
                    if (layoutAlreadyUploaded.visibility == GONE) {
                        layoutAlreadyUploaded.visibility = VISIBLE
                    }
                }
            } ?: binding.fragFeBoxInvoice.run {
                if (layoutWaitingUpload.visibility == GONE) {
                    layoutWaitingUpload.visibility = VISIBLE
                }
                if (layoutAlreadyUploaded.visibility == VISIBLE) {
                    layoutAlreadyUploaded.visibility = GONE
                }
            }
        }

    }

    private fun initCustomerAutoComplete(customerList: List<Customer>) {
        val customersNames = customerList.map { it.name }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            customersNames
        )

        val autoComplete = binding.fragFreightEditorCustomerAc
        autoComplete.setAdapter(adapter)

        autoComplete.setOnItemClickListener { _, _, _, _ -> autoComplete.error = null }

    }

    private fun bind(freight: Freight) {
        binding.apply {
            fragFreightEditorCustomerAc.setText(freight.getCustomerName())
            fragFreightEditorOrigin.setText(freight.origin)
            fragFreightEditorDestiny.setText(freight.destiny)
            fragFreightEditorCargo.setText(freight.cargo)
            fragFreightEditorWeight.setText(freight.weight.formatPriceShow())
            fragFreightEditorValue.setText(freight.value.formatPriceShow())
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}