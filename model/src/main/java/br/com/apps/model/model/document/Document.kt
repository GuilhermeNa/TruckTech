package br.com.apps.model.model.document

import br.com.apps.model.dto.document.DocumentDto
import br.com.apps.model.enums.DocumentType
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.null_objects.NullLabelException
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.label.Label
import br.com.apps.model.util.ERROR_STRING
import java.io.Serializable
import java.time.LocalDateTime

abstract class Document(
    open val masterUid: String,
    open val id: String,
    open val labelId: String,
    open val urlImage: String,
    open val expeditionDate: LocalDateTime,
    open val type: DocumentType,
): ModelObjectInterface<DocumentDto>, Serializable {

    private var _label: Label? = null
    val label get() = _label


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
        fun List<Document>.merge(labels: List<Label>) {
            this.forEach {
                it.setLabelById(labels)
            }
        }
    }

    abstract override fun toDto(): DocumentDto

    fun setLabelById(labels: List<Label>) {
        if (labels.isEmpty()) throw EmptyDataException("Label list cannot be empty")

        _label = labels.firstOrNull { it.id == labelId }
            ?: throw NullLabelException("Label not found")
    }

    fun getLabelName(): String {
        return try {
            label!!.name
        } catch (e: Exception) {
            e.printStackTrace()
            ERROR_STRING
        }
    }

}