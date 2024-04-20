package br.com.apps.trucktech.expressions

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat

fun Context.getColorById(colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

fun Context.getColorStateListById(colorId: Int): ColorStateList {
    return ColorStateList.valueOf(ContextCompat.getColor(this, colorId))
}