package br.com.apps.model.model.request.request

import br.com.apps.model.exceptions.InvalidTypeException
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentRequest(
    val masterUid: String? = null,
    val id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    val date: LocalDateTime? = null,
    val requestNumber: Int? = 0,
    val status: PaymentRequestStatusType? = null,
    val itemsList: MutableList<RequestItem>? = mutableListOf()

) {

    fun getTotalValue(): BigDecimal {
        return itemsList?.sumOf {
            it.value ?: BigDecimal.ZERO
        } ?: BigDecimal.ZERO
    }

    fun getNumberOfItemsByType(type: RequestItemType): Int {
        return itemsList?.count { it.type == type } ?: 0
    }

}

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
                else -> throw InvalidTypeException("Fun getType needs a valid type")
            }
        }
    }

}