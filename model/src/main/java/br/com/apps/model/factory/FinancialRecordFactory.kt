package br.com.apps.model.factory

import br.com.apps.model.dto.finance.FinancialRecordDto
import br.com.apps.model.dto.finance.TransactionDto
import br.com.apps.model.dto.finance.payable.EmployeePayableDto
import br.com.apps.model.dto.finance.receivable.EmployeeReceivableDto
import br.com.apps.model.enums.EmployeePayableTicket
import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.enums.TransactionType
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.model.util.toDate
import java.io.InvalidClassException
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

class FinancialRecordFactory {

    fun <T> create(params: FinancialRecordsParams<T>): Pair<FinancialRecordDto, List<TransactionDto>> {
        return when (params.data) {
            is Loan -> fromLoan(params)
            is Advance -> fromAdvance(params)
            is TravelAid -> fromTravelAid(params)
            is Freight -> fromFreight(params)
            is Outlay -> fromOutlay(params)
            else -> {
                throw InvalidClassException("This class is not registered")
            }
        }
    }

    private fun fromOutlay(params: FinancialRecordsParams<*>): Pair<EmployeePayableDto, List<TransactionDto>> {
        val outlay = params.data as Outlay
        val ins = params.installments

        val dto = EmployeePayableDto(
            masterUid = outlay.masterUid,
            parentId = outlay.id,
            value = outlay.value.toDouble(),
            generationDate = outlay.date.toDate(),
            installments = ins,
            employeeId = outlay.employeeId,
            type = EmployeePayableTicket.OUTLAY.name,
            isPaid = false
        )

        val transactionsDto = generateTransactions(
            masterUid = outlay.masterUid,
            firstDueDate = params.firstDueDate,
            totalValue = outlay.value,
            type = TransactionType.PAYABLE,
            installments = ins,
            maximumInstallments = 1
        )

        return Pair(dto, transactionsDto)
    }

    private fun fromFreight(params: FinancialRecordsParams<*>): Pair<EmployeePayableDto, List<TransactionDto>> {
        val freight = params.data as Freight
        val ins = params.installments
        val firstDueDate = params.firstDueDate

        val dto = EmployeePayableDto(
            masterUid = freight.masterUid,
            parentId = freight.id,
            value = freight.getCommissionValue().toDouble(),
            generationDate = freight.loadingDate.toDate(),
            installments = ins,
            employeeId = freight.employeeId,
            type = EmployeePayableTicket.COMMISSION.name,
            isPaid = false
        )

        val transactionsDto = generateTransactions(
            masterUid = freight.masterUid,
            firstDueDate = firstDueDate,
            totalValue = freight.getCommissionValue(),
            type = TransactionType.PAYABLE,
            installments = ins,
            maximumInstallments = 1
        )

        return Pair(dto, transactionsDto)
    }

    private fun fromAdvance(params: FinancialRecordsParams<*>): Pair<EmployeeReceivableDto, List<TransactionDto>> {
        val advance = params.data as Advance
        val installments = params.installments
        val firstDueDate = params.firstDueDate

        val dto = EmployeeReceivableDto(
            masterUid = advance.masterUid,
            parentId = advance.id,
            value = advance.value.toDouble(),
            generationDate = advance.date.toDate(),
            installments = installments,
            employeeId = advance.employeeId,
            type = EmployeeReceivableTicket.ADVANCE.name,
            isReceived = false
        )

        val transactionsDto = generateTransactions(
            masterUid = advance.masterUid,
            firstDueDate = firstDueDate,
            totalValue = advance.value,
            type = TransactionType.RECEIVABLE,
            installments = installments,
            maximumInstallments = 1
        )

        return Pair(dto, transactionsDto)
    }

    private fun fromLoan(params: FinancialRecordsParams<*>): Pair<EmployeeReceivableDto, List<TransactionDto>> {
        val loan = params.data as Loan
        val installments = params.installments
        val firstDueDate = params.firstDueDate

        val dto = EmployeeReceivableDto(
            masterUid = loan.masterUid,
            parentId = loan.id,
            value = loan.value.toDouble(),
            generationDate = loan.date.toDate(),
            installments = installments,
            employeeId = loan.employeeId,
            type = EmployeeReceivableTicket.LOAN.name,
            isReceived = false
        )

        val transactionsDto = generateTransactions(
            masterUid = loan.masterUid,
            firstDueDate = firstDueDate,
            totalValue = loan.value,
            type = TransactionType.RECEIVABLE,
            installments = installments,
            maximumInstallments = 24
        )

        return Pair(dto, transactionsDto)
    }

    private fun fromTravelAid(params: FinancialRecordsParams<*>): Pair<EmployeeReceivableDto, List<TransactionDto>> {
        val aid = params.data as TravelAid
        val installments = params.installments
        val firstDueDate = params.firstDueDate

        val dto = EmployeeReceivableDto(
            masterUid = aid.masterUid,
            parentId = aid.id,
            value = aid.value.toDouble(),
            generationDate = aid.date.toDate(),
            installments = installments,
            employeeId = aid.employeeId,
            type = EmployeeReceivableTicket.TRAVEL_AID.name,
            isReceived = false
        )

        val transactionsDto = generateTransactions(
            masterUid = aid.masterUid,
            firstDueDate = firstDueDate,
            totalValue = aid.value,
            type = TransactionType.RECEIVABLE,
            installments = installments,
            maximumInstallments = 1
        )

        return Pair(dto, transactionsDto)
    }

    private fun generateTransactions(
        masterUid: String,
        firstDueDate: LocalDateTime,
        totalValue: BigDecimal,
        type: TransactionType,
        installments: Int,
        maximumInstallments: Int
    ): List<TransactionDto> {
        val ml = mutableListOf<TransactionDto>()
        var d = firstDueDate
        var n = 1

        val ins = if(installments > maximumInstallments) maximumInstallments else installments
        val slice = totalValue.divide(BigDecimal(ins), 2, RoundingMode.HALF_EVEN)

        for (i in 0 until ins) {
            val dto = TransactionDto(
                masterUid = masterUid,
                dueDate = d.toDate(),
                number = n,
                value = slice.toDouble(),
                type = type.name,
                isPaid = false
            )

            ml.add(dto)

            d = d.plusDays(30)
            n++
        }

        return ml.toList()
    }

}

class FinancialRecordsParams<T>(
    val data: T,
    val installments: Int,
    val firstDueDate: LocalDateTime
)