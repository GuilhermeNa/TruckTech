package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.payment_method.PixType
import org.junit.Assert
import org.junit.Test

class PixTypeTest {

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return type PHONE when param is string PHONE`() {
        val type = PixType.getType("PHONE")
        Assert.assertEquals(PixType.PHONE, type)
    }

    @Test
    fun `should return type EMAIL when param is string EMAIL`() {
        val type = PixType.getType("EMAIL")
        Assert.assertEquals(PixType.EMAIL, type)
    }

    @Test
    fun `should return type CPF when param is string CPF`() {
        val type = PixType.getType("CPF")
        Assert.assertEquals(PixType.CPF, type)
    }

    @Test
    fun `should return type CNPJ when param is string CNPJ`() {
        val type = PixType.getType("CNPJ")
        Assert.assertEquals(PixType.CNPJ, type)
    }

    @Test
    fun `should throw IllegalArgumentException when type is not registered`() {
        Assert.assertThrows(InvalidTypeException::class.java) {
            PixType.getType("")
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getTypeInString()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return string 'PHONE' when text is 'Celular'`() {
        val type = PixType.getTypeInString("Celular")
        Assert.assertEquals("PHONE", type)
    }

    @Test
    fun `should return string 'EMAIL' when text is 'Email'`() {
        val type = PixType.getTypeInString("Email")
        Assert.assertEquals("EMAIL", type)
    }

    @Test
    fun `should return string 'CPF' when text is 'Cpf'`() {
        val type = PixType.getTypeInString("Cpf")
        Assert.assertEquals("CPF", type)
    }

    @Test
    fun `should return string 'CNPJ' when text is 'Cnpj'`() {
        val type = PixType.getTypeInString("Cnpj")
        Assert.assertEquals("CNPJ", type)
    }

    @Test()
    fun `should throw IllegalArgumentException when text is invalid`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            PixType.getTypeInString("")
        }

    }

    //---------------------------------------------------------------------------------------------//
    // getMappedPixTypeAndDescription()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return correct map of PixType and description`() {
        val expectedMap = mapOf(
            PixType.PHONE to "Celular",
            PixType.EMAIL to "Email",
            PixType.CPF to "Cpf",
            PixType.CNPJ to "Cnpj"
        )
        val actualMap = PixType.getMappedPixTypeAndDescription()
        Assert.assertEquals(expectedMap, actualMap)
    }

    //---------------------------------------------------------------------------------------------//
    // isValidString(text: String)
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return true for valid string`() {
        val validStrings = listOf("Celular", "Email", "Cpf", "Cnpj")

        validStrings.forEach { text ->
            val isValid = PixType.isValidString(text)
            Assert.assertTrue(isValid)
        }
    }

    @Test
    fun `should return false for invalid string`() {
        val invalidString = "Invalid"

        val isValid = PixType.isValidString(invalidString)
        Assert.assertFalse(isValid)
    }

}