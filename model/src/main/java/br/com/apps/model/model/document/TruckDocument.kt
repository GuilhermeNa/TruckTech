package br.com.apps.model.model.document

import br.com.apps.model.dto.document.TruckDocumentDto
import br.com.apps.model.enums.DocumentType
import br.com.apps.model.model.fleet.Fleet
import br.com.apps.model.model.label.Label
import br.com.apps.model.util.toDate
import java.time.LocalDateTime

/**
 * Represents a document related to a [Fleet].
 *
 * Considering that various types of documents with different names may exist and new types may
 * be introduced due to legal requirements, additional details will be defined through a [Label],
 * used as a flag which will provide additional information.
 *
 * @property masterUid Unique identifier of the master account associated with this account.
 * @property id Unique identifier for the [TruckDocument].
 * @property fleetId Unique Identifier for the [Fleet] item to which this document is associated.
 * @property labelId Unique Identifier for the [Label] associated with this document.
 * @property urlImage URL pointing to the image or file of the document.
 * @property expeditionDate Date and time when the document was dispatched.
 * @property expirationDate Optional date and time when the document expires, if applicable.
 * @property label Optional [Label] object containing additional details related to the document's label.
 */
data class TruckDocument(
    override val masterUid: String,
    override val id: String,
    override val labelId: String,
    override val urlImage: String,
    override val expeditionDate: LocalDateTime,
    override val type: DocumentType,

    val fleetId: String,
    val expirationDate: LocalDateTime? = null,
) : Document(
    masterUid = masterUid, id = id, labelId = labelId, type = type,
    urlImage = urlImage, expeditionDate = expeditionDate
) {

    override fun toDto() = TruckDocumentDto(
        masterUid = this.masterUid,
        id = this.id,
        type = type.name,
        fleetId = this.fleetId,
        labelId = this.labelId,
        urlImage = this.urlImage,
        expeditionDate = this.expeditionDate.toDate(),
        expirationDate = this.expirationDate?.toDate()
    )

}