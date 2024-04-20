package br.com.apps.trucktech.ui.fragments.nav_travel.cost.cost_preview

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentCostPreviewBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.navigateWithSafeArgs
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.toBold
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.trucktech.expressions.toItalic
import br.com.apps.trucktech.expressions.toUnderline
import br.com.apps.trucktech.model.costs.DefaultCost
import br.com.apps.trucktech.sampleCostsList
import br.com.apps.trucktech.ui.fragments.base_fragments.BasePreviewFragment

private const val COLLAPSING_TOOLBAR_URL_IMAGE = "https://www.garbuio.com.br/wp-content/uploads/2022/08/manutencao-de-caminhao.jpg"
private const val REFUEL = "Custo"

/**
 * A simple [Fragment] subclass.
 * Use the [CostPreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CostPreviewFragment : BasePreviewFragment() {

    private var _binding: FragmentCostPreviewBinding? = null
    private val binding get() = _binding!!

    private val args: CostPreviewFragmentArgs by navArgs()
    private lateinit var cost: DefaultCost

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCostPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // INTERFACE
    //---------------------------------------------------------------------------------------------//

    override fun configureBaseFragment(configurator: BasePreviewConfigurator) {
        configurator.collapsingToolbar(
            collapsingToolbar = binding.fragmentCostPreviewCollapsingToolbar.collapsingToolbar,
            toolbar = binding.fragmentCostPreviewCollapsingToolbar.toolbar,
            backgroundImage = binding.fragmentCostPreviewCollapsingToolbar.collapsingToolbarImage,
            urlImage = COLLAPSING_TOOLBAR_URL_IMAGE,
            title = REFUEL,
            titleExpandedColor = R.color.white,
            titleCollapsedColor = R.color.white,
        )
    }

    override fun loadData() {
        cost = sampleCostsList.first{ it.id == args.costId }
    }

    override fun bind() {
        binding.apply {
            fragmentCostPreviewName.text = cost.description
            fragmentCostPreviewValue.text = cost.value.toCurrencyPtBr()

            val dateFormatted =
                SpannableString(cost.date.getMonthAndYearInPtBr())
                    .toBold()
                    .toItalic()
                    .toUnderline()

            fragmentCostPreviewDescription.text = buildSpannedString {
                append("No dia ")
                append(dateFormatted)
                append(" você fez o serviço.")
            }
        }
    }

    override fun onDeleteClick() {
        requireView().popBackStack()
    }

    override fun onEditCLickDirection() {
        requireView().navigateWithSafeArgs(CostPreviewFragmentDirections.actionCostPreviewFragmentToCostEditorFragment(cost.id))
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}