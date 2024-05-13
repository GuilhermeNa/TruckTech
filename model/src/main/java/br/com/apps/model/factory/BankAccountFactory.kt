package br.com.apps.model.factory

import br.com.apps.model.factory.FactoryUtil.Companion.checkIfStringsAreBlank
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.model.model.payment_method.PixType
import java.security.InvalidParameterException

object BankAccountFactory {

    const val TAG_MASTER_UID = "masterUid"
    const val TAG_EMPLOYEE_ID = "employeeId"

    const val TAG_BANK_NAME = "bankName"
    const val TAG_BRANCH = "branch"
    const val TAG_ACC_NUMBER = "accNumber"
    const val TAG_PIX = "pix"
    private const val TAG_IMAGE = "image"
    private const val TAG_MAIN_ACCOUNT = "mainAccount"
    const val TAG_PIX_TYPE = "pixType"

    fun create(mappedFields: HashMap<String, String>): BankAccount {
        val masterUid = mappedFields[TAG_MASTER_UID]
            ?: throw NullPointerException("BankAccountFactory, create: masterUid is null")

        val employeeId = mappedFields[TAG_EMPLOYEE_ID]
            ?: throw NullPointerException("BankAccountFactory, create: employeeId is null")

        val bankName = mappedFields[TAG_BANK_NAME]
            ?: throw NullPointerException("BankAccountFactory, create: bankName is null")

        val branch = mappedFields[TAG_BRANCH]
            ?: throw NullPointerException("BankAccountFactory, create: branch is null")

        val accNumber = mappedFields[TAG_ACC_NUMBER]
            ?: throw NullPointerException("BankAccountFactory, create: accNumber is null")

        val pixType = mappedFields[TAG_PIX_TYPE]
            ?: throw NullPointerException("BankAccountFactory, create: pixType is null")

        checkIfStringsAreBlank(
            masterUid, employeeId, bankName,
            branch, accNumber
        )

        return BankAccount(
            masterUid = masterUid,
            employeeId = employeeId,
            bankName = bankName,
            branch = branch.toInt(),
            accNumber = accNumber.toInt(),
            mainAccount = false,
            image = mappedFields[TAG_IMAGE],
            pix = mappedFields[TAG_PIX],
            pixType = PixType.getType(pixType)
        )

    }

    fun update(bankAccount: BankAccount, mappedFields: HashMap<String, String>) {
        mappedFields.forEach { (key, value) ->

            checkIfStringsAreBlank(value)

            when (key) {
                TAG_BANK_NAME -> bankAccount.bankName = value
                TAG_BRANCH -> bankAccount.branch = value.toInt()
                TAG_ACC_NUMBER -> bankAccount.accNumber = value.toInt()
                TAG_PIX -> bankAccount.pix = value
                TAG_IMAGE -> bankAccount.image = value
                TAG_MAIN_ACCOUNT -> bankAccount.mainAccount = value.toBoolean()
                TAG_PIX_TYPE -> bankAccount.pixType = PixType.getType(value)
                else -> throw InvalidParameterException(
                    "BankAccountFactory, update: Impossible update this field ($key)"
                )
            }
        }
    }

}
