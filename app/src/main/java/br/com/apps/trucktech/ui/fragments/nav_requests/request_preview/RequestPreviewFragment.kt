package br.com.apps.trucktech.ui.fragments.nav_requests.request_preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRequestPreviewBinding
import br.com.apps.trucktech.expressions.getColorStateListById
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_requests.request_preview.private_adapter.RequestPreviewRecyclerAdapter
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
    private val viewModel: RequestPreviewFragmentViewModel by viewModel { parametersOf(args.requestId) }
    private var adapter: RequestPreviewRecyclerAdapter? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData()
    }

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
                    true
                }

                else -> false
            }
        }
    }

    /**
     * State manager is set here
     */
    private fun initStateManager() {
        viewModel.loadedPaymentRequest.observe(viewLifecycleOwner) {
            it?.let { resource ->
                if (resource.error == null) {
                    bind(resource.data)
                    resource.data.itemsList?.let { dataSet -> adapter?.update(dataSet) }
                } else {
                    requireView().snackBarRed(resource.error!!)
                }
            }
        }
    }

    private fun bind(request: PaymentRequest) {
        val description = viewModel.getDescriptionText(request)
        binding.fragmentRequestPreviewDescription.text = description
    }

    /**
     * Menu Items is set Here.
     */
    private fun prepareMenuItems() {
        binding.fragmentRequestPreviewToolbar.toolbar.menu.apply {
            findItem(R.id.menu_preview_delete).apply {
                val color = requireContext().getColorStateListById(R.color.dark_grey)
                iconTintList = color
            }
            findItem(R.id.menu_preview_edit).apply {
                val color = requireContext().getColorStateListById(R.color.dark_grey)
                iconTintList = color
            }
        }
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