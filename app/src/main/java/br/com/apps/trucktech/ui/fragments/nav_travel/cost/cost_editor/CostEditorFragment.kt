package br.com.apps.trucktech.ui.fragments.nav_travel.cost.cost_editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentCostEditorBinding
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar

private const val TOOLBAR_TITLE = "Custo"

/**
 * A simple [Fragment] subclass.
 * Use the [CostEditorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CostEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentCostEditorBinding? = null
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
        _binding = FragmentCostEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentCostEditorToolbar.toolbar,
            menuId = R.menu.menu_editor,
            toolbarTextView = binding.fragmentCostEditorToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    override fun initMenuClickListeners(toolbar: Toolbar) {
        super.initMenuClickListeners(toolbar)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_editor_save -> {
                    clearMenu()
                    Toast.makeText(requireContext(), "Click save", LENGTH_SHORT)
                    true
                }
                else -> false
            }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}