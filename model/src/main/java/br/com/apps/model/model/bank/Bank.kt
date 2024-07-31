package br.com.apps.model.model.bank

/**
 * Represents a bank with its essential details.
 *
 * Note: This list of bank objects is consistent across all users and should only be modified
 * by a database administrator.
 *
 * @property id A unique identifier for the bank.
 * @property name The name of the bank.
 * @property code The bank's code (often a numeric identifier).
 * @property urlImage The URL to the bank's logo or image.
 */
data class Bank(
    val id: String,
    val name: String,
    val code: Int,
    val urlImage: String
)