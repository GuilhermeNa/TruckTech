package br.com.apps.model.test_cases

import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.payment_method.PixType
import br.com.apps.model.toDate
import java.time.LocalDateTime

fun sampleBank(): Bank = Bank(
    id = "bankId1",
    name = "BankName",
    code = 1,
    urlImage = "urlImage"
)

fun sampleBankAccount(): BankAccount = BankAccount(
    masterUid = "masterUid1",
    id = "bankAccountId1",
    employeeId = "employeeId1",
    bankId = "bankId1",
    insertionDate = LocalDateTime.of(2024, 1, 1, 0,0,0),
    branch = 123,
    accNumber = 123456,
    mainAccount = true,
    pixType = PixType.PHONE,
    pix = "(xx) x xxxx xxxx",
    bank = sampleBank()
)

fun sampleBankAccountDto(): BankAccountDto = BankAccountDto(
    masterUid = "masterUid1",
    id = "bankAccountId1",
    employeeId = "employeeId1",
    bankId = "bankId1",
    insertionDate = LocalDateTime.of(2024, 1, 1, 0,0,0).toDate(),
    branch = 123,
    accNumber = 123456,
    mainAccount = true,
    pixType = PixType.PHONE.description,
    pix = "(xx) x xxxx xxxx",
)