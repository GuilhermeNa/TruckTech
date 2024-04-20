package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freights_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.databinding.FragmentFreightsListBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.model.freight.Freight
import br.com.apps.trucktech.sampleFreightList
import br.com.apps.trucktech.ui.KEY_ID
import br.com.apps.trucktech.ui.PAGE_FREIGHT
import br.com.apps.trucktech.ui.fragments.nav_travel.records.RecordsFragmentViewModel
import br.com.apps.trucktech.ui.public_adapters.DateRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.RecordsItemRecyclerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 * Use the [FreightsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FreightsListFragment : Fragment() {

    private var _binding: FragmentFreightsListBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by viewModels<RecordsFragmentViewModel>({requireParentFragment()})
    private val viewModel: FreightsListFragmentViewModel by viewModel { parametersOf(sharedViewModel.travelId) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFreightsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listSorted = initAdapters()
        initRecyclerView(listSorted)
    }

    private fun initAdapters(): List<RecyclerView.Adapter<out RecyclerView.ViewHolder>> {
        return sampleFreightList
            .sortedBy { it.date }
            .reversed()
            .groupBy { it.date.getMonthAndYearInPtBr() }
            .map { createAdapters(it) }
            .flatten()
    }

    private fun createAdapters(itemsMap: Map.Entry<String, List<Freight>>) =
        listOf(
            DateRecyclerAdapter(requireContext(), listOf(itemsMap.key)),
            RecordsItemRecyclerAdapter(
                requireContext(),
                itemsMap.value,
                itemCLickListener = { id ->
                    val bundle = Bundle()
                    bundle.putString(KEY_ID, id)
                    parentFragmentManager.setFragmentResult(PAGE_FREIGHT, bundle)
                })
        )

    private fun initRecyclerView(adapters: List<RecyclerView.Adapter<out RecyclerView.ViewHolder>>) {
        val concatAdapter = ConcatAdapter(adapters)
        val recyclerView = binding.freightFragmentRecycler
        recyclerView.adapter = concatAdapter
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}