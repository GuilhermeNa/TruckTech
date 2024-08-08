package br.com.apps.model.model.travel

import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.null_objects.NullLabelException
import br.com.apps.model.model.travel.Outlay.Companion.merge
import br.com.apps.model.test_cases.sampleCostLabel
import br.com.apps.model.test_cases.sampleExpend
import br.com.apps.model.util.ERROR_STRING
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class OutlayTest {

    private lateinit var outlay: Outlay

    @Before
    fun setup() {
        outlay = sampleExpend()
    }

    //---------------------------------------------------------------------------------------------//
    // setLabelById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should define the label for this expend`() {
        outlay.setLabelById(listOf(sampleCostLabel()))

        val expected = sampleCostLabel()
        val actual = outlay.label

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should throw exception when list of labels is empty`() {
        Assert.assertThrows(EmptyDataException::class.java) {
            outlay.setLabelById(emptyList())
        }
    }

    @Test
    fun `should throw exception when label is not found`() {
        val label = sampleCostLabel().apply { id = "anotherId1" }
        Assert.assertThrows(NullLabelException::class.java) {
            outlay.setLabelById(listOf(label))
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getLabelName()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the label name`() {
        outlay.setLabelById(listOf(sampleCostLabel()))

        val expected = "Name1"
        val name = outlay.getLabelName()

        Assert.assertEquals(expected, name)
    }

    @Test
    fun `should return default error text for name when the customer is null`() {
        val name = outlay.getLabelName()

        Assert.assertEquals(ERROR_STRING, name)
    }

    //---------------------------------------------------------------------------------------------//
    // merge()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun`should merge a expend list and a label list`() {
        val label = sampleCostLabel()

        listOf(outlay).merge(listOf(label))

        Assert.assertEquals(label, outlay.label)
    }

    @Test
    fun`should throw exception for merge when customer list is null`() {
        Assert.assertThrows(Exception::class.java) {
            listOf(outlay).merge(emptyList())
        }
    }

}