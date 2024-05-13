package br.com.apps.trucktech.ui.activities.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.apps.trucktech.R

class LoginActivity : AppCompatActivity() {

 /*   private var pixelsHeight: Int = 0
    private lateinit var logo: ImageView
    private lateinit var loadingText: TextView*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
     /*   getViewFields()
        pixelsHeight = getHeightInPixels()

        iniStateManager()*/
    }
/*

    private fun getViewFields() {
        logo = binding.activityLoginLogoImage
        loadingText = binding.activityLoginLoading
    }

    private fun getHeightInPixels(): Int {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                windowManager
                    .currentWindowMetrics
                    .bounds
                    .height()
            }

            else -> {
                val metrics = DisplayMetrics()
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay.getMetrics(metrics)
                metrics.heightPixels
            }
        }
    }

    */
/**
     * State manager
     *//*

    private fun iniStateManager() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                viewModel.INITIAL_STATE -> setUiInLoadingState()
                viewModel.GET_CREDENTIAL_STATE -> setUiInRequestLoginState()
                viewModel.LOGIN_STATE -> setUiInClosingState()
            }
        }
    }

    */
/**
     * Ui State 1
     *//*

    private fun setUiInLoadingState() {
        defineLogo()
        defineLoadingText()
        runInitialAnim()
    }

    private fun defineLogo() {
        logo.apply {
            y = pixelsHeight * 0.29.toFloat()
            scaleY = pixelsHeight * 0.00055.toFloat()
            scaleX = pixelsHeight * 0.00055.toFloat()
        }
    }

    private fun defineLoadingText() {
        loadingText.y = pixelsHeight * 0.12.toFloat()
    }

    private fun runInitialAnim() {
        lifecycleScope.launch {
            delay(500)
            val slideInLeftLoading =
                AnimationUtils.loadAnimation(this@LoginActivity, R.anim.slide_in_from_left)
            loadingText.apply {
                visibility = View.VISIBLE
                animation = slideInLeftLoading
            }
        }
    }

    */
/**
     * Ui State 2
     *//*

    private fun setUiInRequestLoginState() {
        TODO("Not yet implemented")
    }

    */
/**
     * Ui State 3
    *//*

    private fun setUiInClosingState() {
        TODO("Not yet implemented")
    }
*/


}