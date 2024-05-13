package br.com.apps.model.factory

import java.security.InvalidParameterException

class FactoryUtil {

    companion object {

        fun checkIfStringsAreBlank(vararg strings: String) {
            strings.forEach { text ->
                if(text.isBlank()) {
                    throw InvalidParameterException("FactoryUtil, checkIfStringsAreBlank: String is blank")
                }
            }
        }

    }

}