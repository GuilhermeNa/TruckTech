package br.com.apps.trucktech.ui.fragments.nav_home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.databinding.FragmentHomeBinding
import br.com.apps.trucktech.ui.activities.main.VisualComponents
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_home.home.box_view_managers.BoxFinesViewManager
import br.com.apps.trucktech.ui.fragments.nav_home.home.box_view_managers.BoxHeaderViewManager
import br.com.apps.trucktech.ui.fragments.nav_home.home.box_view_managers.BoxTimeLineViewManager
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var stateHandler: HomeState? = null
    private val viewModel: HomeViewModel by viewModel()

    private var boxHeader: BoxHeaderViewManager? = null
    private var boxFines: BoxFinesViewManager? = null
    private var boxTimeLine: BoxTimeLineViewManager? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLoggedUser()
    }

    /**
     * When the app starts and the user logs in, it will load the driver data and save it in the
     * shared activity ViewModel.
     */
    private fun loadLoggedUser() {
        authViewModel.userId.let {
            lifecycleScope.launch {
                mainActVM.initUserData(it).asFlow().first { response ->
                    when (response) {
                        is Response.Error -> {
                            response.exception.printStackTrace()
                            viewModel.setState(State.Error(response.exception))
                        }
                        is Response.Success -> response.data?.let { viewModel.loadData(it) }
                            ?: viewModel.setState(State.Error(NullPointerException()))
                    }
                    true
                }
            }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateHandler = HomeState(binding)
        boxHeader = BoxHeaderViewManager(binding.boxHeader)
        boxFines = BoxFinesViewManager(binding.boxFines, requireContext())
        boxTimeLine = BoxTimeLineViewManager(binding.boxTimeline, requireContext())

        initSwipeRefresh()
        initStateManager()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(hasToolbar = false)
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadData(mainActVM.loggedUser)
        }
    }

    /**
     * State manager for home fragment
     */
    private fun initStateManager() {
        viewModel.fragmentState.observe(viewLifecycleOwner) { fragmentState ->
            when (fragmentState) {
                is State.Loading -> {
                    stateHandler?.showLoading()
                    mainActVM.setComponents(VisualComponents(hasBottomNavigation = false))
                }
                is State.Loaded -> {
                    stateHandler?.showLoaded()
                    mainActVM.setComponents(VisualComponents(hasBottomNavigation = true))
                }
                is State.Empty -> {}
                is State.Error -> stateHandler?.showError()
                else -> {}
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { data ->
            binding.swipeRefresh.isRefreshing = false
            data?.let {
                boxHeader?.bind(mainActVM.loggedUser)
                data.fines?.let { boxFines?.initialize(it) }
                boxTimeLine?.initialize()
            }
        }

    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        binding.swipeRefresh.isRefreshing = false
        boxHeader = null
        boxFines = null
        boxTimeLine = null
        stateHandler = null
        _binding = null
    }

}