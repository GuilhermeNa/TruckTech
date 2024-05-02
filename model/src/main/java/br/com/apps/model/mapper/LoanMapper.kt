package br.com.apps.model.mapper

import br.com.apps.model.dto.payroll.LoanDto
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal

fun LoanDto.toModel(): Loan {
    return Loan(
        masterUid = this.masterUid,
        id = this.id,
        employeeId = this.employeeId,
        date = this.date?.toLocalDateTime(),
        value = this.value?.let { BigDecimal(it) },
        installments = this.installments,
        installmentsAlreadyPaid = this.installmentsAlreadyPaid,
        isPaid = this.isPaid
    )
}

fun Loan.toDto(): LoanDto {
    return LoanDto(
        masterUid = this.masterUid,
        id = this.id,
        employeeId = this.employeeId,
        date = this.date?.toDate(),
        value = this.value?.toDouble(),
        installments = this.installments,
        installmentsAlreadyPaid = this.installmentsAlreadyPaid,
        isPaid = this.isPaid
    )
}