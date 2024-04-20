package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_preview

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRefuelPreviewBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.navigateWithSafeArgs
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.toBold
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.trucktech.expressions.toItalic
import br.com.apps.trucktech.expressions.toUnderline
import br.com.apps.trucktech.model.refuel.ReFuel
import br.com.apps.trucktech.sampleReFuelLists
import br.com.apps.trucktech.ui.fragments.base_fragments.BasePreviewFragment

private const val COLLAPSING_TOOLBAR_URL_IMAGE = "https://i0.wp.com/blogdocaminhoneiro.com/wp-content/uploads/2020/04/diesel-abastecendo-caminhao-1.jpg?ssl=1"
private const val REFUEL = "Abastecimento"
/**
 * A simple [Fragment] subclass.
 * Use the [RefuelPreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RefuelPreviewFragment : BasePreviewFragment() {

    private var _binding : FragmentRefuelPreviewBinding? = null
    private val binding get() = _binding!!

    private val args: RefuelPreviewFragmentArgs by navArgs()
    private lateinit var refuel: ReFuel

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

    override fun loadData() {
        refuel = sampleReFuelLists.first { it.id == args.refuelId }

    }

    override fun bind() {
        binding.apply {
            fragmentRefuelPreviewStation.text = refuel.fuelStationName
            fragmentRefuelPreviewValue.text = refuel.getTotalValue()?.toCurrencyPtBr()

            val dateFormatted =
                SpannableString(refuel.date.getMonthAndYearInPtBr())
                    .toBold()
                    .toItalic()
                    .toUnderline()

            fragmentRefuelPreviewDescription.text = buildSpannedString {
                append("No dia ")
                append(dateFormatted)
                append(" você abasteceu.")
            }
        }
    }

    override fun onDeleteClick() {
        requireView().popBackStack()
    }

    override fun onEditCLickDirection() {
        requireView().navigateWithSafeArgs(RefuelPreviewFragmentDirections.actionRefuelPreviewFragmentToRefuelEditorFragment(args.refuelId))
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}