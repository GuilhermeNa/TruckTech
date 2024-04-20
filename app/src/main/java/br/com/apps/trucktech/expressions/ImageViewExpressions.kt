package br.com.apps.trucktech.expressions

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.widget.ImageView
import br.com.apps.trucktech.R
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load

fun ImageView.loadImageThroughUrl(url: String? = null, context: Context){

    val imageLoaderGifSupport = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    load(url, imageLoaderGifSupport) {
        fallback(R.drawable.placeholder)
        error(R.drawable.placeholder)
        placeholder(R.drawable.placeholder)
    }

}

fun ImageView.isLightTheme(): Boolean {
    val currentNightMode = this.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_NO
}