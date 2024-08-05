package br.com.apps.model.model.bank

import br.com.apps.model.dto.bank.BankDto
import br.com.apps.model.interfaces.ModelObjectInterface

/**
 * Represents a bank with its essential details.
 *
 * Notes:
 * * The list of banks is consistent across all users and should only be modified by a database administrator.
 * * Used as part of [BankAccount] data.
 *
 * @property id A unique identifier for the bank.
 * @property name The name of the bank.
 * @property code The bank's code (often a numeric identifier).
 * @property urlImage The URL to the bank's logo or image.
 */
data class Bank(
    var id: String,
    val name: String,
    val code: Int,
    val urlImage: String
): ModelObjectInterface<BankDto> {

    override fun toDto() = BankDto(
            id = this.id,
            name = this.name,
            code = this.code,
            urlImage = this.urlImage
        )

}