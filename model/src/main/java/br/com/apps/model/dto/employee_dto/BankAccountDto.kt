package br.com.apps.model.dto.employee_dto

data class BankAccountDto(
    var masterUid: String? = null,
    var id: String? = null,
    var employeeId: String? = null,
    var bankName: String? = null,
    var branch: Int? = null,
    var accNumber: Int? = null,
    var pix: String? = null,
    val code: String? = null,
    val mainAccount: Boolean? = false,
    var pixType: String? = null
) {

    fun validateFields(): Boolean {
        var isValid = true

        if (masterUid == null ||
            employeeId == null ||
            code == null ||
            bankName == null ||
            branch == null ||
            accNumber == null ||
            mainAccount == null
        ) {
            isValid = false
        }

        return isValid
    }

}