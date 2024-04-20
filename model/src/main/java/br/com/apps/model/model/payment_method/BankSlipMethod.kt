package br.com.apps.model.model.payment_method

data class BankSlipMethod(
    override val id: Long? = 0L,
    override val requestId: Long? = 0L,

    val barCode: String? = ""

): PaymentMethod(
    id = id,
    requestId = requestId
)