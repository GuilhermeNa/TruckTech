package br.com.apps.model.model.employee

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.payment_method.PixType

data class BankAccount(
    val masterUid: String? = null,
    val id: String? = null,
    val employeeId: String? = null,
    val bankName: String? = null,
    val branch: Int? = null,
    val accNumber: Int? = null,
    val pix: String? = null,
    val image: String? = null,
    val mainAccount: Boolean? = false,
    val pixType: PixType? = null
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


