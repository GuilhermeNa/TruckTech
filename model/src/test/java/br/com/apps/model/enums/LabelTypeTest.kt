package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.label.LabelType
import org.junit.Assert
import org.junit.Test

class LabelTypeTest {

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return type COST when param is string COST`() {
        val type = LabelType.getType("COST")
        Assert.assertEquals(LabelType.COST, type)
    }

    @Test
    fun `should return type EXPENSE when param is string EXPENSE`() {
        val type = LabelType.getType("EXPENSE")
        Assert.assertEquals(LabelType.EXPENSE, type)
    }

    @Test
    fun `should return type INCOME when param is string INCOME`() {
        val type = LabelType.getType("INCOME")
        Assert.assertEquals(LabelType.INCOME, type)
    }

    @Test
    fun `should return type TRUCK_WALLET when param is string TRUCK_WALLET`() {
        val type = LabelType.getType("TRUCK_WALLET")
        Assert.assertEquals(LabelType.TRUCK_WALLET, type)
    }

    @Test
    fun `should return type DOCUMENT when param is string DOCUMENT`() {
        val type = LabelType.getType("DOCUMENT")
        Assert.assertEquals(LabelType.DOCUMENT, type)
    }

    @Test
    fun `should throw InvalidTypeException when type is not registered`() {
        Assert.assertThrows(InvalidTypeException::class.java) {
            LabelType.getType("")
        }
    }

}