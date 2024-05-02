package br.com.apps.trucktech.ui.fragments.nav_requests.requests_list

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.repository.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.databinding.FragmentRequestsListBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_requests.requests_list.private_adapter.RequestsListRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.DateRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.HeaderDefaultHorizontalAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.security.InvalidParameterException

private const val TOOLBAR_TITLE = "Solicitação de pagamento"

private const val REQUEST_HAVE_BEEN_SAVED = "Requisição adicionada"

/**
 * A simple [Fragment] subclass.
 * Use the [RequestsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RequestsListFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestsListBinding? = null
    private val binding get() = _binding!!

    private val driverAndTruck by lazy {
        sharedViewModel.userData.value
    }
    private val viewModel: RequestsListFragmentViewModel by viewModel { parametersOf(driverAndTruck) }
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
     * Fab is configured here
     */
    private fun initFab() {
        binding.fragmentRequestsListFab.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nova requisição")
            .setMessage("Você confirma a adição uma nova requisição?")
            .setPositiveButton("Ok") { _, _ -> createNewRequest() }
            .setNegativeButton("Cancelar") { _, _ -> }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun createNewRequest() {
        lifecycleScope.launch {
            viewModel.createNewRequest().asFlow().collect { resource ->
                if (resource.error == null) {
                    requireView().snackBarGreen(REQUEST_HAVE_BEEN_SAVED)
                    requireView().navigateTo(
                        RequestsListFragmentDirections.actionRequestsListFragmentToRequestEditorFragment(
                            resource.data
                        )
                    )
                } else {
                    requireView().snackBarRed(resource.error!!)
                }
            }

        }
    }

    /**
     * State manager is configured here.
     */
    private fun initStateManager() {
        viewModel.loadedRequestsData?.observe(viewLifecycleOwner) {
            it?.let { resource ->
                if (resource.error == null) {
                    if (adapter == null) {
                        initRequestRecyclerView()
                    } else {
                        adapter?.update(resource.data)
                    }
                } else {
                    requireView().snackBarRed(resource.error!!)
                }
            }
        }
    }

    /**
     * Requests recycler view is configured here
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
                deleteClickListener = ::deleteRequest
            )
        )

    private fun deleteRequest(requestId: String, itemsIdList: List<String>?) {
        lifecycleScope.launch {
            viewModel.delete(requestId, itemsIdList).asFlow().collect { resource ->
                if (resource.error == null) {
                    requireView().snackBarOrange(SUCCESSFULLY_REMOVED)
                } else {
                    requireView().snackBarRed(resource.error!!)
                }
            }
        }
    }

    private fun initConcatAdapter(adapters: List<RecyclerView.Adapter<out RecyclerView.ViewHolder>>) {
        val concatAdapter = ConcatAdapter(adapters)
        val recyclerView = binding.fragmentRequestsListRecycler
        recyclerView.adapter = concatAdapter
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