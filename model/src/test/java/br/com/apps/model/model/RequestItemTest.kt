package br.com.apps.model.model

import br.com.apps.model.enums.RequestItemType
import br.com.apps.model.model.request.RequestItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class RequestItemTest {

    private lateinit var requestItem: RequestItem

    @Before
    fun setup() {
        requestItem = RequestItem(
            id = "1",
            labelId = "2",
            requestId = "3",
            docUrl = "docUrl",
            label = null,
            kmMarking = null,
            value = BigDecimal("100.00"),
            type = RequestItemType.WALLET
        )
    }

    //---------------------------------------------------------------------------------------------//
    // getDescription()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return description Abastecimento for type REFUEL`() {
       requestItem.type = RequestItemType.REFUEL
        val description = requestItem.getDescription()
        assertEquals("Abastecimento", description)
    }

    @Test
    fun `should return description Despesa for type COST`() {
        requestItem.type = RequestItemType.COST
        val description = requestItem.getDescription()
        assertEquals("Despesa", description)
    }

    @Test
    fun `should return description Vale para viagem for type WALLET`() {
        requestItem.type = RequestItemType.WALLET
        val description = requestItem.getDescription()
        assertEquals("Vale para viagem", description)
    }

}
