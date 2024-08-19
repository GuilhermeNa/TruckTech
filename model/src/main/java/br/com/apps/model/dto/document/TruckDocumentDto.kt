package br.com.apps.model.dto.document

import br.com.apps.model.enums.DocumentType
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.document.TruckDocument
import br.com.apps.model.util.toLocalDateTime
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
    override val masterUid: String? = null,
    override var id: String? = null,
    override val labelId: String? = null,
    override val type: String? = null,
    override val urlImage: String? = null,
    override val expeditionDate: Date? = null,

    val fleetId: String? = null,
    val expirationDate: Date? = null
): DocumentDto(
    masterUid = masterUid, id = id, labelId = labelId, type = type,
    urlImage = urlImage, expeditionDate = expeditionDate
) {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            fleetId == null ||
            labelId == null ||
            type == null ||
            urlImage == null ||
            expeditionDate == null
        ) throw CorruptedFileException("BankAccountDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        TODO("Not yet implemented")
    }

    override fun toModel(): TruckDocument {
        validateDataIntegrity()
        return TruckDocument(
            masterUid = this.masterUid!!,
            id = this.id!!,
            fleetId = this.fleetId!!,
            labelId = this.labelId!!,
            urlImage = this.urlImage!!,
            type = DocumentType.valueOf(type!!),
            expeditionDate = this.expeditionDate?.toLocalDateTime()!!,
            expirationDate = this.expirationDate?.toLocalDateTime()!!
        )
    }

}