package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.navArgs
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.repository.util.CANCEL
import br.com.apps.repository.util.CONNECTION_FAILURE
import br.com.apps.repository.util.KEY_RESULT
import br.com.apps.repository.util.OK
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRequestEditorBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.expressions.toast
import br.com.apps.trucktech.ui.activities.CameraActivity
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.private_adapter.RequestEditorRecyclerAdapter
import br.com.apps.trucktech.util.DeviceCapabilities
import br.com.apps.trucktech.util.state.DataEvent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

private const val TOOLBAR_TITLE = "Requisição"
private const val LOADING_IMAGE = "Carregando imagem..."

private const val ITEM_SUCCESSFULLY_REMOVED = "Item removido com sucesso"
private const val FAILED_TO_REMOVE_ITEM = "Falha ao remover Item"

class RequestEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestEditorBinding? = null
    private val binding get() = _binding!!

    private val args: RequestEditorFragmentArgs by navArgs()
    private val vmData by lazy {
        RequestEditorVmData(
            requestId = args.requestId,
            permission = mainActVM.loggedUser.accessLevel
        )
    }
    private val viewModel: RequestEditorViewModel by viewModel { parametersOf(vmData) }

    private var adapter: RequestEditorRecyclerAdapter? = null
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val data = result.data!!
                processCameraActivityResult(data)
            }
        }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    private fun processCameraActivityResult(data: Intent) {
        try {
            data.getStringExtra(KEY_RESULT)?.let { filePath ->
                val file = File(filePath)
                if (file.exists()) {
                    DeviceCapabilities.hasNetworkConnection(requireContext()).let { hasConnection ->
                        when(hasConnection) {
                            true -> viewModel.imageHaveBeenLoaded(file.readBytes())
                            false -> requireView().snackBarRed(CONNECTION_FAILURE)
                        }
                    }
                    file.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initBoxSummary()
        initBoxPayment()
        initRecyclerView()
        initFab()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentRequestEditorToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentRequestEditorToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Initialize the box summary. Get user data from sharedViewModel and bind.
     */
    private fun initBoxSummary() {
        binding.boxFragRequestPreviewDescription.apply {
            driverField.text = mainActVM.loggedUser.name
            plateField.text = mainActVM.loggedUser.truck.plate
            valueField.text = viewModel.request?.run { getValue().toCurrencyPtBr() } ?: "-"
        }
    }

    /**
     * Init box payment
     */
    private fun initBoxPayment() {
        binding.boxFragRequestEditorPayment.apply {
            panelAddPixLayoutWaitingUpload.setOnClickListener { launchCameraActivity() }
            boxAddPixEdit.setOnClickListener { launchCameraActivity() }
        }
    }

    private fun launchCameraActivity() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    /**
     * Initialize the RecyclerView.
     *
     * Options:
     *  - clickListener -> navigation
     *  - context menu -> delete
     */
    private fun initRecyclerView() {
        val recyclerView = binding.fragmentRequestEditorRecycler
        adapter = RequestEditorRecyclerAdapter(
            requireContext(),
            viewModel.getItems(),
            itemClickListener = { itemId ->
                when {
                    itemId.isBlank() -> requireContext().toast(LOADING_IMAGE)
                    else -> requireView().navigateTo(
                        RequestEditorFragmentDirections
                            .actionRequestEditorFragmentToItemEditor(
                                requestId = args.requestId, itemId
                            )
                    )
                }
            },
            deleteClickListener = this::showAlertDialogForDelete
        )
        recyclerView.adapter = adapter
    }

    private fun showAlertDialogForDelete(itemId: String) {
        viewModel.requestDarkLayer()

        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle("Apagando item")
            .setMessage("Você realmente deseja apagar este item permanentemente?")
            .setPositiveButton(OK) { _, _ -> deleteItem(itemId) }
            .setNegativeButton(CANCEL) { _, _ -> }
            .setOnDismissListener { viewModel.dismissDarkLayer() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }

    }

    private fun deleteItem(itemId: String) {
        DeviceCapabilities.hasNetworkConnection(requireContext()).let { hasConnection ->
            when (hasConnection) {
                true -> viewModel.deleteItem(itemId).observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Response.Error -> requireView().snackBarRed(FAILED_TO_REMOVE_ITEM)
                        is Response.Success -> requireView().snackBarOrange(
                            ITEM_SUCCESSFULLY_REMOVED
                        )
                    }
                }
                false -> requireView().snackBarRed(CONNECTION_FAILURE)
            }
        }
    }

    /**
     * Initializes the FAB for the request editor fragment.
     *
     * Sets a click listener on the FAB to trigger the bottom sheet for ViewModel.
     */
    private fun initFab() {
        binding.fragmentRequestEditorFab.setOnClickListener {
            it.navigateTo(
                RequestEditorFragmentDirections.actionRequestEditorFragmentToItemEditor(
                    requestId = args.requestId,
                    itemId = null
                )
            )
        }
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *  - Observes requestData to update [adapter] and bind data.
     *  - Observes dark layer to manage the interaction.
     *
     */
    private fun initStateManager() {
        viewModel.dataEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is DataEvent.Initialize -> adapter?.initialize(event.data)
                is DataEvent.Insert -> {
                    binding.fragmentRequestEditorRecycler.smoothScrollToPosition(0)
                    adapter?.insert(event.item)
                }
                is DataEvent.Update -> adapter?.update(event.item)
                is DataEvent.Remove -> adapter?.remove(event.item)
            }
            binding.boxFragRequestPreviewDescription.valueField.text =
                viewModel.request?.getValue()?.toCurrencyPtBr()
        }

        viewModel.image.observe(viewLifecycleOwner) { image ->
            image?.run {
                binding.boxFragRequestEditorPayment.apply {
                    panelAddPixLayoutWaitingUpload.visibility = GONE
                    panelAddPixLayoutAlreadyUploaded.visibility = VISIBLE
                    panelAddPixImage.loadImageThroughUrl(getData())
                }
            }
        }

        viewModel.darkLayer.observe(viewLifecycleOwner) { isRequested ->
            when (isRequested) {
                true -> binding.fragmentRequestEditorDarkLayer.visibility = VISIBLE
                false -> binding.fragmentRequestEditorDarkLayer.visibility = GONE
            }
        }

    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

}