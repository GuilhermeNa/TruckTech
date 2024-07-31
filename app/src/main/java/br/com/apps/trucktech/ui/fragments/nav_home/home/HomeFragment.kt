package br.com.apps.trucktech.ui.fragments.nav_home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import br.com.apps.model.model.FleetFine
import br.com.apps.trucktech.databinding.FragmentHomeBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.ui.activities.main.LoggedUser
import br.com.apps.trucktech.ui.activities.main.VisualComponents
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.util.state.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

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
        initStateManager()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(hasToolbar = false)
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * State manager for home fragment
     */
    private fun initStateManager() {
        mainActVM.state.observe(viewLifecycleOwner) { globalState ->
            when(globalState) {
                is State.Loading -> {
                    binding.apply {
                        shimmer.visibility = VISIBLE
                        boxHeader.layout.visibility = GONE
                        fragReceivable.visibility = GONE
                        fragPerformance.visibility = GONE
                        boxFines.layout.visibility = GONE
                        boxTimeline.layout.visibility = GONE
                    }
                }
                is State.Error -> {
                    globalState.error.printStackTrace()
                    requireActivity().finish()
                }
                is State.Loaded -> {
                    binding.apply {
                        shimmer.visibility = GONE
                        boxHeader.layout.visibility = VISIBLE
                        fragReceivable.visibility = VISIBLE
                        fragPerformance.visibility = VISIBLE
                        boxFines.layout.visibility = VISIBLE
                        boxTimeline.layout.visibility = VISIBLE
                    }
                    mainActVM.setComponents(VisualComponents(hasBottomNavigation = true))
                    bindBoxHeader(mainActVM.loggedUser)
                    bindBoxTimeLine()
                }
                else -> {}
            }
        }

        mainActVM.cachedFines.observe(viewLifecycleOwner) { fines ->
            bindBoxFines(fines)
        }

    }

    private fun bindBoxHeader(user: LoggedUser) {
        binding.boxHeader.apply {
            name.text = user.name
        }
    }

    private fun bindBoxFines(fines: List<FleetFine>?) {
        binding.boxFines.apply {
            panelFinesImage.loadImageThroughUrl(
                "https://gringo.com.vc/wp-content/uploads/2022/06/Multa_18032016_1738_1280_960-1024x768.jpg"
            )

            panelFinesNewText.text = viewModel.getNewFinesText()
            panelFinesNew.text = fines?.let { viewModel.getThisYearFines(it).toString() }
            panelFinesAccumulated.text = fines?.size.toString()

            panelFinesCard.setOnClickListener {
                it.navigateTo(HomeFragmentDirections.actionHomeFragmentToFinesFragment())
            }
        }
    }

    private fun bindBoxTimeLine() {
        binding.boxTimeline.apply {
            panelTimeLineImage.loadImageThroughUrl(
                "https://cdn.sanity.io/images/599r6htc/localized/e09081e08bcc400a488dd7c1fa88a4d1493b52aa-1108x1108.png?w=514&q=75&fit=max&auto=format"
            )
            panelTimeLineCard.setOnClickListener {
                it.navigateTo(HomeFragmentDirections.actionHomeFragmentToTimelineFragment())
            }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}