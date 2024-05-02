package br.com.apps.model.dto.payroll

import java.util.Date

data class LoanDto (
    val masterUid: String? = null,
    val id: String? = null,
    val employeeId: String? = null,

    val date: Date? = null,
    val value: Double? = null,

    val installments: Int? = null,
    val installmentsAlreadyPaid: Int? = null,

    @field:JvmField
    val isPaid: Boolean? = null

)