package br.com.apps.trucktech.expressions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.widget.ImageView
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

fun Bitmap.getByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}

fun ImageView.getByteArray(): ByteArray {
    val bitmap = (this.drawable as BitmapDrawable).bitmap
    return bitmap.getByteArray()
}