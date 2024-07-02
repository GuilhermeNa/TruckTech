package br.com.apps.trucktech.expressions

import android.text.InputType
import com.vicmikhailau.maskededittext.MaskedEditText
import com.vicmikhailau.maskededittext.MaskedFormatter

fun MaskedEditText.addPhoneMask() {
    this.apply {
        inputType = InputType.TYPE_CLASS_PHONE
        val mask = MaskedFormatter("(##) # ####-####")
    }
}

fun MaskedEditText.addCpfMask() {
    this.apply {
        inputType = InputType.TYPE_CLASS_PHONE
        val mask = MaskedFormatter("###.###.###-##")
    }
}

fun MaskedEditText.addCnpjMask() {
    this.apply {
        inputType = InputType.TYPE_CLASS_PHONE
        val mask = MaskedFormatter("##.###.###/####-##")
    }
}

fun MaskedEditText.addEmailMask() {
    this.apply {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }
}


