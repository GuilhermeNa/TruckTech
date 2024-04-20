package br.com.apps.trucktech.expressions

import android.graphics.Color
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar

fun View.navigateTo(direction: NavDirections){
    val navController = Navigation.findNavController(this)
    navController.navigate(direction)
}

fun View.navigateWithSafeArgs(direction: NavDirections){
    val navController = Navigation.findNavController(this)
    navController.navigate(direction)
}

fun View.popBackStack(){
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