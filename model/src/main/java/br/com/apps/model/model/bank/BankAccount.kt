package br.com.apps.model.model.bank

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.payment_method.PixType
import java.time.LocalDateTime

data class BankAccount(
    val masterUid: String,
    val id: String? = null,
    val employeeId: String,

    var insertionDate: LocalDateTime,
    var code: Int,
    var bankName: String,
    var branch: Int,
    var accNumber: Int,
    var mainAccount: Boolean,
    var pix: String? = null,
    var pixType: PixType? = null
) {

    fun getTypeDescription(): String {
        return when (pixType) {
            PixType.PHONE -> "Celular"
            PixType.EMAIL -> "Email"
            PixType.CPF -> "Cpf"
            PixType.CNPJ -> "Cnpj"
            else -> throw InvalidTypeException("Fun getDescription needs a valid type")
        }
    }

}


