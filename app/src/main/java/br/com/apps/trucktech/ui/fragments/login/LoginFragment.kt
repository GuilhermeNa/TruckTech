package br.com.apps.trucktech.ui.fragments.login

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentLoginBinding
import br.com.apps.trucktech.expressions.getColorStateListById
import br.com.apps.trucktech.expressions.hideKeyboard
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.activities.main.MainActivity
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragment
import com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var stateHandler: LoginState? = null
    private val viewModel: LoginViewModel by viewModel()

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchForLoggedUser()
    }

    private fun searchForLoggedUser() {
        lifecycleScope.launch {
            authViewModel.getCurrentUser().let { user ->
                if (user != null) viewModel.setState(LfState.CurrentUserFound, 1000)
                else viewModel.setState(LfState.CurrentUserNotFound, 1000)
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
        stateHandler = LoginState(getHeightInPixels(), binding)
        return binding.root
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
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setState(LfState.Opening)
        initTouchListeners()
        initLoginListeners()
        initStateManager()
        initEditTextSettings()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initTouchListeners() {
        binding.fragLoginBg.setOnTouchListener { _, _ ->
            requireActivity().hideKeyboard()
            true
        }
        binding.fragLoginLogoImage.setOnTouchListener { _, _ ->
            requireActivity().hideKeyboard()
            true
        }
        binding.fragLoginTitle.setOnTouchListener { _, _ ->
            requireActivity().hideKeyboard()
            true
        }
    }

    private fun initEditTextSettings() {
        binding.apply {
            fragFieldPassword.addTextChangedListener { text ->
                cleanEditTextError(fragFieldPassword)
                text?.let { t ->
                    binding.fragLayoutPassword.endIconMode =
                        if (t.isBlank()) END_ICON_NONE
                        else END_ICON_PASSWORD_TOGGLE

                    binding.fragLayoutPassword.counterTextColor =
                        if (t.length > 5) requireContext().getColorStateListById(R.color.dark_green)
                        else requireContext().getColorStateListById(R.color.dark_red)

                }
            }

            fragFieldUser.addTextChangedListener { text ->
                cleanEditTextError(fragFieldUser)
                text?.let { t ->
                    if (t.isBlank()) {
                        binding.fragLayoutUser.isEndIconVisible = false
                        binding.fragLayoutUser.endIconMode = END_ICON_NONE
                    } else {
                        binding.fragLayoutUser.endIconMode = END_ICON_CLEAR_TEXT
                    }
                }
            }

        }
    }

    /**
     * Init login button
     */
    private fun initLoginListeners() {
        binding.apply {
            fragFieldPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    initLoginFlow()
                    return@setOnEditorActionListener true
                }
                false
            }

            fragButtonLogin.setOnClickListener { initLoginFlow() }
        }
    }

    private fun initLoginFlow() {
        binding.apply {
            setClickRangeTimer(requireView(), 1000)

            cleanEditTextError(fragFieldUser, fragFieldPassword)

            val login = fragFieldUser.text.toString()
            val email = fragFieldPassword.text.toString()

            var fieldsAreValid = true
            if (login.isBlank()) {
                fragFieldUser.error = "Preencha o login"
                fieldsAreValid = false
            }
            if (email.isBlank()) {
                fragFieldPassword.error = "Preencha a senha"
                fieldsAreValid = false
            }

            if (fieldsAreValid) {
                tryToLogin(Pair(login, email))
            }
        }
    }

    private fun tryToLogin(credential: Pair<String, String>) {
        requireActivity().hideKeyboard()
        viewModel.setState(LfState.TryingToLogin)

        authViewModel.signIn(credential).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> viewModel.setState(LfState.LoginFailed(response.exception))

                is Response.Success -> {
                    authViewModel.getCurrentUser()
                    navigateToMainAct()
                }
            }
        }

    }

    private fun navigateToMainAct() {
        if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
            requireActivity().navigateTo(MainActivity::class.java)
            requireActivity().finish()
        }
    }

    /**
     * State manager
     */
    private fun initStateManager() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            lifecycleScope.launch {
                when (state) {
                    is LfState.Opening -> stateHandler?.showOpening()
                    is LfState.CurrentUserNotFound -> stateHandler?.showEditTextFieldsForCredentials()
                    is LfState.CurrentUserFound -> stateHandler?.showOpeningToMainActivity { navigateToMainAct() }
                    is LfState.TryingToLogin -> stateHandler?.showTryingToLogin()
                    is LfState.LoginFailed -> {
                        stateHandler?.showLoginFailed()
                        val message = viewModel.getErrorMessage(state.error)
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
        super.onDestroyView()
        stateHandler = null
        _binding = null
    }

}