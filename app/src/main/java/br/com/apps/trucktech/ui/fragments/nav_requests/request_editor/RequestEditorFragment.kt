package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.model.model.request.request.RequestItemType
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRequestEditorBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.private_adapters.RequestEditorRecyclerAdapter
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.private_dialogs.RequestEditorBottomSheet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TOOLBAR_TITLE = "Requisição"

private const val DIRECTION_REFUEL = 0
private const val DIRECTION_COST = 1
private const val DIRECTION_WALLET = 2

private const val ITEM_SUCCESSFULLY_REMOVED = "Item removido com sucesso"
private const val FAILED_TO_REMOVE_ITEM = "Falha ao remover Item"

/**
 * A simple [Fragment] subclass.
 * Use the [RequestEditorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RequestEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRequestEditorBinding? = null
    private val binding get() = _binding!!
    private val args: RequestEditorFragmentArgs by navArgs()
    private val viewModel: RequestEditorFragmentViewModel by viewModel()
    private var adapter: RequestEditorRecyclerAdapter? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.requestId?.let { id ->
            viewModel.requestId = id
            viewModel.loadData()
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
        initRecyclerView()
        initFab()
    }

    private fun initBoxSummary() {
        binding.boxFragRequestPreviewDescription.apply {
            sharedViewModel.userData.value.let {
                it?.let { driverAndUser ->
                    driverAndUser.user?.name?.let { name -> driverField.text = name } ?: "-"
                    driverAndUser.truck?.plate?.let { truckPlate -> plateField.text = truckPlate }
                        ?: "-"
                    valueField.text = "-"
                }
            }
        }
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentRequestEditorToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentRequestEditorToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Recycler view is configured here.
     */
    private fun initRecyclerView() {
        val recyclerView = binding.fragmentRequestEditorRecycler
        adapter = RequestEditorRecyclerAdapter(
            requireContext(),
            emptyList(),
            itemClickListener = { itemCLickData ->
                val direction = when (itemCLickData.type) {
                    RequestItemType.REFUEL ->
                        RequestEditorFragmentDirections
                            .actionRequestEditorFragmentToRequestEditorRefuelFragment(
                                refuelId = itemCLickData.itemId,
                                requestId = args.requestId!!
                            )

                    RequestItemType.COST ->
                        RequestEditorFragmentDirections
                            .actionRequestEditorFragmentToRequestEditorCostFragment(
                                costId = itemCLickData.itemId,
                                requestId = args.requestId!!
                            )

                    RequestItemType.WALLET ->
                        RequestEditorFragmentDirections
                            .actionRequestEditorFragmentToRequestEditorWalletFragment(
                                walletId = itemCLickData.itemId,
                                requestId = args.requestId!!
                            )
                }
                requireView().navigateTo(direction)
            },
            deleteClickListener = this::showAlertDialog
        )
        recyclerView.adapter = adapter
    }

    private fun showAlertDialog(itemId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle("Apagando item")
            .setMessage("Você realmente deseja apagar este item permanentemente?")
            .setPositiveButton("Ok") { dialog, _ ->
                viewModel.deleteItem(itemId)
            }
            .setNegativeButton("Cancelar") { _, _ ->
            }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    /**
     * Fab is configured here.
     */
    private fun initFab() {
        binding.fragmentRequestEditorFab.setOnClickListener {
            viewModel.bottomSheetRequested()
        }
    }

    /**
     * State manager is configured here.
     */
    private fun initStateManager() {
        viewModel.loadedRequestData.observe(viewLifecycleOwner) {
            it?.let { resource ->
                val paymentRequest = resource.data
                val requestItems = resource.data.itemsList!!
                val totalValueInPtBr = paymentRequest.getTotalValue().toCurrencyPtBr()

                if (resource.error == null) {
                    adapter?.update(requestItems)
                    binding.boxFragRequestPreviewDescription.valueField.text = totalValueInPtBr
                } else {
                    requireView().snackBarRed(resource.error!!)
                }

            }
        }

        viewModel.bottomSheetVisibility.observe(viewLifecycleOwner) { shouldBeVisible ->
            when (shouldBeVisible) {
                true -> {
                    val bottomSheet = RequestEditorBottomSheet(
                        onClickListener = { result ->
                            when (result) {
                                DIRECTION_REFUEL -> {
                                    requireView().navigateTo(
                                        RequestEditorFragmentDirections.actionRequestEditorFragmentToRequestEditorRefuelFragment(
                                            refuelId = null,
                                            requestId = args.requestId!!
                                        )
                                    )
                                }

                                DIRECTION_COST -> {
                                    requireView().navigateTo(
                                        RequestEditorFragmentDirections.actionRequestEditorFragmentToRequestEditorCostFragment(
                                            costId = null,
                                            requestId = args.requestId!!
                                        )
                                    )
                                }

                                DIRECTION_WALLET -> {
                                    requireView().navigateTo(
                                        RequestEditorFragmentDirections.actionRequestEditorFragmentToRequestEditorWalletFragment(
                                            walletId = null,
                                            requestId = args.requestId!!
                                        )
                                    )
                                }
                            }
                        },
                        onDismissListener = {
                            viewModel.bottomSheetDismissed()
                        }
                    )
                    bottomSheet.show(childFragmentManager, RequestEditorBottomSheet.TAG)
                    binding.fragmentRequestEditorDarkLayer.visibility = VISIBLE
                }

                false -> {
                    binding.fragmentRequestEditorDarkLayer.visibility = GONE
                }
            }
        }

        viewModel.requestItemDeleted.observe(viewLifecycleOwner) {
            it?.let { resource ->
                if (resource.error == null) {

                    requireView().snackBarGreen(ITEM_SUCCESSFULLY_REMOVED)
                } else {
                    requireView().snackBarRed(FAILED_TO_REMOVE_ITEM)
                }
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