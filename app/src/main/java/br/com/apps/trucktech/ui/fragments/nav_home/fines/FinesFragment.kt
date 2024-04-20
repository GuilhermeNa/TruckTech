package br.com.apps.trucktech.ui.fragments.nav_home.fines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import br.com.apps.trucktech.databinding.FragmentFinesBinding
import br.com.apps.trucktech.sampleFinesList
import br.com.apps.trucktech.ui.fragments.nav_home.fines.private_adapters.FineRecyclerAdapter
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar

private const val TOOLBAR_TITLE = "Multas"

/**
 * A simple [Fragment] subclass.
 * Use the [FinesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinesFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentFinesBinding? = null
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
        _binding = FragmentFinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentFinesToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentFinesToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    private fun initRecyclerView() {
        val recyclerView = binding.fragmentFinesRecycler
        val adapter = FineRecyclerAdapter(requireContext(), sampleFinesList)
        recyclerView.adapter = adapter

        val divider = DividerItemDecoration(recyclerView.context, VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}