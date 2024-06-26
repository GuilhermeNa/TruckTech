package br.com.apps.trucktech.ui.fragments.base_fragments

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseFragment : Fragment() {

    val authViewModel: AuthViewModel by viewModel()

    /**
     * Defines a timer range for click interactions with the view.
     *
     * @param view that receives the timer.
     * @param rangeTimer range time in milliseconds.
     */
    fun setClickRangeTimer(view: View?, rangeTimer: Long) {
        view?.let {
            lifecycleScope.launch {
                it.isEnabled = false
                delay(rangeTimer)
                it.isEnabled = true
            }
        }
    }

    /**
     * The function will clear the error for each EditText received as an argument.
     *
     * @param view represents the editTexts
     */
    fun cleanEditTextError(vararg view: View) {
        view.forEach {
            when (it) {
                is MaterialAutoCompleteTextView -> it.error = null
                is TextInputEditText -> it.error = null
            }
        }
    }


}