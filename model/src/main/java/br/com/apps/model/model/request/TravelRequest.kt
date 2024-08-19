package br.com.apps.model.model.request

/*
import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.enums.PaymentRequestStatusType
import br.com.apps.model.enums.RequestItemType
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.util.toDate
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
): ModelObjectInterface<TravelRequestDto> {

    */
/**
     * Calculates and returns the total value of all items in the payment request.
     *
     * @return The total value of all items in the payment request as a BigDecimal.
     *//*

    fun getTotalValue(): BigDecimal {
        return if(itemsList.isNullOrEmpty()) BigDecimal.ZERO
        else itemsList!!.sumOf { it.value }
    }

    */
/**
     * Retrieves the number of items of a specific type within the payment request.
     *
     * @param type The type of request item to count.
     * @return The number of items of the specified type within the payment request.
     *//*

    fun getNumberOfItemsByType(type: RequestItemType): Int {
        return if(itemsList.isNullOrEmpty())  0
        else itemsList!!.count { it.type == type }
    }

    override fun toDto() = TravelRequestDto(
            masterUid = masterUid,
            id = id,
            driverId = driverId,
            truckId = truckId,
            encodedImage = encodedImage,
            requestNumber = requestNumber,
            date = date.toDate(),
            status = status.name
        )

}

*/
