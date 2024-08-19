package br.com.apps.model.model

import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.null_objects.NullLabelException
import br.com.apps.model.model.document.Document.Companion.merge
import br.com.apps.model.model.document.TruckDocument
import br.com.apps.model.test_cases.sampleDocumentLabel
import br.com.apps.model.test_cases.sampleTruckDocument
import br.com.apps.model.util.ERROR_STRING
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class TruckDocumentTest {

    private lateinit var truckDocument: TruckDocument

    @Before
    fun setup() {
        truckDocument = sampleTruckDocument()
    }

    //---------------------------------------------------------------------------------------------//
    // setLabelById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should define the label for this truck document`() {
        val label = sampleDocumentLabel()
        truckDocument.setLabelById(listOf(label))
        assertEquals(label, truckDocument.label)
    }

    @Test
    fun `should throw exception when list of labels is empty`() {
        assertThrows(EmptyDataException::class.java) {
            truckDocument.setLabelById(emptyList())
        }
    }

    @Test
    fun `should throw exception when label is not found`() {
        val label = sampleDocumentLabel().apply { id = "anotherId1" }
        assertThrows(NullLabelException::class.java) {
            truckDocument.setLabelById(listOf(label))
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getLabelName()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the label name`() {
        val label = sampleDocumentLabel().copy(id = "labelId2" )
        truckDocument.setLabelById(listOf(label))
        val name = truckDocument.getLabelName()
        assertEquals("Name2", name)
    }

    @Test
    fun `should return default error text for name when the label is null`() {
        val name = truckDocument.getLabelName()

        assertEquals(ERROR_STRING, name)
    }

    //---------------------------------------------------------------------------------------------//
    // merge()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should merge a truck document list and a label list`() {
        val label = sampleDocumentLabel()

        listOf(truckDocument).merge(listOf(label))

        assertEquals(label, truckDocument.label)
    }

    @Test
    fun `should throw exception for merge when label list is empty`() {
        assertThrows(Exception::class.java) {
            listOf(truckDocument).merge(emptyList())
        }
    }


}