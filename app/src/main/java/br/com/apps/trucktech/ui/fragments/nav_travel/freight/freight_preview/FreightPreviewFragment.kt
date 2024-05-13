package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview

import android.os.Bundle
import android.text.SpannableString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.CANCEL
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.FAILED_TO_REMOVE
import br.com.apps.repository.OK
import br.com.apps.repository.Response
import br.com.apps.repository.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentFreightPreviewBinding
import br.com.apps.trucktech.expressions.getColorById
import br.com.apps.trucktech.expressions.getColorStateListById
import br.com.apps.trucktech.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.navigateTo
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
    "https://www.datamex.com.br/blog/wp-content/uploads/2018/10/248950-frete-de-retorno-9-dicas-para-facilitar-esse-processo.jpg"
private const val FREIGHT = "Frete"
private const val DIALOG_TITLE = "Removendo Frete."
private const val DIALOG_MESSAGE = "Você realmente deseja apagar este frete permanentemente?"

/**
 * A simple [Fragment] subclass.
 * Use the [FreightPreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FreightPreviewFragment : BasePreviewFragment() {

    private var _binding: FragmentFreightPreviewBinding? = null
    private val binding get() = _binding!!

    private val args: FreightPreviewFragmentArgs by navArgs()
    private val viewModel: FreightPreviewViewModel by viewModel { parametersOf(args.freightId) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFreightPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        onPrepareMenu()
    }

    override fun configureBaseFragment(configurator: BasePreviewConfigurator) {
        configurator.collapsingToolbar(
            collapsingToolbar = binding.fragmentFreightPreviewCollapsingToolbar.collapsingToolbar,
            toolbar = binding.fragmentFreightPreviewCollapsingToolbar.toolbar,
            backgroundImage = binding.fragmentFreightPreviewCollapsingToolbar.collapsingToolbarImage,
            urlImage = COLLAPSING_TOOLBAR_URL_IMAGE,
            title = FREIGHT,
            titleExpandedColor = R.color.white,
            titleCollapsedColor = R.color.white
        )

    }

    private fun onPrepareMenu() {
        binding.fragmentFreightPreviewCollapsingToolbar.toolbar.apply {
            navigationIcon?.setTint(requireContext().getColorById(R.color.white))

            menu.apply {
                val iconDelete = findItem(R.id.menu_preview_delete)
                val iconEdit = findItem(R.id.menu_preview_edit)

                iconDelete.iconTintList = requireContext().getColorStateListById(R.color.white)
                iconEdit.iconTintList = requireContext().getColorStateListById(R.color.white)
            }

        }
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *  - Observes freightData to bind the [Freight].
     *  - Observes darkLayer to manage the interaction.
     *
     */
    private fun initStateManager() {
        viewModel.freightData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                is Response.Success -> response.data?.let { bind(it) }
            }
        }

        viewModel.darkLayer.observe(viewLifecycleOwner) { isRequested ->
            when (isRequested) {
                true -> binding.fragFreightPreviewDarkLayer.visibility = VISIBLE
                false -> binding.fragFreightPreviewDarkLayer.visibility = GONE
            }
        }

    }

    private fun bind(freight: Freight) {
        binding.apply {
            fragmentFreightPreviewClient.text = freight.company
            fragmentFreightPreviewValue.text = freight.value?.toCurrencyPtBr()
            fragmentFreightPreviewBreakDown.text = freight.breakDown?.toCurrencyPtBr() ?: "-"
            fragmentFreightPreviewDaily.text = freight.daily?.toString() ?: "-"
            fragFreightPreviewDailyValue.text = freight.dailyTotalValue?.toCurrencyPtBr() ?: "-"

            val dateFormatted =
                SpannableString(freight.loadingDate?.getCompleteDateInPtBr())
                    .toBold()
                    .toUnderline()

            val cargoFormatted =
                SpannableString(freight.cargo)
                    .toBold()
                    .toUnderline()

            val originFormatted =
                SpannableString(freight.origin)
                    .toBold()
                    .toUnderline()

            val destinyFormatted =
                SpannableString(freight.destiny)
                    .toBold()
                    .toUnderline()

            fragmentFreightPreviewDescription.text = buildSpannedString {
                append("No dia ")
                append(dateFormatted)
                append(" você carregou ")
                append(cargoFormatted)
                append(" de ")
                append(originFormatted)
                append(" para ")
                append(destinyFormatted)
                append(".")
            }

        }
    }

    /**
     * Try to delete the [Freight].
     *  1. Displays a confirmation dialog to delete the [Freight].
     *  2. Sends a request to the [viewModel] to delete the [Freight].
     */
    override fun onDeleteMenuClick() {
        showAlertDialog()
    }

    private fun showAlertDialog() {
        viewModel.requestDarkLayer()

        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle(DIALOG_TITLE)
            .setMessage(DIALOG_MESSAGE)
            .setPositiveButton(OK) { _, _ -> deleteFreight() }
            .setNegativeButton(CANCEL) { _, _ -> }
            .setOnDismissListener { viewModel.dismissDarkLayer() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }

    }

    private fun deleteFreight() {
        viewModel.delete().observe(viewLifecycleOwner) { response ->
            when (response) {
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
        requireView().navigateTo(
            FreightPreviewFragmentDirections
                .actionFreightPreviewFragmentToFreightEditorFragment(args.freightId, args.travelId)
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