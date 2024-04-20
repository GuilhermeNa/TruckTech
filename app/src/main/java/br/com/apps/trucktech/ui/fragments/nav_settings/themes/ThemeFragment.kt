package br.com.apps.trucktech.ui.fragments.nav_settings.themes

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import br.com.apps.trucktech.databinding.FragmentThemeBinding
import br.com.apps.trucktech.ui.DARK_SWITCH
import br.com.apps.trucktech.ui.LIGHT_SWITCH
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TOOLBAR_TEXT = "Tema"

/**
 * A simple [Fragment] subclass.
 * Use the [ThemeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ThemeFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentThemeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ThemeFragmentViewModel by viewModel()

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
        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        initCheckBox()
        initStateManager()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentThemeToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentThemeToolbar.toolbarText,
            title = TOOLBAR_TEXT
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Check the correct box by the app theme
     */
    private fun bind() {
        val currentNightMode =
            this.context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            binding.fragThemeLightCheck.isChecked = true
        } else {
            binding.fragThemeDarkCheck.isChecked = true
        }
    }

    private fun initCheckBox() {
        binding.apply {
            fragThemeLightCheck.addOnCheckedStateChangedListener { checkBox, state ->
                if (checkBox.isChecked) {
                    viewModel.newSwitchHaveBeenSelected(LIGHT_SWITCH)
                }
            }
            fragThemeDarkCheck.addOnCheckedStateChangedListener { checkBox, state ->
                if (checkBox.isChecked) {
                    viewModel.newSwitchHaveBeenSelected(DARK_SWITCH)
                }
            }
        }
    }

    /**
     * State manager is set here.
     */
    private fun initStateManager() {
        viewModel.selectedSwitch.observe(viewLifecycleOwner) { switchMap ->
            val key = switchMap.entries.find { it.value }?.key
            when (key) {
                LIGHT_SWITCH -> {
                    binding.fragThemeDarkCheck.isChecked = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                DARK_SWITCH -> {
                    binding.fragThemeLightCheck.isChecked = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
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