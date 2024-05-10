package br.com.apps.trucktech.expressions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream



fun Bitmap.encodeBitmap(): String {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 70, stream)
    val imageBytes = stream.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}

fun String.decodeBitMap(): Bitmap {
    val decode = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decode, 0, decode.size)
}