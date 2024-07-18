package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.request.travel_requests.PaymentRequestStatusType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PaymentRequestStatusTypeTest {

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return type SENT when param is string SENT`() {
        val type = PaymentRequestStatusType.getType("SENT")
        assertEquals(PaymentRequestStatusType.SENT, type)
    }

    @Test
    fun `should return type APPROVED when param is string APPROVED`() {
        val type = PaymentRequestStatusType.getType("APPROVED")
        assertEquals(PaymentRequestStatusType.APPROVED, type)
    }

    @Test
    fun `should return type DENIED when param is string DENIED`() {
        val type = PaymentRequestStatusType.getType("DENIED")
        assertEquals(PaymentRequestStatusType.DENIED, type)
    }

    @Test
    fun `should return type PROCESSED when param is string PROCESSED`() {
        val type = PaymentRequestStatusType.getType("PROCESSED")
        assertEquals(PaymentRequestStatusType.PROCESSED, type)
    }

    @Test
    fun `should throw InvalidTypeException when type is not registered`() {
        assertThrows(InvalidTypeException::class.java) {
            PaymentRequestStatusType.getType("Invalid Type")
        }
    }

}