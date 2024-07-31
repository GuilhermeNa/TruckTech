package br.com.apps.trucktech.ui.fragments.login

import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentLoginBinding
import kotlinx.coroutines.delay


class LoginState(
    private val pixelsHeight: Int,
    private val binding: FragmentLoginBinding
) {

    suspend fun showOpening() {
        binding.apply {
            fragLoginLogoImage.apply {
                y = pixelsHeight * 0.28.toFloat()
                scaleY = pixelsHeight * 0.00055.toFloat()
                scaleX = pixelsHeight * 0.00055.toFloat()
            }

            fragLoginLoading.y = pixelsHeight * 0.14.toFloat()

            delay(500)
            fragLoginLoading.apply {
                visibility = VISIBLE
                animation =
                    AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)
            }
        }

    }

    suspend fun showEditTextFieldsForCredentials() {
        delay(1000)
        binding.fragLoginLoading.apply {
            visibility = GONE
            animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_out_to_right)
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
            visibility = VISIBLE
            animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_in_from_bottom)
        }

        delay(250)
        binding.fragLayoutUser.apply {
            visibility = VISIBLE
            animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)
        }

        delay(50)
        binding.fragLayoutPassword.apply {
            visibility = VISIBLE
            animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)
        }

        delay(50)
        binding.fragButtonLogin.apply {
            visibility = VISIBLE
            animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)
        }

        delay(50)
        binding.fragButtonHelp.apply {
            visibility = VISIBLE
            animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)
        }
    }

    suspend fun showOpeningToMainActivity(animationComplete: () -> Unit) {
        delay(1000)
        binding.fragLoginLoading.apply {
            visibility = GONE
            animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_out_to_right)
        }

        delay(500)
        binding.fragLoginLogoImage.apply {
            visibility = GONE
            animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_to_left)
        }

        delay(250)
        animationComplete()
    }

    fun showTryingToLogin() {
        binding.apply {
            boxLoading.layout.visibility = VISIBLE
            scroll.apply {
                isEnabled = false
                isClickable = false
            }
            fragLayoutUser.apply {
                isEnabled = false
                isClickable = false
            }
            fragLayoutPassword.apply {
                isEnabled = false
                isClickable = false
            }
        }
    }

    fun showLoginFailed() {
        binding.apply {
            boxLoading.layout.visibility = GONE
            scroll.apply {
                isEnabled = true
                isClickable = true
            }
            fragLayoutUser.apply {
                isEnabled = true
                isClickable = true
            }
            fragLayoutPassword.apply {
                isEnabled = true
                isClickable = true
            }
        }
    }

}


