package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException

enum class RequestItemType(val description: String) {
    REFUEL("REFUEL"),
    COST("COST"),
    WALLET("WALLET");

    companion object {
        fun getType(type: String): RequestItemType {
            return when (type) {
                REFUEL.description -> REFUEL
                COST.description -> COST
                WALLET.description -> WALLET
                else -> throw InvalidTypeException("Fun getType needs a valid type: ($type)")
            }
        }
    }

}