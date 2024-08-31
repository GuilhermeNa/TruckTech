package br.com.apps.trucktech.util.image

class Image(
     val byteArray: ByteArray? = null,
     val url: String? = null
) {

    fun getData(): Any? = when {
        byteArray != null -> byteArray
        url != null -> url
        else -> null
    }

}





