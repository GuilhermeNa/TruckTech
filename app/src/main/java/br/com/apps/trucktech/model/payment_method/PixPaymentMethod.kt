package br.com.apps.trucktech.model.payment_method

import br.com.apps.model.model.payment_method.PixType

data class PixPaymentMethod(

    override val id: Long,
    override val requestId: Long,

    val name: String,
    val address: String,
    val type: PixType

) : PaymentMethod(
    id = id,
    requestId = id
) {

    

}
