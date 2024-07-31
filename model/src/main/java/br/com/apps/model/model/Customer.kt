package br.com.apps.model.model

import br.com.apps.model.model.travel.Freight

/**
 * Data class representing a customer in the system.
 * Customers make requests for transporting goods through [Freight]'s.
 *
 * @property masterUid Unique identifier for the master record associated with this customer.
 * @property id Unique identifier for the [Customer].
 * @property cnpj The CNPJ (Cadastro Nacional da Pessoa Jurídica) number of the customer.
 * @property name The name of the customer.
 */
data class Customer(
    val masterUid: String,
    val id: String,
    val cnpj: String,
    val name: String
)