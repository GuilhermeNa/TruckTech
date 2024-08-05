package br.com.apps.model.model

import br.com.apps.model.util.ERROR_STRING
import br.com.apps.model.dto.TruckDocumentDto
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.null_objects.NullLabelException
import br.com.apps.model.interfaces.LabelInterface
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.fleet.Fleet
import br.com.apps.model.model.label.Label
import br.com.apps.model.util.toDate
import java.io.Serializable
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
    val masterUid: String,
    val id: String,
    val fleetId: String,
    val labelId: String,
    val urlImage: String,
    val expeditionDate: LocalDateTime,
    val expirationDate: LocalDateTime? = null,
    var label: Label? = null
) : LabelInterface, Serializable, ModelObjectInterface<TruckDocumentDto> {

    override fun setLabelById(labels: List<Label>) {
        if (labels.isEmpty()) throw EmptyDataException("Label list cannot be empty")

        label = labels.firstOrNull { it.id == labelId }
            ?: throw NullLabelException("Label not found")
    }

    override fun getLabelName(): String {
        return try {
            label!!.name
        } catch (e: Exception) {
            e.printStackTrace()
            ERROR_STRING
        }
    }

    override fun toDto() = TruckDocumentDto(
        masterUid = this.masterUid,
        id = this.id,
        fleetId = this.fleetId,
        labelId = this.labelId,
        urlImage = this.urlImage,
        expeditionDate = this.expeditionDate.toDate(),
        expirationDate = this.expirationDate?.toDate()
    )

    companion object {

        /**
         * Extension function for [List] of [TruckDocument]' to merge with a list of [Label]'s.
         *
         * Each document in the list will have its label updated with
         * the corresponding `Label` from the `labels` list.
         *
         * @param labels A list of [Label]'s objects containing additional information of [TruckDocument].
         *
         * @return A [List] of [TruckDocument] with valid [Label]'s.
         */
        fun List<TruckDocument>.merge(labels: List<Label>) {
            this.forEach { it.setLabelById(labels) }
        }
    }

}