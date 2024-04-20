package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentFreightPreviewBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.toBold
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.trucktech.expressions.toItalic
import br.com.apps.trucktech.expressions.toUnderline
import br.com.apps.trucktech.model.freight.Freight
import br.com.apps.trucktech.sampleFreightList
import br.com.apps.trucktech.ui.fragments.base_fragments.BasePreviewFragment

private const val COLLAPSING_TOOLBAR_URL_IMAGE =
    "https://www.datamex.com.br/blog/wp-content/uploads/2018/10/248950-frete-de-retorno-9-dicas-para-facilitar-esse-processo.jpg"

private const val FREIGHT = "Frete"

/**
 * A simple [Fragment] subclass.
 * Use the [FreightPreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FreightPreviewFragment : BasePreviewFragment() {

    private var _binding: FragmentFreightPreviewBinding? = null
    private val binding get() = _binding!!

    private val args: FreightPreviewFragmentArgs by navArgs()
    private lateinit var freight: Freight

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
    // INTERFACE
    //---------------------------------------------------------------------------------------------//

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

    override fun loadData() {
        freight = sampleFreightList.first { it.id == args.freightId }
    }

    override fun bind() {
        binding.apply {
            fragmentFreightPreviewClient.text = freight.company
            fragmentFreightPreviewValue.text = freight.value.toCurrencyPtBr()
            fragmentFreightPreviewBreakDown.text = freight.breakDown?.toCurrencyPtBr() ?: "-"

            val dateFormatted =
                SpannableString(freight.date.getMonthAndYearInPtBr())
                    .toBold()
                    .toItalic()
                    .toUnderline()

            val cargoFormatted =
                SpannableString(freight.cargo)
                    .toBold()
                    .toItalic()
                    .toUnderline()

            val originFormatted =
                SpannableString(freight.origin)
                    .toBold()
                    .toItalic()
                    .toUnderline()

            val destinyFormatted =
                SpannableString(freight.destiny)
                    .toBold()
                    .toItalic()
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

    override fun onDeleteClick() {
        requireView().popBackStack()
    }

    override fun onEditCLickDirection() {
        requireView()
            .navigateTo(
                FreightPreviewFragmentDirections.actionFreightPreviewFragmentToFreightEditorFragment(
                    args.freightId
                )
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