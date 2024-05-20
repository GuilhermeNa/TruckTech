package br.com.apps.trucktech.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.apps.trucktech.databinding.FragmentLoginBinding
import br.com.apps.trucktech.expressions.hideKeyboard
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.activities.main.MainActivity
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragment

private const val SUCCESSFULLY_LOGIN = "Sucesso ao logar"
private const val LOGIN_FAILED = "Falha ao logar"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchForLoggedUser()
    }

    private fun searchForLoggedUser() {
        authViewModel.getCurrentUser().let { user ->
            if (user != null) {
                authViewModel.userId = user.uid
                requireActivity().navigateTo(MainActivity::class.java)
                requireActivity().finish()
            }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLoginButton()
        initStateManager()
    }

    private fun initLoginButton() {
        binding.apply {
            fragmentLoginButton.setOnClickListener {
                setClickRangeTimer(it, 1000)

                cleanEditTextError(
                    fragmentLoginUserEditText,
                    fragmentLoginPasswordEditText
                )

                val login = fragmentLoginUserEditText.text.toString()
                val email = fragmentLoginPasswordEditText.text.toString()

                var fieldsAreValid = true
                if (login.isBlank()) {
                    fragmentLoginUserEditText.error = "Preencha o login"
                    fieldsAreValid = false
                }
                if (email.isBlank()) {
                    fragmentLoginPasswordEditText.error = "Preencha o login"
                    fieldsAreValid = false
                }

                if(fieldsAreValid) {
                    val credential = Pair(login, email)
                    authViewModel.signIn(credential)
                }

            }
        }
    }

    private fun initStateManager() {
        authViewModel.signIn.observe(viewLifecycleOwner) {
            it?.let { resource ->
                if(resource.data) {
                    requireContext().hideKeyboard(requireView())
                    requireView().snackBarGreen(SUCCESSFULLY_LOGIN)
                    searchForLoggedUser()
                } else {
                    val message = resource.error ?: LOGIN_FAILED
                    requireView().snackBarRed(message)
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