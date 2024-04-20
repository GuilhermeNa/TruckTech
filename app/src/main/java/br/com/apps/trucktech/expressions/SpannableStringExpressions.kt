package br.com.apps.trucktech.expressions

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan

fun SpannableString.toBold(): SpannableString {
    this.setSpan(StyleSpan(Typeface.BOLD), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun SpannableString.toItalic(): SpannableString {
    this.setSpan(StyleSpan(Typeface.ITALIC), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun SpannableString.toUnderline(): SpannableString {
    this.setSpan(StyleSpan(Typeface.BOLD_ITALIC), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.setSpan(UnderlineSpan(), 0, this.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this

}