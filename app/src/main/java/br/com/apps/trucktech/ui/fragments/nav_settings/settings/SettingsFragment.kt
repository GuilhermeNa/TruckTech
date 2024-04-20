package br.com.apps.trucktech.ui.fragments.nav_settings.settings

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentSettingsBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.toast
import br.com.apps.trucktech.ui.activities.LoginActivity
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsFragmentViewModel.Companion.BANK
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsFragmentViewModel.Companion.LOGOUT
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsFragmentViewModel.Companion.PASSWORD
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsFragmentViewModel.Companion.THEME
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
    private val viewModel: SettingsFragmentViewModel by viewModel { parametersOf(sharedViewModel.userData.value?.user) }

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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerAdapter()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(hasToolbar = false)
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    private fun initRecyclerAdapter() {
        val recyclerView = binding.fragmentSettingsRecycler
        val adapter = SettingsRecyclerAdapter(
            requireContext(),
            viewModel.settingsItems,
            itemCLickListener = {
                when (it) {
                    PASSWORD -> requireView().navigateTo(SettingsFragmentDirections.actionSettingsFragmentToChangePasswordFragment())
                    THEME -> requireView().navigateTo(SettingsFragmentDirections.actionSettingsFragmentToThemeFragment())
                    LOGOUT -> showAlertDialog()
                    BANK -> requireView().navigateTo(SettingsFragmentDirections.actionSettingsFragmentToBankFragment())
                }
            }
        )
        recyclerView.adapter = adapter
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_logout_black)
            .setTitle("Saindo do App")
            .setMessage("Você realmente deseja sair do aplicativo e esquecer a senha?")
            .setPositiveButton("Ok") { dialog, _ ->
                loginViewModel.signOut()
                requireContext().navigateTo(LoginActivity::class.java)
                requireActivity().finish()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                requireContext().toast("Negative click")
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