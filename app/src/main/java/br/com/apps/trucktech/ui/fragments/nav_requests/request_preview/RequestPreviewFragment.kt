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
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.repository.CANCEL
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.FAILED_TO_REMOVE
import br.com.apps.repository.OK
import br.com.apps.repository.Response
import br.com.apps.repository.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentRequestPreviewBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_requests.request_preview.private_adapter.RequestPreviewRecyclerAdapter
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

    private val args: RequestPreviewFragmentArgs by navArgs()
    private val viewModel: RequestPreviewViewModel by viewModel { parametersOf(args.requestId) }
    private var adapter: RequestPreviewRecyclerAdapter? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestPreviewBinding.inflate(inflater, container, false)
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
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentRequestPreviewToolbar.toolbar,
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
            when(response) {
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
        viewModel.requestData.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                is Response.Success -> {
                    response.data?.let { request ->
                        request.itemsList?.let { adapter?.update(it) }
                        bind(request)
                    }
                }
            }
        }

        viewModel.darkLayer.observe(viewLifecycleOwner) { isRequested ->
            when (isRequested) {
                true -> binding.fragRequestPreviewDarkLayer.visibility = View.VISIBLE
                false -> binding.fragRequestPreviewDarkLayer.visibility = View.GONE
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
        _binding = null
        adapter = null
    }

}