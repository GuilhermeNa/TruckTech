package br.com.apps.model.model.request

import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.exceptions.invalid.InvalidIdException
import br.com.apps.model.model.request.Request.Companion.merge
import br.com.apps.model.test_cases.sampleItem
import br.com.apps.model.test_cases.sampleRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class RequestTest {

    private lateinit var request: Request

    @Before
    fun setup() {
        request = sampleRequest()
    }

    //---------------------------------------------------------------------------------------------//
    // addItem()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add a item when data is correct`() {
        val item = sampleItem().copy(parentId = "requestId1")
        request.addItem(item)

        val expected = 1
        val actual = request.items.size

        assertEquals(expected, actual)
    }

    @Test
    fun `should throw DuplicatedItemsException when the id is repeated`() {
        val item = sampleItem().copy(parentId = "requestId1")
        request.addItem(item)

        assertThrows(DuplicatedItemsException::class.java) {
            request.addItem(item)
        }
    }

    @Test
    fun `should throw InvalidIdException when the id of item is different from parent id`() {
        val item = sampleItem().copy(parentId = "wrongId")

        assertThrows(InvalidIdException::class.java) {
            request.addItem(item)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // addAll()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add all items when data is correct`() {
        val item = sampleItem().copy(parentId = "requestId1", id = "itemId1")
        val itemB = sampleItem().copy(parentId = "requestId1", id = "itemId2")
        request.addAll(listOf(item, itemB))

        val expected = 2
        val actual = request.items.size

        assertEquals(expected, actual)
    }

    @Test
    fun `should not repeat insertion of items with repeated ids`() {
        val item = sampleItem().copy(parentId = "requestId1")
        val itemB = sampleItem().copy(parentId = "requestId1")
        request.addAll(listOf(item, itemB))

        val expected = 1
        val actual = request.items.size

        assertEquals(expected, actual)
    }

    //---------------------------------------------------------------------------------------------//
    // getValue()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should sum the items value`() {
        val item =
            sampleItem().copy(parentId = "requestId1", id = "itemId1", value = BigDecimal("100.00"))
        val itemB =
            sampleItem().copy(parentId = "requestId1", id = "itemId2", value = BigDecimal("100.00"))
        request.addAll(listOf(item, itemB))

        val expected = BigDecimal("200.00")
        val actual = request.getValue()

        assertEquals(expected, actual)
    }

    @Test
    fun `should sum when the item value is zero`() {
        val item =
            sampleItem().copy(parentId = "requestId1", id = "itemId1", value = BigDecimal.ZERO)
        request.addItem(item)

        val expected = BigDecimal.ZERO
        val actual = request.getValue()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return when list is empty`() {
        val expected = BigDecimal.ZERO
        val actual = request.getValue()

        assertEquals(expected, actual)
    }

    //---------------------------------------------------------------------------------------------//
    // merge()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should merge requests to items`() {
        val items = listOf(
            sampleItem().copy(id = "itemId1", parentId = "requestId1"),
            sampleItem().copy(id = "itemId2", parentId = "requestId2")
        )

        val requests = listOf(
            request,
            sampleRequest().copy(id = "requestId2")
        )

        requests.merge(items)

        val expected = "itemId1"
        val actual = requests[0].items[0].id
        assertEquals(expected, actual)

        val expectedA = "itemId2"
        val actualA = requests[1].items[0].id
        assertEquals(expectedA, actualA)

    }

}