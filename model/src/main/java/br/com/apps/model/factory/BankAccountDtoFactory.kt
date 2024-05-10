package br.com.apps.model.factory

import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.factory.FactoryUtil.Companion.checkIfStringsAreBlank

object BankAccountDtoFactory {

    /**
     *
     * @exception IllegalArgumentException when any field is blank.
     */
    fun create(
        masterUid: String,
        employeeId: String,
        bankName: String,
        branch: String,
        accNumber: String,
        type: String,
        pix: String,
        mainAccount: Boolean
    ): BankAccountDto {

        checkIfStringsAreBlank(masterUid, employeeId, bankName, branch, accNumber, type, pix)

        return BankAccountDto(
            masterUid = masterUid,
            employeeId = employeeId,
            bankName = bankName,
            branch = branch.toInt(),
            accNumber = accNumber.toInt(),
            pix = pix,
            pixType = type,
            mainAccount = mainAccount
        )

    }

}
