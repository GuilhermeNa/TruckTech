package br.com.apps.trucktech.ui.fragments.nav_requests.request_preview

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.repository.util.CANCEL
import br.com.apps.repository.util.FAILED_TO_REMOVE
import br.com.apps.repository.util.OK
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_REMOVED
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRequestPreviewBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_requests.request_preview.private_adapter.RequestPreviewRecyclerAdapter
import br.com.apps.trucktech.util.state.State
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Requisição"

/**
 * A simple [Fragment] subclass.
 * Use the [RequestPreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RequestPreviewFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestPreviewBinding? = null
    private val binding get() = _binding!!
    private var stateHandler: RequestPreviewState? = null

    private val args: RequestPreviewFragmentArgs by navArgs()
    private val vmData by lazy {
        RequestPreviewVmData(
            requestId = args.requestId,
            permission = mainActVM.loggedUser.permissionLevelType
        )
    }
    private val viewModel: RequestPreviewViewModel by viewModel { parametersOf(vmData) }
    private var adapter: RequestPreviewRecyclerAdapter? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestPreviewBinding.inflate(inflater, container, false)
        stateHandler = RequestPreviewState(binding)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initRecyclerView()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        val toolbar = binding.fragmentRequestPreviewToolbar.toolbar
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = toolbar,
            menuId = R.menu.menu_preview,
            toolbarTextView = binding.fragmentRequestPreviewToolbar.toolbarText,
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
                        RequestPreviewFragmentDirections.actionRequestPreviewFragmentToRequestEditorFragment(
                            args.requestId
                        )
                    )
                    true
                }

                R.id.menu_preview_delete -> {
                    showAlertDialogForDelete()
                    true
                }

                else -> false
            }
        }
    }

    private fun showAlertDialogForDelete() {
        viewModel.requestDarkLayer()

        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle("Apagando requisição")
            .setMessage("Você realmente deseja apagar esta requisição e seus itens permanentemente?")
            .setPositiveButton(OK) { _, _ -> delete() }
            .setNegativeButton(CANCEL) { _, _ -> }
            .setOnDismissListener { viewModel.dismissDarkLayer() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun delete() {
        viewModel.delete().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    requireView().snackBarRed(FAILED_TO_REMOVE)
                    Log.e(TAG_DEBUG, response.exception.message.toString())
                }

                is Response.Success -> {
                    requireView().snackBarOrange(SUCCESSFULLY_REMOVED)
                    requireView().popBackStack()
                }
            }
        }
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes requestData for update the recyclerView and bind.
     *   - Observes dark layer to manage the interaction.
     */
    private fun initStateManager() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is State.Loading -> stateHandler?.showLoading()
                is State.Loaded -> stateHandler?.showLoaded()
                is State.Error -> stateHandler?.showError(state.error)
                else -> {}
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { data ->
            data?.let { request ->
                request.itemsList?.let { adapter?.update(it) }
                bind(request)
            }
        }

        viewModel.menu.observe(viewLifecycleOwner) { hasMenu ->
            when (hasMenu) {
                true -> stateHandler?.showWriteOptions()
                false -> stateHandler?.hideWriteOptions()
            }
        }

        viewModel.darkLayer.observe(viewLifecycleOwner) { isRequested ->
            when (isRequested) {
                true -> stateHandler?.showDarkLayer()
                false -> stateHandler?.hideDarkLayer()
            }
        }

    }

    private fun bind(request: PaymentRequest) {
        val description = viewModel.getDescriptionText(request)
        binding.fragmentRequestPreviewDescription.text = description
    }

    /**
     * RecyclerView is set Here.
     */
    private fun initRecyclerView() {
        val recyclerView = binding.fragmentRequestPreviewRecycler
        adapter = RequestPreviewRecyclerAdapter(requireContext(), emptyList())
        recyclerView.adapter = adapter

        val divider = DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        stateHandler = null
        adapter = null
        _binding = null
    }

}