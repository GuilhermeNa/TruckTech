package br.com.apps.model.model.request.travel_requests

import br.com.apps.model.exceptions.InvalidTypeException
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentRequest(
    val masterUid: String,
    val id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    val encodedImage: String? = null,
    val date: LocalDateTime,
    val requestNumber: Int,
    val status: PaymentRequestStatusType,
    var itemsList: MutableList<RequestItem>? = mutableListOf()
) {

    /**
     * Calculates and returns the total value of all items in the payment request.
     *
     * @return The total value of all items in the payment request as a BigDecimal.
     */
    fun getTotalValue(): BigDecimal {
        return if(itemsList.isNullOrEmpty()) BigDecimal.ZERO
        else itemsList!!.sumOf { it.value }
    }

    /**
     * Retrieves the number of items of a specific type within the payment request.
     *
     * @param type The type of request item to count.
     * @return The number of items of the specified type within the payment request.
     */
    fun getNumberOfItemsByType(type: RequestItemType): Int {
        return if(itemsList.isNullOrEmpty())  0
        else itemsList!!.count { it.type == type }
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
                else -> throw InvalidTypeException("Invalid type for string ($type)")
            }
        }
    }

}