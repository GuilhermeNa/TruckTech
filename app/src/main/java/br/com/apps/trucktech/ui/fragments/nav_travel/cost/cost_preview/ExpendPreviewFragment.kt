package br.com.apps.trucktech.ui.fragments.nav_travel.cost.cost_preview

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.CANCEL
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.FAILED_TO_REMOVE
import br.com.apps.repository.OK
import br.com.apps.repository.Response
import br.com.apps.repository.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentExpendPreviewBinding
import br.com.apps.trucktech.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.navigateWithSafeArgs
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.expressions.toCurrencyPtBr
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

    private val args: ExpendPreviewFragmentArgs by navArgs()
    private val viewModel: ExpendPreviewViewModel by viewModel{ parametersOf(args.costId) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpendPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
    }

    override fun configureBaseFragment(configurator: BasePreviewConfigurator) {
        configurator.collapsingToolbar(
            collapsingToolbar = binding.fragmentCostPreviewCollapsingToolbar.collapsingToolbar,
            toolbar = binding.fragmentCostPreviewCollapsingToolbar.toolbar,
            backgroundImage = binding.fragmentCostPreviewCollapsingToolbar.collapsingToolbarImage,
            urlImage = COLLAPSING_TOOLBAR_URL_IMAGE,
            title = EXPEND,
            titleExpandedColor = R.color.white,
            titleCollapsedColor = R.color.white,
        )
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *  - Observes expendData to bind the [Expend].
     *
     */
    private fun initStateManager() {
        viewModel.expendData.observe(viewLifecycleOwner) { response ->
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
    }

    private fun bind(expend: Expend) {
        binding.apply {
            fragmentCostPreviewCompany.text = expend.company
            fragmentCostPreviewValue.text = expend.value?.toCurrencyPtBr()
            fragmentCostPreviewDate.text = expend.date?.getCompleteDateInPtBr()
            fragmentCostPreviewDescription.text = expend.description
        }
    }

    /**
     * Try to delete the [Expend].
     *  1. Displays a confirmation dialog to delete the [Expend].
     *  2. Sends a request to the [viewModel] to delete the [Expend].
     */
    override fun onDeleteMenuClick() { showAlertDialog() }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle(DIALOG_TITLE)
            .setMessage(DIALOG_MESSAGE)
            .setPositiveButton(OK) { _, _ -> deleteExpend() }
            .setNegativeButton(CANCEL) { _, _ -> }
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
                    clearMenu()
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
        requireView().navigateWithSafeArgs(
            ExpendPreviewFragmentDirections
                .actionCostPreviewFragmentToCostEditorFragment(args.costId, args.travelId)
        )
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}