package br.com.apps.trucktech.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat

object DeviceCapabilities {

    fun hasNetworkConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    fun softKeyboardListener(root: View?, height: Int, complete: (isOpen: Boolean) -> Unit) =
        object : ViewTreeObserver.OnGlobalLayoutListener {
            private var isOpen = false

            override fun onGlobalLayout() {
                root?.let { root ->
                    val newHeight = root.height

                    if (height > newHeight && !isOpen) {
                        complete(true)
                        isOpen = true
                    } else if (height == newHeight && isOpen) {
                        complete(false)
                        isOpen = false
                    }
                }
            }
        }

    fun checkCameraPermission(context: Context, hasPermission: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            hasPermission(false)
        } else {
            hasPermission(true)
        }
    }

}