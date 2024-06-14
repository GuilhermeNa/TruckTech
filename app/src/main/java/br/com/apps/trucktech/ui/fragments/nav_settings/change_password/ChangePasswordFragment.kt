package br.com.apps.trucktech.ui.fragments.nav_settings.change_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.trucktech.databinding.FragmentChangePasswordBinding
import br.com.apps.trucktech.expressions.hideKeyboard
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TOOLBAR_TITLE = "Mudar senha"

private const val INVALID_PASSWORD = "Senha atual inválida"

private const val PASSWORD_CHANGED = "Senha alterada"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePasswordFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChangePasswordFragmentViewModel by viewModel()


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
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChangePasswordButton()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentChangePasswordToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentChangePasswordToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    /**
     * Init change button
     */
    private fun initChangePasswordButton() {
        binding.apply {
            fragChangePassButton.setOnClickListener {
                setClickRangeTimer(it, 1000)

                cleanEditTextError(
                    fragChangePassOldPass,
                    fragChangePassNewPass,
                    fragChangePassRepeatNewPass
                )

                val oldPass = fragChangePassOldPass.text?.toString() ?: ""
                val newPass = fragChangePassNewPass.text?.toString() ?: ""
                val repeatNewPass = fragChangePassRepeatNewPass.text?.toString() ?: ""

                var fieldsAreValid = true
                if (oldPass.isBlank()) {
                    fragChangePassOldPass.error = "Preencha senha atual"
                    fieldsAreValid = false
                }
                if (newPass.isBlank()) {
                    fragChangePassNewPass.error = "Preencha nova senha"
                    fieldsAreValid = false
                }
                if (repeatNewPass.isBlank()) {
                    fragChangePassRepeatNewPass.error = "Confirme nova senha"
                    fieldsAreValid = false
                }
                if (newPass != repeatNewPass) {
                    fragChangePassNewPass.error = "Senha e confirmação diferentes"
                    fragChangePassRepeatNewPass.error = "Senha e confirmação diferentes"
                    fieldsAreValid = false
                }

                if (fieldsAreValid) {
                    updatePassword(oldPass, newPass)
                }
            }
        }
    }

    private fun updatePassword(oldPass: String, newPass: String) {
        lifecycleScope.launch {
            viewModel.updatePassword(oldPass, newPass).observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Success -> {
                        requireContext().hideKeyboard(requireView())
                        requireView().snackBarGreen(PASSWORD_CHANGED)
                        requireView().popBackStack()
                    }

                    is Response.Error -> {
                        val message =
                            when (response.exception) {
                                is FirebaseAuthInvalidCredentialsException -> INVALID_PASSWORD
                                else -> UNKNOWN_EXCEPTION
                            }
                        requireView().snackBarRed(message)
                    }
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