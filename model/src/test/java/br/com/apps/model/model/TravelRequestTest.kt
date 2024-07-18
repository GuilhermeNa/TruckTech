package br.com.apps.model.model

import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.PaymentRequestStatusType
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.request.travel_requests.RequestItemType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class TravelRequestTest {

    private lateinit var request: PaymentRequest

    @Before
    fun setup() {
        request  = PaymentRequest(
            masterUid = "1",
            id = "2",
            truckId = "3",
            driverId = "4",
            encodedImage = "encodedImage123",
            date = LocalDateTime.now(),
            requestNumber = 1,
            status = PaymentRequestStatusType.SENT,
            itemsList = mutableListOf(
                RequestItem(
                    id = "1",
                    requestId = "2",
                    docUrl = "docUrl1",
                    kmMarking = 100,
                    value = BigDecimal("150.00"),
                    type = RequestItemType.REFUEL
                ),
                RequestItem(
                    id = "2",
                    requestId = "2",
                    docUrl = "docUrl2",
                    kmMarking = null,
                    value = BigDecimal("200.00"),
                    type = RequestItemType.COST
                )
            )
        )
    }

    //---------------------------------------------------------------------------------------------//
    // getTotalValue()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return total value of the request when there is items list`() {
        val itemsValue = request.getTotalValue()
        assertEquals(BigDecimal("350.00"), itemsValue)
    }

    @Test
    fun `should return total value zero when the items list is null`() {
        request.itemsList = null
        val itemsValue = request.getTotalValue()
        assertEquals(BigDecimal.ZERO, itemsValue)
    }

    @Test
    fun `should return total value zero when the items list is empty`() {
        request.itemsList!!.clear()
        val itemsValue = request.getTotalValue()
        assertEquals(BigDecimal.ZERO, itemsValue)
    }

    //---------------------------------------------------------------------------------------------//
    // getNumberOfItemsByType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the number of items when the item type is found`() {
        val itemsNumber = request.getNumberOfItemsByType(RequestItemType.REFUEL)
        assertEquals(1, itemsNumber)
    }

    @Test
    fun `should return zero when the item type is not found`() {
        val itemsNumber = request.getNumberOfItemsByType(RequestItemType.WALLET)
        assertEquals(0, itemsNumber)
    }

    @Test
    fun `should return zero when the items list is null`() {
        request.itemsList = null
        val itemsNumber = request.getNumberOfItemsByType(RequestItemType.REFUEL)
        assertEquals(0, itemsNumber)
    }

    @Test
    fun `should return zero when the items list is empty`() {
        request.itemsList!!.clear()
        val itemsNumber = request.getNumberOfItemsByType(RequestItemType.REFUEL)
        assertEquals(0, itemsNumber)
    }


}