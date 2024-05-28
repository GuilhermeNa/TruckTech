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
import br.com.apps.model.IdHolder
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.repository.util.NULL_DATE
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentFreightsListBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.KEY_ID
import br.com.apps.trucktech.ui.PAGE_FREIGHT
import br.com.apps.trucktech.ui.fragments.nav_travel.records.RecordsViewModel
import br.com.apps.trucktech.ui.public_adapters.DateRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.RecordsItemRecyclerAdapter
import br.com.apps.trucktech.util.state.State
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.security.InvalidParameterException

class FreightsListFragment : Fragment() {

    private var _binding: FragmentFreightsListBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by viewModels<RecordsViewModel>({ requireParentFragment() })

    private val idHolder by lazy {
        IdHolder(
            masterUid = sharedViewModel.masterUid,
            travelId = sharedViewModel.travelId
        )
    }
    private val viewModel: FreightsListViewModel by viewModel { parametersOf(idHolder) }

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
        initStateManager()
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes freightData for bind the recyclerView
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            initRecyclerView(initAdapters(data))
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {
                    binding.apply {
                        freightFragmentRecycler.visibility = View.GONE
                        fragFreightsBoxError.layout.visibility = View.GONE
                        fragFreightsBoxError.error.visibility = View.GONE
                        fragFreightsBoxError.empty.visibility = View.GONE
                    }
                }
                is State.Loaded -> {
                    binding.apply {
                        freightFragmentRecycler.visibility = View.VISIBLE
                        fragFreightsBoxError.layout.visibility = View.GONE
                        fragFreightsBoxError.error.visibility = View.GONE
                        fragFreightsBoxError.empty.visibility = View.GONE
                    }
                }
                is State.Empty -> {
                    binding.apply {
                        freightFragmentRecycler.visibility = View.GONE
                        fragFreightsBoxError.layout.visibility = View.VISIBLE
                        fragFreightsBoxError.error.visibility = View.GONE
                        fragFreightsBoxError.empty.visibility = View.VISIBLE
                    }
                }
                is State.Error -> {
                    binding.apply {
                        freightFragmentRecycler.visibility = View.GONE
                        fragFreightsBoxError.layout.visibility = View.VISIBLE
                        fragFreightsBoxError.error.visibility = View.VISIBLE
                        fragFreightsBoxError.empty.visibility = View.GONE
                    }
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                    Log.e(TAG_DEBUG, state.error.message.toString())
                }
            }
        }
    }

    /**
     * Initializes the RecyclerView.
     *
     *   Options:
     *   - Navigation: Send the ID and page reference as a result to the parent fragment manager,
     *   which will be in charge of navigation.
     */
    private fun initRecyclerView(adapters: List<RecyclerView.Adapter<out RecyclerView.ViewHolder>>) {
        val concatAdapter = ConcatAdapter(adapters)
        val recyclerView = binding.freightFragmentRecycler
        recyclerView.adapter = concatAdapter
    }

    private fun initAdapters(dataSet: List<Freight>): List<RecyclerView.Adapter<out RecyclerView.ViewHolder>> {
        return dataSet
            .sortedBy { it.loadingDate }
            .reversed()
            .groupBy {
                it.loadingDate?.getMonthAndYearInPtBr() ?: throw InvalidParameterException(NULL_DATE)
            }
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

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}