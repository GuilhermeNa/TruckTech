package br.com.apps.model.model.bank

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.exceptions.NullBankException
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.payment_method.PixType
import java.time.LocalDateTime

/**
 * Represents a bank account associated with an [Employee].
 *
 * Each employee can register multiple bank accounts; however, only one account can
 * be designated as the [mainAccount]. The main account is specifically used for salary
 * payments and other primary transactions. This ensures that there is a clear,
 * designated account for essential financial operations related to the employee.
 *
 * @property masterUid Unique identifier of the master account associated with this account.
 * @property id Unique identifier of the [BankAccount].
 * @property employeeId Unique identifier of the [Employee] to whom the account belongs.
 * @property bankId Unique identifier of the [Bank] where the account is registered.
 * @property insertionDate Date and time when the account was added to the system.
 * @property branch The account branch number.
 * @property accNumber The account number.
 * @property mainAccount Indicates whether this account is the employee's main account.
 * @property pixType Type of PIX key (e.g., email, CPF). May be null if the PIX key is not defined.
 * @property pix PIX key associated with this account. May be null if there is no associated PIX.
 * @property bank A reference to the [Bank] where the account is registered.
 */
data class BankAccount(
    val masterUid: String,
    val id: String,
    val employeeId: String,
    var bankId: String,
    var insertionDate: LocalDateTime,
    var branch: Int,
    var accNumber: Int,
    var mainAccount: Boolean,
    var pixType: PixType? = null,
    var pix: String? = null,
    var bank: Bank? = null
) {

    /**
     * Returns a description based on the PIX type.
     *
     * @return Description of the PIX type.
     * @throws IllegalArgumentException if [pixType] is null or not recognized.
     */
    fun getTypeDescription(): String {
        return when (pixType) {
            PixType.PHONE -> "Celular"
            PixType.EMAIL -> "Email"
            PixType.CPF -> "Cpf"
            PixType.CNPJ -> "Cnpj"
            else -> throw InvalidTypeException("Fun getDescription needs a valid type")
        }
    }

    /**
     * Get the name of the bank associated with this account.
     * @return The name of the bank or "Error" if the bank is not registered.
     */
    fun getBankName(): String {
        return try {
            bank!!.name
        } catch (e: Exception) {
            e.printStackTrace()
            "Erro"
        }
    }

    /**
     * Retrieves the URL of the bank's image associated with this [BankAccount].
     * @return The URL of the bank's image if the [Bank] is not `null` and the property is accessible; otherwise, returns an empty string.
     */
    fun getBankUrlImage(): String {
        return try {
            bank!!.urlImage
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Sets the [bank] property of this [BankAccount] based on the provided list of [Bank]'s.
     * @param bankList A list of [Bank]'s objects to search for the [Bank] with the matching ID.
     * @throws NullBankException If no [Bank] in the [bankList] has an ID that matches the [bankId] of this [BankAccount].
     */
    fun setBankById(bankList: List<Bank>) {
        bank = bankList.firstOrNull { it.id == bankId } ?: throw NullBankException("Bank not found")
    }

    /**
     * Retrieves the code of the bank associated with this [BankAccount].
     * @return The code of the [Bank]; otherwise, returns `-1`.
     */
    fun getBankCode(): Int {
        return try {
            bank!!.code
        }catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

}



