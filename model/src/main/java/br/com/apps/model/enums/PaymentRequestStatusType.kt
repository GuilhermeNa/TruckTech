package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException

enum class PaymentRequestStatusType(val description: String) {
    SENT("SENT"),
    APPROVED("APPROVED"),
    DENIED("DENIED"),
    PROCESSED("PROCESSED");

    companion object {
        fun getType(type: String): PaymentRequestStatusType {
            return when (type) {
                SENT.description -> SENT
                APPROVED.description -> APPROVED
                DENIED.description -> DENIED
                PROCESSED.description -> PROCESSED
                else -> throw InvalidTypeException("Invalid type for string ($type)")
            }
        }
    }

}