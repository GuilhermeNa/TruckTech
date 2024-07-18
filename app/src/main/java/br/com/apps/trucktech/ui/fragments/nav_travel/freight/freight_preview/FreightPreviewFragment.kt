package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview

import android.os.Bundle
import android.text.SpannableString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.CANCEL
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.repository.util.FAILED_TO_REMOVE
import br.com.apps.repository.util.OK
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentFreightPreviewBinding
import br.com.apps.model.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.expressions.toBold
import br.com.apps.model.expressions.toCurrencyPtBr
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
    private var stateHandler: FreightPreviewState? = null

    private val args: FreightPreviewFragmentArgs by navArgs()
    private val vmData by lazy {
        FreightPreviewVmData(
            freightId = args.freightId,
            permission = mainActVM.loggedUser.permissionLevelType
        )
    }
    private val viewModel: FreightPreviewViewModel by viewModel { parametersOf(vmData) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFreightPreviewBinding.inflate(inflater, container, false)
        stateHandler = FreightPreviewState(requireContext(), binding)
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
            title = FREIGHT
        )
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *  - Observes freightData to bind the [Freight].
     *  - Observes darkLayer to manage the interaction.
     *
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    response.exception.printStackTrace()
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                }
                is Response.Success -> response.data?.let { bind(it) }
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

    private fun bind(freight: Freight) {
        binding.apply {
            fragmentFreightPreviewClient.text = freight.customer?.name
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
        showDeleteDialog()
    }

    private fun showDeleteDialog() {
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
        requireView().navigateTo(
            FreightPreviewFragmentDirections
                .actionFreightPreviewFragmentToFreightEditorFragment(args.freightId, args.travelId)
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