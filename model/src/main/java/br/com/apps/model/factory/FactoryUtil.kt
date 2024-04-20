package br.com.apps.model.factory

import java.security.InvalidParameterException

class FactoryUtil {

    companion object {

        fun checkStrings(vararg strings: String) {
            strings.forEach { text ->
                if(text.isBlank()) {
                    throw InvalidParameterException("String is blank")
                }
            }
        }

    }

}