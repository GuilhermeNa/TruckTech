package br.com.apps.model.model.payment_method

data class BankTransferMethod(

    override val id: Long? = 0L,
    override val requestId: Long? = 0L,

    val accountNumber: Int? = 0,
    val accountNumberDigit: Int? = 0,
    val agencyNumber: Int? = 0,
    val agencyDigit: Int? = 0,
    val identifierNumber: Int? = 0,
    val name: String? = "",
    val identifier: IdentifierType,

): PaymentMethod(
    id = id,
    requestId = requestId
)

enum class IdentifierType {
    CPF, CNPJ
}

