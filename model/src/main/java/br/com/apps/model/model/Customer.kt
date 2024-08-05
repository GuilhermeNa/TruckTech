package br.com.apps.model.model

import br.com.apps.model.dto.CustomerDto
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.travel.Freight

/**
 * Data class representing a customer in the system.
 *
 * Notes:
 * * Customers make requests for transporting goods through [Freight]'s.
 *
 * @property masterUid Unique identifier for the master record associated with this customer.
 * @property id Unique identifier for the [Customer].
 * @property cnpj The CNPJ (Cadastro Nacional da Pessoa Jurídica) number of the customer.
 * @property name The name of the customer.
 */
data class Customer(
    val masterUid: String,
    var id: String,
    val cnpj: String,
    val name: String
) : ModelObjectInterface<CustomerDto> {

    override fun toDto() = CustomerDto(
        masterUid = this.masterUid,
        id = this.id,
        cnpj = this.cnpj,
        name = this.name
    )

}