package br.com.apps.trucktech.expressions

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar

fun View.navigateTo(direction: NavDirections) {
    val navController = Navigation.findNavController(this)
    navController.navigate(direction)
}

fun View.navigateWithSafeArgs(direction: NavDirections) {
    val navController = Navigation.findNavController(this)
    navController.navigate(direction)
}

fun View.popBackStack() {
    val navController = Navigation.findNavController(this)
    navController.popBackStack()
}

fun View.snackBarGreen(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(Color.parseColor("#3ED745"))
        .setTextColor(Color.parseColor("#FFFFFF"))
        .show()
}

fun View.snackBarRed(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(Color.parseColor("#FF0000"))
        .setTextColor(Color.parseColor("#FFFFFF"))
        .show()
}

fun View.snackBarOrange(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(Color.parseColor("#FF9800"))
        .setTextColor(Color.parseColor("#FFFFFF"))
        .show()
}

fun View.snackBarTop(text: String) {
    val snack = Snackbar.make(this, text, Snackbar.LENGTH_INDEFINITE)
    snack.run {
        setBackgroundTint(Color.parseColor("#016794"))
        setAction("CANCEL") { snack.dismiss() }
    }

    val params = this.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
    snack.view.layoutParams = params;
    snack.show();
}


fun View.snackBarRedTop(text: String) {
    val snackBar = Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
    snackBar.setBackgroundTint(Color.parseColor("#FF0000"))
    snackBar.setTextColor(Color.parseColor("#FFFFFF"))

    // Get the Snackbar's view
    val snackBarView = snackBar.view

    // Get the Snackbar's LayoutParams and set its gravity to TOP
    val params = snackBarView.layoutParams as ViewGroup.MarginLayoutParams
    params.topMargin = 0
    params.bottomMargin = 0

    // Use the parent view to set the layout parameters
    val parentView = this as ViewGroup
    parentView.addView(snackBarView, params)

    // Show the Snackbar
    snackBar.addCallback(object : Snackbar.Callback() {
        override fun onShown(sb: Snackbar?) {
            // Move the Snackbar to the top
            snackBarView.translationY = -parentView.height.toFloat() + snackBarView.height
        }

        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            // Remove the Snackbar view when it's dismissed
            parentView.removeView(snackBarView)
        }
    })

    snackBar.show()
}