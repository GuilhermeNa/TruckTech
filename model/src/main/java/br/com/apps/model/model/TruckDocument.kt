package br.com.apps.model.model

import br.com.apps.model.exceptions.NullLabelException
import br.com.apps.model.model.fleet.Fleet
import br.com.apps.model.model.label.Label
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
) : Serializable {

    /**
     * Get the name of the document.
     * @return The name of the document or "Error" if the label is not registered.
     */
    fun getDocumentName(): String {
        return try {
            label!!.name
        } catch (e: Exception) {
            e.printStackTrace()
            "Erro"
        }
    }

    /**
     * Sets the [label] property of this document based on the provided list of labels.
     * @param labels A list of labels objects to search for the label with the matching ID.
     * @throws NullLabelException If no label in the label list has an ID that matches the [labelId]
     * of this truck document.
     */
    fun setLabelById(labels: List<Label>) {
        label =
            labels.firstOrNull { it.id == labelId } ?: throw NullLabelException("Label not found")
    }

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