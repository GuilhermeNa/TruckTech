package br.com.apps.trucktech.ui.fragments.nav_home.time_line

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.apps.trucktech.databinding.FragmentTimelineBinding
import br.com.apps.trucktech.sampleEventsList
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_home.time_line.private_adapters.TimelineRecyclerAdapter

private const val TOOLBAR_TITLE = "Linha do tempo"

/**
 * A simple [Fragment] subclass.
 * Use the [TimelineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimelineFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        binding.layoutError.apply {
            empty.visibility = VISIBLE
            error.visibility = VISIBLE
        }

    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentTimelineToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentTimelineToolbar.toolbarText,
            title = TOOLBAR_TITLE

        )
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    private fun initRecyclerView() {
        val recyclerView = binding.fragmentTimeLineRecycler
        val adapter = TimelineRecyclerAdapter(requireContext(), sampleEventsList)
        recyclerView.adapter = adapter
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}