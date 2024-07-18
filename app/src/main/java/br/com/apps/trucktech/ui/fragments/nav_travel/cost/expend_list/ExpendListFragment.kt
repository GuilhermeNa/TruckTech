package br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_list

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
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.util.FAILED_TO_LOAD_DATA
import br.com.apps.repository.util.NULL_DATE
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.databinding.FragmentExpendListBinding
import br.com.apps.model.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.KEY_ID
import br.com.apps.trucktech.ui.PAGE_COST
import br.com.apps.trucktech.ui.fragments.nav_travel.records.RecordsViewModel
import br.com.apps.trucktech.ui.public_adapters.DateRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.RecordsItemRecyclerAdapter
import br.com.apps.trucktech.util.state.State
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.security.InvalidParameterException

class ExpendListFragment : Fragment() {

    private var _binding: FragmentExpendListBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: RecordsViewModel by viewModels({ requireParentFragment() })
    private val idHolder by lazy { IdHolder(masterUid = sharedViewModel.masterUid, travelId = sharedViewModel.travelId) }
    private val viewModel: ExpendListViewModel by viewModel { parametersOf(idHolder) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpendListBinding.inflate(inflater, container, false)
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
     *   - Observes expendData for bind the recyclerView
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            initRecyclerView(initAdapters(data))
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {
                    binding.apply {
                        costFragmentRecycler.visibility = View.GONE
                        fragExpendBoxError.layout.visibility = View.GONE
                        fragExpendBoxError.error.visibility = View.GONE
                        fragExpendBoxError.empty.visibility = View.GONE
                    }
                }
                is State.Loaded -> {
                    binding.apply {
                        costFragmentRecycler.visibility = View.VISIBLE
                        fragExpendBoxError.layout.visibility = View.GONE
                        fragExpendBoxError.error.visibility = View.GONE
                        fragExpendBoxError.empty.visibility = View.GONE
                    }
                }
                is State.Empty -> {
                    binding.apply {
                        costFragmentRecycler.visibility = View.GONE
                        fragExpendBoxError.layout.visibility = View.VISIBLE
                        fragExpendBoxError.error.visibility = View.GONE
                        fragExpendBoxError.empty.visibility = View.VISIBLE
                    }
                }
                is State.Error -> {
                    binding.apply {
                        costFragmentRecycler.visibility = View.GONE
                        fragExpendBoxError.layout.visibility = View.VISIBLE
                        fragExpendBoxError.error.visibility = View.VISIBLE
                        fragExpendBoxError.empty.visibility = View.GONE
                    }
                    requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                    Log.e(TAG_DEBUG, state.error.message.toString())
                }

                else -> {}
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
        val recyclerView = binding.costFragmentRecycler
        recyclerView.adapter = concatAdapter
    }

    private fun initAdapters(dataSet: List<Expend>): List<RecyclerView.Adapter<out RecyclerView.ViewHolder>> {
        return dataSet
            .sortedBy { it.date }
            .reversed()
            .groupBy {
                it.date?.getMonthAndYearInPtBr() ?: throw InvalidParameterException(NULL_DATE)
            }
            .map { createAdapters(it) }
            .flatten()
    }

    private fun createAdapters(itemsMap: Map.Entry<String, List<Expend>>) =
        listOf(
            DateRecyclerAdapter(requireContext(), listOf(itemsMap.key)),
            RecordsItemRecyclerAdapter(
                requireContext(),
                itemsMap.value,
                itemCLickListener = { id ->
                    val bundle = Bundle()
                    bundle.putString(KEY_ID, id)
                    parentFragmentManager.setFragmentResult(PAGE_COST, bundle)
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