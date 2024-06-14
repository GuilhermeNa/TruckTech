package br.com.apps.trucktech.ui.fragments.nav_home.fines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import br.com.apps.model.IdHolder
import br.com.apps.trucktech.databinding.FragmentFinesListBinding
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_home.fines.private_adapters.FineRecyclerAdapter
import br.com.apps.trucktech.util.state.State
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Multas"

class FinesListFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentFinesListBinding? = null
    private val binding get() = _binding!!

    private val idHolder by lazy {
        IdHolder(
            truckId = mainActVM.loggedUser.truckId,
            driverId = mainActVM.loggedUser.driverId
        )
    }
    private val viewModel: FinesListViewModel by viewModel { parametersOf(idHolder) }

    private var adapter: FineRecyclerAdapter? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initStateManager()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentFinesToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentFinesToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    /**
     * Initialize Recycler View
     */
    private fun initRecyclerView() {
        val recyclerView = binding.fragmentFinesRecycler
        adapter = FineRecyclerAdapter(requireContext(), emptyList())
        recyclerView.adapter = adapter

        val divider = DividerItemDecoration(recyclerView.context, VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes fineData for update the recyclerView.
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            data?.let {
                adapter?.update(it)
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is State.Loading -> {
                    binding.fragmentFinesRecycler.visibility = GONE
                    binding.layoutError.layout.visibility = GONE
                    binding.layoutError.error.visibility = GONE
                    binding.layoutError.empty.visibility = GONE
                }
                is State.Loaded -> {
                    binding.fragmentFinesRecycler.visibility = VISIBLE
                    binding.layoutError.layout.visibility = GONE
                    binding.layoutError.error.visibility = GONE
                    binding.layoutError.empty.visibility = GONE
                }
                is State.Empty -> {
                    binding.fragmentFinesRecycler.visibility = GONE
                    binding.layoutError.layout.visibility = VISIBLE
                    binding.layoutError.error.visibility = GONE
                    binding.layoutError.empty.visibility = VISIBLE
                }
                is State.Error -> {
                    state.error.printStackTrace()
                    binding.fragmentFinesRecycler.visibility = GONE
                    binding.layoutError.layout.visibility = VISIBLE
                    binding.layoutError.error.visibility = VISIBLE
                    binding.layoutError.empty.visibility = GONE
                }
                else -> {}
            }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

}