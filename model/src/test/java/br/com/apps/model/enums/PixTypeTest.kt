package br.com.apps.model.enums

import br.com.apps.model.enums.PixType.Companion.descriptionToPixType
import br.com.apps.model.enums.PixType.Companion.toPixType
import org.junit.Assert
import org.junit.Test

class PixTypeTest {

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return type PHONE when param is string PHONE`() {
        val type = "PHONE".toPixType()
        Assert.assertEquals(PixType.PHONE, type)
    }

    @Test
    fun `should return type EMAIL when param is string EMAIL`() {
        val type = "EMAIL".toPixType()
        Assert.assertEquals(PixType.EMAIL, type)
    }

    @Test
    fun `should return type CPF when param is string CPF`() {
        val type = "CPF".toPixType()
        Assert.assertEquals(PixType.CPF, type)
    }

    @Test
    fun `should return type CNPJ when param is string CNPJ`() {
        val type = "CNPJ".toPixType()
        Assert.assertEquals(PixType.CNPJ, type)
    }

    @Test
    fun `should throw IllegalArgumentException when type is not registered`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            "".toPixType()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // String.descriptionToPixType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return string 'PHONE' when text is 'Celular'`() {
        val type = "Celular".descriptionToPixType().toString()
        Assert.assertEquals("PHONE", type)
    }

    @Test
    fun `should return string 'EMAIL' when text is 'Email'`() {
        val type = "Email".descriptionToPixType().toString()
        Assert.assertEquals("EMAIL", type)
    }

    @Test
    fun `should return string 'CPF' when text is 'Cpf'`() {
        val type = "Cpf".descriptionToPixType().toString()
        Assert.assertEquals("CPF", type)
    }

    @Test
    fun `should return string 'CNPJ' when text is 'Cnpj'`() {
        val type = "Cnpj".descriptionToPixType().toString()
        Assert.assertEquals("CNPJ", type)
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