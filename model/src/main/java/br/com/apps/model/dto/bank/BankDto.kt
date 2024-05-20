package br.com.apps.model.dto.bank

class BankDto(
    val id: String? = null,
    val name: String? = null,
    val code: Int? = null,
    val urlImage: String? = null
) {

    fun validateFields(): Boolean {
        var isValid = true

        if (id == null ||
            name == null ||
            code == null ||
            urlImage == null
        ) {
            isValid = false
        }

        return isValid
    }

}