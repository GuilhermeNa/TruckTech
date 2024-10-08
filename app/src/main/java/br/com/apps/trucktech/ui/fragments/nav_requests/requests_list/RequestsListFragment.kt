package br.com.apps.trucktech.ui.fragments.nav_requests.requests_list

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.expressions.getMonthAndYearInPtBr
import br.com.apps.model.model.request.Request
import br.com.apps.repository.util.CANCEL
import br.com.apps.repository.util.CONNECTION_FAILURE
import br.com.apps.repository.util.FAILED_TO_REMOVE
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.OK
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRequestsListBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.expressions.toast
import br.com.apps.trucktech.ui.activities.main.VisualComponents
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_requests.requests_list.private_adapter.RequestsListRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.DateRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.HeaderDefaultHorizontalAdapter
import br.com.apps.trucktech.util.DeviceCapabilities
import br.com.apps.trucktech.util.state.State
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.security.InvalidParameterException

private const val TOOLBAR_TITLE = "Solicitação de pagamento"

private const val REQUEST_HAVE_BEEN_SAVED = "Requisição adicionada"

class RequestsListFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestsListBinding? = null
    private val binding get() = _binding!!
    private var stateHandler: RequestsListState? = null

    private val vmData by lazy {
        RequestLVMData(
            masterUid = mainActVM.loggedUser.masterUid,
            uid = mainActVM.loggedUser.uid,
            truckId = mainActVM.loggedUser.truckId,
            driverId = mainActVM.loggedUser.driverId,
            permission = mainActVM.loggedUser.accessLevel
        )
    }
    private val viewModel: RequestsListViewModel by viewModel { parametersOf(vmData) }
    private var adapter: RequestsListRecyclerAdapter? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestsListBinding.inflate(inflater, container, false)
        stateHandler = RequestsListState(binding, lifecycleScope)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initHeaderRecyclerView()
        initFab()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragRlToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragRlToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    /**
     * Header Recycler is configured here
     */
    private fun initHeaderRecyclerView() {
        val recyclerView = binding.fragRlRecyclerHeader
        val adapter = HeaderDefaultHorizontalAdapter(
            context = requireContext(),
            dataSet = viewModel.headerItemsMap,
            adapterPos = viewModel.headerPos,
            itemClickListener = { headerTxt ->
                viewModel.newHeaderSelected(headerTxt)
                initRequestRecyclerView()
            }
        )
        recyclerView.adapter = adapter
    }

    /**
     * Try to create a [PaymentRequest].
     *  1. Displays an alert dialog for confirmation.
     *  2. If the dialog response is positive, sends the request to the viewModel for creation.
     */
    private fun initFab() {
        binding.fragRlFab.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        viewModel.dialogRequested()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nova requisição")
            .setMessage("Você confirma a adição uma nova requisição?")
            .setPositiveButton("Ok") { _, _ ->
                createNewRequest()
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .setOnDismissListener { viewModel.dialogDismissed() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun createNewRequest() {
        DeviceCapabilities.hasNetworkConnection(requireContext()).let { isConnected ->
            when (isConnected) {
                false -> requireContext().toast("Sem conexão")

                true -> {
                    lifecycleScope.launch {
                        viewModel.save().asFlow().collect { response ->
                            when (response) {
                                is Response.Error -> requireView().snackBarRed(FAILED_TO_SAVE)
                                is Response.Success -> {
                                    response.data?.let {
                                        requireView()
                                            .navigateTo(
                                                RequestsListFragmentDirections
                                                    .actionRequestsListFragmentToRequestEditorFragment(
                                                        it
                                                    )
                                            )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes requestData for update the recyclerView.
     *   - Observes darkLayer to manage the interaction.
     *   - Observes bottomNav to manage the interaction.
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            if(data.isNotEmpty()) {
                if (adapter == null) {
                    initRequestRecyclerView()
                } else {
                    adapter?.update(data)
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state != State.Loading) stateHandler?.showAfterLoading()

            when (state) {
                State.Loading -> stateHandler?.showLoading()
                State.Loaded -> stateHandler?.showLoaded()
                State.Empty -> stateHandler?.showEmpty()
                is State.Error -> stateHandler?.showError(state.error)
                else -> {}
            }
        }

        viewModel.dialog.observe(viewLifecycleOwner) { dialog ->
            when (dialog) {
                true -> {
                    binding.fragRlDarkLayer.visibility = View.VISIBLE
                    mainActVM.setComponents(VisualComponents(hasBottomNavigation = false))
                }

                false -> {
                    binding.fragRlDarkLayer.visibility = View.GONE
                    mainActVM.setComponents(VisualComponents(hasBottomNavigation = true))
                }
            }
        }

    }

    /**
     * Initialize the RecyclerView.
     *
     * Options:
     *  - clickListener -> navigation
     *  - context menu -> delete
     */
    private fun initRequestRecyclerView() {
        try {
            viewModel.filterDataByHeader()?.let { dataSet ->
                initConcatAdapter(initAdaptersData(dataSet))
            }
        } catch (e: Exception) {
            e.message?.let { error -> requireView().snackBarRed(error) }
        }
    }

    private fun createAdapters(itemsMap: Map.Entry<String, List<Request>>) =
        listOf(
            DateRecyclerAdapter(requireContext(), listOf(itemsMap.key)),
            RequestsListRecyclerAdapter(
                requireContext(),
                itemsMap.value,
                itemCLickListener = { requestId ->
                    requireView().navigateTo(
                        RequestsListFragmentDirections.actionRequestsListFragmentToRequestEditorFragment(
                            requestId
                        )
                    )
                },
                deleteClickListener = ::showAlertDialogForDelete
            )
        )

    private fun initAdaptersData(dataSet: List<Request>): List<RecyclerView.Adapter<out RecyclerView.ViewHolder>> {
        return dataSet
            .sortedBy { it.date }
            .reversed()
            .groupBy {
                it.date.getMonthAndYearInPtBr()
                    ?: throw InvalidParameterException("Date is null")
            }
            .map { createAdapters(it) }
            .flatten()
    }

    private fun initConcatAdapter(adapters: List<RecyclerView.Adapter<out RecyclerView.ViewHolder>>) {
        val concatAdapter = ConcatAdapter(adapters)
        val recyclerView = binding.fragRlRecyclerBoddy
        recyclerView.adapter = concatAdapter
    }

    private fun showAlertDialogForDelete(id: String) {
        viewModel.dialogRequested()

        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle("Apagando requisição")
            .setMessage("Você realmente deseja apagar esta requisição e seus itens permanentemente?")
            .setPositiveButton(OK) { _, _ -> deleteRequest(id) }
            .setNegativeButton(CANCEL) { _, _ -> }
            .setOnDismissListener { viewModel.dialogDismissed() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun deleteRequest(id: String) {
        DeviceCapabilities.hasNetworkConnection(requireContext()).let { isConnected ->
            when (isConnected) {
                false -> {
                    requireView().snackBarRed(CONNECTION_FAILURE)
                }

                true -> {
                    stateHandler?.showDeleting()
                    lifecycleScope.launch {
                        viewModel.delete(id).asFlow().collect { response ->
                            when (response) {
                                is Response.Error -> requireView().snackBarRed(FAILED_TO_REMOVE)
                                is Response.Success -> {
                                    stateHandler?.showDeleted()
                                    requireView().snackBarOrange(SUCCESSFULLY_REMOVED)
                                }
                            }
                        }
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
        adapter = null
        stateHandler = null
        _binding = null
    }

}