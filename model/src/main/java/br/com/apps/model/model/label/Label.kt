package br.com.apps.model.model.label

import br.com.apps.model.dto.LabelDto
import br.com.apps.model.enums.LabelCategory
import br.com.apps.model.interfaces.ModelObjectInterface

data class Label(
    val masterUid: String,
    var id: String,
    var name: String,
    var urlIcon: String? = null,
    var color: Int? = 0,
    val type: LabelCategory,
    val isDefaultLabel: Boolean,
    val isOperational: Boolean
) : ModelObjectInterface<LabelDto> {

    companion object {

        /**
         * Retrieves a list of names from a list of labels.
         *
         * @receiver The list of labels to retrieve names from.
         * @return List of names extracted from the labels.
         */
        fun List<Label>.getListOfTitles(): List<String> {
            return this.map { it.name }
        }

        /**
         * Checks if a list of labels contains a label with the specified name.
         *
         * @receiver The list of labels to search.
         * @param name The name of the label to check for.
         * @return `true` if a label with the specified name exists in the list, otherwise `false`.
         */
        fun List<Label>.containsByName(name: String): Boolean {
            return this.map { it.name }.contains(name)
        }

        /**
         * Retrieves the identifier of a label by its name from a list of labels.
         *
         * @receiver The list of labels to search.
         * @param name The name of the label to retrieve the identifier for.
         * @return The identifier of the label with the specified name, or `null` if no such label is found.
         */
        fun List<Label>.getIdByName(name: String): String? {
            return this.firstOrNull { it.name == name }?.id
        }

    }

    override fun toDto(): LabelDto {
        TODO("Not yet implemented")
    }

}



