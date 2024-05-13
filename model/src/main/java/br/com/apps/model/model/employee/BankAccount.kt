package br.com.apps.model.model.employee

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.payment_method.PixType

data class BankAccount(
    val masterUid: String? = null,
    val id: String? = null,
    val employeeId: String? = null,

    var bankName: String? = null,
    var branch: Int? = null,
    var accNumber: Int? = null,
    var pix: String? = null,
    var image: String? = null,
    var mainAccount: Boolean? = false,
    var pixType: PixType? = null
) {

    fun getTypeDescription(): String {
        return when (pixType) {
            PixType.PHONE -> "Celular"
            PixType.EMAIL -> "Email"
            PixType.ID -> "Id"
            PixType.KEY -> "Chave"
            PixType.CPF -> "Cpf"
            PixType.CNPJ -> "Cnpj"
            else -> throw InvalidTypeException("Fun getDescription needs a valid type")
        }
    }

}


