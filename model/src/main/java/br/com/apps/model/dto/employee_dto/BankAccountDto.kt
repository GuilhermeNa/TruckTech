package br.com.apps.model.dto.employee_dto

data class BankAccountDto(
    val masterUid: String? = null,
    var id: String? = null,
    val employeeId: String? = null,
    var bankName: String? = null,
    var branch: Int? = null,
    var accNumber: Int? = null,
    var pix: String? = null,
    val image: String? = null,
    val mainAccount: Boolean? = false,
    var pixType: String? = null
)