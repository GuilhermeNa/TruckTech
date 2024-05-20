package br.com.apps.trucktech.ui.fragments.nav_settings.settings

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentSettingsBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.toast
import br.com.apps.trucktech.ui.activities.login.LoginActivity
import br.com.apps.trucktech.ui.activities.main.VisualComponents
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsViewModel.Companion.BANK
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsViewModel.Companion.LOGOUT
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsViewModel.Companion.PASSWORD
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsViewModel.Companion.THEME
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.private_adapters.SettingsRecyclerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val userName by lazy { mainActVM.loggedUser.name }
    private val viewModel: SettingsViewModel by viewModel { parametersOf(userName) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initRecyclerAdapter()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(hasToolbar = false)
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes darkLayer to manage the interaction.
     *   - Observes bottomNav to manage the interaction.
     */
    private fun initStateManager() {
        viewModel.darkLayer.observe(viewLifecycleOwner) { isRequested ->
            when (isRequested) {
                true -> binding.fragSettingsDarkLayer.visibility = VISIBLE
                false -> binding.fragSettingsDarkLayer.visibility = GONE
            }
        }

        viewModel.bottomNav.observe(viewLifecycleOwner) { isRequested ->
            when (isRequested) {
                true -> mainActVM.setComponents(VisualComponents(hasBottomNavigation = true))
                false -> mainActVM.setComponents(VisualComponents(hasBottomNavigation = false))
            }
        }
    }

    /**
     * Initializes the RecyclerView.
     *
     * Options:
     *  - clickListener: Performs navigation based on the clicked item.
     */
    private fun initRecyclerAdapter() {
        val recyclerView = binding.fragmentSettingsRecycler
        val adapter = SettingsRecyclerAdapter(
            requireContext(),
            viewModel.settingsItems,
            itemCLickListener = {
                when (it) {
                    PASSWORD -> requireView().navigateTo(SettingsFragmentDirections.actionSettingsFragmentToChangePasswordFragment())
                    THEME -> requireView().navigateTo(SettingsFragmentDirections.actionSettingsFragmentToThemeFragment())
                    LOGOUT -> showAlertDialogForLogout()
                    BANK -> requireView().navigateTo(SettingsFragmentDirections.actionSettingsFragmentToBankFragment())
                }
            }
        )
        recyclerView.adapter = adapter
    }

    private fun showAlertDialogForLogout() {
        viewModel.requestDarkLayer()
        viewModel.dismissBottomNav()

        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_logout)
            .setTitle("Saindo do App")
            .setMessage("Você realmente deseja sair do aplicativo e esquecer a senha?")
            .setPositiveButton("Ok") { dialog, _ ->
                authViewModel.signOut()
                requireContext().navigateTo(LoginActivity::class.java)
                requireActivity().finish()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                requireContext().toast("Negative click")
            }
            .setOnDismissListener {
                viewModel.dismissDarkLayer()
                viewModel.requestBottomNav()
            }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
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