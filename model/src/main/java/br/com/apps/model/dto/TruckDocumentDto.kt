package br.com.apps.model.dto

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.TruckDocument
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [TruckDocument].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class TruckDocumentDto(
    //Ids
    val masterUid: String? = null,
    var id: String? = null,
    val fleetId: String? = null,
    val expenseId: String? = null,
    val labelId: String? = null,

    // Others
    val urlImage: String? = null,
    val expeditionDate: Date? = null,
    val expirationDate: Date? = null
): DtoObjectsInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            fleetId == null ||
            labelId == null ||
            urlImage == null ||
            expeditionDate == null
        ) throw CorruptedFileException("BankAccountDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {
        TODO("Not yet implemented")
    }


}