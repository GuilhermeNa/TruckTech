package br.com.apps.trucktech.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object TempFile {

    fun create(context: Context, ba: ByteArray, name: String): String {
        val outputDir = context.cacheDir
        val imageFile = File(outputDir, "$name.jpg")

        try {
            FileOutputStream(imageFile).use { os ->
                os.write(ba)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return imageFile.absolutePath
    }

}