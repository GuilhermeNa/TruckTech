package br.com.apps.trucktech.ui.fragments.login

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentLoginBinding
import br.com.apps.trucktech.expressions.hideKeyboard
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.activities.main.MainActivity
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragment
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModel()
    private var pixelsHeight: Int = 0

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchForLoggedUser()
        pixelsHeight = getHeightInPixels()
    }

    private fun searchForLoggedUser() {
        authViewModel.getCurrentUser().let { user ->
            if (user != null) viewModel.setStateLogin()
            else viewModel.setStateCredential()
        }
    }

    private fun getHeightInPixels(): Int {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                requireActivity().windowManager
                    .currentWindowMetrics
                    .bounds
                    .height()
            }

            else -> {
                val metrics = DisplayMetrics()
                @Suppress("DEPRECATION")
                requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
                metrics.heightPixels
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

    /**
     * Init login button
     */
    private fun initLoginButton() {
        binding.apply {
            fragButtonLogin.setOnClickListener {
                setClickRangeTimer(it, 1000)

                cleanEditTextError(
                    fragFieldUser,
                    fragFieldPassword
                )

                val login = fragFieldUser.text.toString()
                val email = fragFieldPassword.text.toString()

                var fieldsAreValid = true
                if (login.isBlank()) {
                    fragFieldUser.error = "Preencha o login"
                    fieldsAreValid = false
                }
                if (email.isBlank()) {
                    fragFieldPassword.error = "Preencha o login"
                    fieldsAreValid = false
                }

                if (fieldsAreValid) {
                    tryToLogin(Pair(login, email))
                }

            }
        }
    }

    private fun tryToLogin(credential: Pair<String, String>) {
        authViewModel.signIn(credential).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    val message = when (response.exception) {
                        is FirebaseAuthInvalidUserException,
                        is FirebaseAuthInvalidCredentialsException -> "Credenciais incorretas"
                        is FirebaseTooManyRequestsException -> "Usuário bloqueado temporariamente por muitos erros"
                        else -> "Erro desconhecido"
                    }
                    requireView().snackBarRed(message)
                }
                is Response.Success -> {
                    authViewModel.getCurrentUser()
                    navigateToMainAct()
                }
            }
        }
    }

    private fun navigateToMainAct() {
        requireContext().hideKeyboard(requireView())
        requireActivity().navigateTo(MainActivity::class.java)
        requireActivity().finish()
    }

    /**
     * State manager
     */
    private fun initStateManager() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                STATE_INITIAL -> setUiInLoadingState()
                STATE_CREDENTIAL -> setUiInRequestCredentialState()
                STATE_LOGIN -> setUiInLoginState { navigateToMainAct() }
            }
        }
    }

    /**
     * State Initial
     */
    private fun setUiInLoadingState() {
        binding.fragLoginLogoImage.apply {
            y = pixelsHeight * 0.28.toFloat()
            scaleY = pixelsHeight * 0.00055.toFloat()
            scaleX = pixelsHeight * 0.00055.toFloat()
        }

        binding.fragLoginLoading.y = pixelsHeight * 0.14.toFloat()

        lifecycleScope.launch {
            delay(500)
            binding.fragLoginLoading.apply {
                visibility = View.VISIBLE
                animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left)
            }
        }

    }

    /**
     * State credential
     */
    private fun setUiInRequestCredentialState() {
        lifecycleScope.launch {

            delay(1000)
            binding.fragLoginLoading.apply {
                visibility = View.GONE
                animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_to_right)
            }

            delay(500)
            binding.fragLoginLogoImage.apply {
                animate()
                    .setDuration(250)
                    .scaleY((pixelsHeight * 0.00045).toFloat())
                    .scaleX((pixelsHeight * 0.00045).toFloat())
                    .yBy(-(pixelsHeight * 0.31).toFloat())
                    .start()
            }

            delay(200)
            binding.fragLoginTitle.apply {
                visibility = View.VISIBLE
                animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_bottom)
            }

            delay(250)
            binding.fragLayoutUser.apply {
                visibility = View.VISIBLE
                animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left)
            }

            delay(50)
            binding.fragLayoutPassword.apply {
                visibility = View.VISIBLE
                animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left)
            }

            delay(50)
            binding.fragButtonLogin.apply {
                visibility = View.VISIBLE
                animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left)
            }

            delay(50)
            binding.fragButtonHelp.apply {
                visibility = View.VISIBLE
                animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left)
            }
        }

    }

    /**
     * State login
     */
    private fun setUiInLoginState(animationComplete: () -> Unit) {
        lifecycleScope.launch {

            delay(1000)
            binding.fragLoginLoading.apply {
                visibility = View.GONE
                animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_to_right)
            }

            delay(500)
            binding.fragLoginLogoImage.apply {
                visibility = View.GONE
                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_to_left)
            }

            delay(250)
            animationComplete()
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