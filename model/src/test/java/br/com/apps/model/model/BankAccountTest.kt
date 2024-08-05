package br.com.apps.model.model

import br.com.apps.model.enums.PixType
import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.exceptions.null_objects.NullBankException
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.test_cases.sampleBank
import br.com.apps.model.test_cases.sampleBankAccount
import br.com.apps.model.util.ERROR_INT
import br.com.apps.model.util.ERROR_STRING
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BankAccountTest {

    private lateinit var bankAccount: BankAccount

    @Before
    fun setup() {
        bankAccount = sampleBankAccount()
    }

    //---------------------------------------------------------------------------------------------//
    // getTypeDescription()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return string Celular when the pixType is PHONE `() {
        bankAccount.pixType = PixType.PHONE
        val description = bankAccount.getTypeDescription()
        Assert.assertEquals("Celular", description)
    }

    @Test
    fun `should return string Email when the pixType is EMAIL`() {
        bankAccount.pixType = PixType.EMAIL
        val description = bankAccount.getTypeDescription()
        Assert.assertEquals("Email", description)
    }

    @Test
    fun `should return string Cpf when the pixType is CPF`() {
        bankAccount.pixType = PixType.CPF
        val description = bankAccount.getTypeDescription()
        Assert.assertEquals("Cpf", description)
    }

    @Test
    fun `should return string Cnpj when the pixType is CNPJ`() {
        bankAccount.pixType = PixType.CNPJ
        val description = bankAccount.getTypeDescription()
        Assert.assertEquals("Cnpj", description)
    }

    @Test
    fun `should throw InvalidTypeException when the type is not configured`() {
        bankAccount.pixType = null
        Assert.assertThrows(InvalidTypeException::class.java) {
            bankAccount.getTypeDescription()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getBankName()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the bank name`() {
        val name = bankAccount.getBankName()
        Assert.assertEquals("Name1", name)
    }

    @Test
    fun `should return error text for bank name when bank is null`() {
        bankAccount.bank = null
        val name = bankAccount.getBankName()
        Assert.assertEquals(ERROR_STRING, name)
    }

    //---------------------------------------------------------------------------------------------//
    // getBankUrlImage()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the bank URL image`() {
        val urlImage = bankAccount.getBankUrlImage()
        Assert.assertEquals("urlImage", urlImage)
    }

    @Test
    fun `should return empty string for bank URL image when bank is null`() {
        bankAccount.bank = null
        val urlImage = bankAccount.getBankUrlImage()
        Assert.assertEquals(ERROR_STRING, urlImage)
    }

    //---------------------------------------------------------------------------------------------//
    // setBankById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should set the bank by ID from the bank list`() {
        val bank = sampleBank()
        bankAccount.setBankById(listOf(bank))
        Assert.assertEquals(bank, bankAccount.bank)
    }

    @Test
    fun `should throw exception when bank ID is not found in the list`() {
        val bank = sampleBank().apply { id = "anotherBankId" }
        Assert.assertThrows(NullBankException::class.java) {
            bankAccount.setBankById(listOf(bank))
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getBankCode()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the bank code`() {
        val code = bankAccount.getBankCode()
        Assert.assertEquals(1, code)
    }

    @Test
    fun `should return -1 for bank code when bank is null`() {
        bankAccount.bank = null
        val code = bankAccount.getBankCode()
        Assert.assertEquals(ERROR_INT, code)
    }

}