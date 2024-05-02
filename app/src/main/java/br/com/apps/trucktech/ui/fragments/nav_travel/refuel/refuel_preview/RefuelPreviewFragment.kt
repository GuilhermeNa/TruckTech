package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_preview

import android.os.Bundle
import android.text.SpannableString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.navigation.fragment.navArgs
import br.com.apps.model.model.travel.Refuel
import br.com.apps.repository.CANCEL
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.FAILED_TO_REMOVE
import br.com.apps.repository.OK
import br.com.apps.repository.Response
import br.com.apps.repository.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRefuelPreviewBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.navigateWithSafeArgs
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.expressions.toBold
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.trucktech.expressions.toUnderline
import br.com.apps.trucktech.ui.fragments.base_fragments.BasePreviewFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val COLLAPSING_TOOLBAR_URL_IMAGE =
    "https://i0.wp.com/blogdocaminhoneiro.com/wp-content/uploads/2020/04/diesel-abastecendo-caminhao-1.jpg?ssl=1"
private const val REFUEL = "Abastecimento"
private const val DIALOG_TITLE = "Removendo abastecimento"
private const val DIALOG_MESSAGE =
    "Você realmente deseja remover este abastecimento permanentemente?"

class RefuelPreviewFragment : BasePreviewFragment() {

    private var _binding: FragmentRefuelPreviewBinding? = null
    private val binding get() = _binding!!

    private val args: RefuelPreviewFragmentArgs by navArgs()
    private val viewModel: RefuelPreviewViewModel by viewModel { parametersOf(args.refuelId) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRefuelPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // INTERFACE
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
    }

    override fun configureBaseFragment(configurator: BasePreviewConfigurator) {
        configurator.collapsingToolbar(
            collapsingToolbar = binding.fragmentRefuelPreviewCollapsingToolbar.collapsingToolbar,
            toolbar = binding.fragmentRefuelPreviewCollapsingToolbar.toolbar,
            backgroundImage = binding.fragmentRefuelPreviewCollapsingToolbar.collapsingToolbarImage,
            urlImage = COLLAPSING_TOOLBAR_URL_IMAGE,
            title = REFUEL,
            titleExpandedColor = R.color.white,
            titleCollapsedColor = R.color.white,
        )
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *  - Observes refuelData to bind the [Refuel].
     *
     */
    private fun initStateManager() {
        viewModel.refuelData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                is Response.Success -> response.data?.let { bind(it) }
                else -> {}
            }
        }
    }

    fun bind(refuel: Refuel) {
        binding.apply {
            fragmentRefuelPreviewStation.text = refuel.station
            fragmentRefuelPreviewValue.text = refuel.totalValue?.toCurrencyPtBr()

            val dateFormatted =
                SpannableString(refuel.date?.getMonthAndYearInPtBr()).toBold().toUnderline()

            fragmentRefuelPreviewDescription.text = buildSpannedString {
                append("No dia ")
                append(dateFormatted)
                append(" você abasteceu.")
            }
        }
    }

    /**
     * Try to delete the [Refuel].
     *  1. Displays a confirmation dialog to delete the [Refuel].
     *  2. Sends a request to the [viewModel] to delete the [Refuel].
     */
    override fun onDeleteMenuClick() {
        showAlertDialog()
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle(DIALOG_TITLE)
            .setMessage(DIALOG_MESSAGE)
            .setPositiveButton(OK) { _, _ -> deleteRefuel() }
            .setNegativeButton(CANCEL) { _, _ -> }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun deleteRefuel() {
        viewModel.delete().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_REMOVE)
                is Response.Success -> {
                    clearMenu()
                    requireView().snackBarOrange(SUCCESSFULLY_REMOVED)
                    requireView().popBackStack()

                }

                else -> {}
            }
        }
    }

    /**
     * Navigate to the editor fragment.
     */
    override fun onEditMenuCLick() {
        requireView().navigateWithSafeArgs(
            RefuelPreviewFragmentDirections
                .actionRefuelPreviewFragmentToRefuelEditorFragment(args.refuelId, args.travelId)
        )
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}