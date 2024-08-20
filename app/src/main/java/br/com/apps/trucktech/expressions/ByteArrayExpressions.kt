package br.com.apps.trucktech.expressions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.widget.ImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

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

fun ByteArray.compressByteArray(): ByteArray {
    ByteArrayOutputStream().use { byteStream ->
        GZIPOutputStream(byteStream).use { gzipStream ->
            gzipStream.write(this)
        }
        return byteStream.toByteArray()
    }
}

fun ByteArray.decompressByteArray(): ByteArray {
    ByteArrayInputStream(this).use { byteStream ->
        GZIPInputStream(byteStream).use { gzipStream ->
            return gzipStream.readBytes()
        }
    }
}