package br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_preview

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.util.CANCEL
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.repository.util.FAILED_TO_REMOVE
import br.com.apps.repository.util.OK
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentExpendPreviewBinding
import br.com.apps.model.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.navigateWithSafeArgs
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.trucktech.ui.fragments.base_fragments.BasePreviewFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val COLLAPSING_TOOLBAR_URL_IMAGE = "https://www.garbuio.com.br/wp-content/uploads/2022/08/manutencao-de-caminhao.jpg"
private const val EXPEND = "Despesa"
private const val DIALOG_TITLE = "Removendo despesa"
private const val DIALOG_MESSAGE = "Você realmente deseja remover esta despesa permanentemente?"

class ExpendPreviewFragment : BasePreviewFragment() {

    private var _binding: FragmentExpendPreviewBinding? = null
    private val binding get() = _binding!!
    private var stateHandler: ExpendPreviewState? = null

    private val args: ExpendPreviewFragmentArgs by navArgs()
    private val vmData by lazy {
        ExpendPreviewVmData(
            expendId = args.costId,
            permission = mainActVM.loggedUser.permissionLevelType
        )
    }
    private val viewModel: ExpendPreviewViewModel by viewModel{ parametersOf(vmData) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpendPreviewBinding.inflate(inflater, container, false)
        stateHandler = ExpendPreviewState(binding, requireContext())
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initFabs()
    }

    override fun configureBaseFragment(configurator: BasePreviewConfigurator) {
        configurator.collapsingToolbar(
            collapsingToolbar = binding.collapsingToolbar,
            toolbar = binding.toolbar,
            backgroundImage = binding.collapsingToolbarImage,
            urlImage = COLLAPSING_TOOLBAR_URL_IMAGE,
            title = EXPEND,
        )
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *  - Observes expendData to bind the [Expend].
     *
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Error -> {
                    response.exception.printStackTrace()
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                }
                is Response.Success -> {
                    response.data?.let { bind(it) }
                }
            }
        }

        viewModel.darkLayer.observe(viewLifecycleOwner) { isRequested ->
            stateHandler?.hasDarkLayer(isRequested)
        }

        viewModel.writeAuth.observe(viewLifecycleOwner) { hasAuth ->
            stateHandler?.hideWriteOptions()
            var isExpanded = false
            var scrollRange = -1

            binding.appBar.addOnOffsetChangedListener { appBar, yOffSet ->
                if (scrollRange == -1) scrollRange = -appBar.totalScrollRange

                when {
                    (yOffSet > scrollRange) && !isExpanded -> {
                        stateHandler?.showAppBarExpanded(hasAuth)
                        isExpanded = true
                    }

                    (yOffSet == scrollRange) && isExpanded -> {
                        stateHandler?.showAppBarCollapsed(hasAuth)
                        isExpanded = false
                    }

                }
            }
            viewModel.writeAuth.removeObservers(viewLifecycleOwner)
        }

    }

    private fun bind(expend: Expend) {
        binding.apply {
            fragmentCostPreviewCompany.text = expend.company
            fragmentCostPreviewValue.text = expend.value.toCurrencyPtBr()
            fragmentCostPreviewDate.text = expend.date.getCompleteDateInPtBr()
            fragmentCostPreviewDescription.text = expend.description
        }
    }

    /**
     * Try to delete the [Expend].
     *  1. Displays a confirmation dialog to delete the [Expend].
     *  2. Sends a request to the [viewModel] to delete the [Expend].
     */
    override fun onDeleteMenuClick() { showDeleteDialog() }

    private fun showDeleteDialog() {
        viewModel.requestDarkLayer()

        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle(DIALOG_TITLE)
            .setMessage(DIALOG_MESSAGE)
            .setPositiveButton(OK) { _, _ -> deleteExpend() }
            .setNegativeButton(CANCEL) { _, _ -> }
            .setOnDismissListener { viewModel.dismissDarkLayer() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun deleteExpend() {
        viewModel.delete().observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_REMOVE)
                is Response.Success -> {
                    requireView().snackBarOrange(SUCCESSFULLY_REMOVED)
                    requireView().popBackStack()
                }
            }
        }
    }

    /**
     * Navigate to the editor fragment.
     */
    override fun onEditMenuCLick() {
        onEditClick()
    }

    private fun onEditClick() {
        requireView().navigateWithSafeArgs(
            ExpendPreviewFragmentDirections
                .actionCostPreviewFragmentToCostEditorFragment(args.costId, args.travelId)
        )
    }

    private fun initFabs() {
        binding.fabDelete.setOnClickListener { showDeleteDialog() }
        binding.fabEdit.setOnClickListener { onEditClick() }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        stateHandler = null
        _binding = null
    }

}