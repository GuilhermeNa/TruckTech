package br.com.apps.model.model

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.payment_method.PixType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class BankAccountTest {

    private lateinit var bankAcc: BankAccount

    @Before
    fun setup() {
        bankAcc = BankAccount(
            masterUid = "1",
            id = "2",
            employeeId = "3",
            bankId = "4",
            insertionDate = LocalDateTime.of(2024, 7, 15, 12, 0, 0),
            branch = 321,
            accNumber = 123456,
            mainAccount = true,
            pix = "111.111.111-11",
            pixType = PixType.CPF,
            bank = Bank(id = "4", name = "Bradesco", code = 123, urlImage = "urlImage")
        )
    }

    //---------------------------------------------------------------------------------------------//
    // getTypeDescription()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return string Celular when the pixType is PHONE `() {
        bankAcc.pixType = PixType.PHONE
        val description = bankAcc.getTypeDescription()
        Assert.assertEquals("Celular", description)
    }

    @Test
    fun `should return string Email when the pixType is EMAIL`() {
        bankAcc.pixType = PixType.EMAIL
        val description = bankAcc.getTypeDescription()
        Assert.assertEquals("Email", description)
    }

    @Test
    fun `should return string Cpf when the pixType is CPF`() {
        bankAcc.pixType = PixType.CPF
        val description = bankAcc.getTypeDescription()
        Assert.assertEquals("Cpf", description)
    }

    @Test
    fun `should return string Cnpj when the pixType is CNPJ`() {
        bankAcc.pixType = PixType.CNPJ
        val description = bankAcc.getTypeDescription()
        Assert.assertEquals("Cnpj", description)
    }

    @Test
    fun `should throw InvalidTypeException when the type is not configured`() {
        bankAcc.pixType = null
        Assert.assertThrows(InvalidTypeException::class.java) {
            bankAcc.getTypeDescription()
        }
    }

}