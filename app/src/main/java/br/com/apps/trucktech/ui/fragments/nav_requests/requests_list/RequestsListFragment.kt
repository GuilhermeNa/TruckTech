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
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.repository.util.CANCEL
import br.com.apps.repository.util.FAILED_TO_REMOVE
import br.com.apps.repository.util.FAILED_TO_SAVE
import br.com.apps.repository.util.OK
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRequestsListBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.activities.main.VisualComponents
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_requests.requests_list.private_adapter.RequestsListRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.DateRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.HeaderDefaultHorizontalAdapter
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
            permission = mainActVM.loggedUser.permissionLevelType
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
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateHandler = RequestsListState(binding)
        initSwipeRefresh()
        initHeaderRecyclerView()
        initStateManager()
        initFab()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentRequestsListToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentRequestsListToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadData()
        }
    }

    /**
     * Header Recycler is configured here
     */
    private fun initHeaderRecyclerView() {
        val recyclerView = binding.fragmentRequestsListHeaderRecycler
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
        binding.fragmentRequestsListFab.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        viewModel.dialogRequested()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nova requisição")
            .setMessage("Você confirma a adição uma nova requisição?")
            .setPositiveButton("Ok") { _, _ -> createNewRequest() }
            .setNegativeButton("Cancelar") { _, _ -> }
            .setOnDismissListener { viewModel.dialogDismissed() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun createNewRequest() {
        lifecycleScope.launch {
            viewModel.save().asFlow().collect { response ->
                when (response) {
                    is Response.Error -> requireView().snackBarRed(FAILED_TO_SAVE)
                    is Response.Success -> {
                        response.data?.let {
                            requireView().snackBarGreen(REQUEST_HAVE_BEEN_SAVED)
                            requireView()
                                .navigateTo(
                                    RequestsListFragmentDirections
                                        .actionRequestsListFragmentToRequestEditorFragment(it)
                                )
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
            binding.swipeRefresh.isRefreshing = false
            data?.let { requests ->
                if (adapter == null) {
                    initRequestRecyclerView()
                } else {
                    adapter?.update(requests)
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                State.Loading -> stateHandler?.showLoading()
                State.Loaded -> stateHandler?.showLoaded()
                State.Empty -> stateHandler?.showEmpty()
                is State.Error -> stateHandler?.showError(state.error)
                State.Deleting -> stateHandler?.showDeleting()
                State.Deleted -> stateHandler?.showDeleted()
            }
        }

        viewModel.dialog.observe(viewLifecycleOwner) { dialog ->
            when (dialog) {
                true -> {
                    binding.fragRequestListDarkLayer.visibility = View.VISIBLE
                    mainActVM.setComponents(VisualComponents(hasBottomNavigation = false))
                }

                false -> {
                    binding.fragRequestListDarkLayer.visibility = View.GONE
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

    private fun createAdapters(itemsMap: Map.Entry<String, List<PaymentRequest>>) =
        listOf(
            DateRecyclerAdapter(requireContext(), listOf(itemsMap.key)),
            RequestsListRecyclerAdapter(
                requireContext(),
                itemsMap.value,
                itemCLickListener = { requestId ->
                    requireView().navigateTo(
                        RequestsListFragmentDirections.actionRequestsListFragmentToRequestPreviewFragment(
                            requestId
                        )
                    )
                },
                deleteClickListener = ::showAlertDialogForDelete
            )
        )

    private fun initAdaptersData(dataSet: List<PaymentRequest>): List<RecyclerView.Adapter<out RecyclerView.ViewHolder>> {
        return dataSet
            .sortedBy { it.date }
            .reversed()
            .groupBy {
                it.date?.getMonthAndYearInPtBr() ?: throw InvalidParameterException("Date is null")
            }
            .map { createAdapters(it) }
            .flatten()
    }

    private fun initConcatAdapter(adapters: List<RecyclerView.Adapter<out RecyclerView.ViewHolder>>) {
        val concatAdapter = ConcatAdapter(adapters)
        val recyclerView = binding.fragmentRequestsListRecycler
        recyclerView.adapter = concatAdapter
    }

    private fun showAlertDialogForDelete(request: PaymentRequest) {
        viewModel.dialogRequested()

        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle("Apagando requisição")
            .setMessage("Você realmente deseja apagar esta requisição e seus itens permanentemente?")
            .setPositiveButton(OK) { _, _ -> deleteRequest(request) }
            .setNegativeButton(CANCEL) { _, _ -> }
            .setOnDismissListener { viewModel.dialogDismissed() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun deleteRequest(request: PaymentRequest) {
        lifecycleScope.launch {
            viewModel.delete(request).asFlow().collect { response ->
                when (response) {
                    is Response.Error -> requireView().snackBarRed(FAILED_TO_REMOVE)
                    is Response.Success -> {
                        viewModel.loadData()
                        requireView().snackBarOrange(SUCCESSFULLY_REMOVED)
                    }
                }
            }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON RESUME
    //---------------------------------------------------------------------------------------------//

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        binding.swipeRefresh.isRefreshing = false
        adapter = null
        stateHandler = null
        _binding = null
    }

}