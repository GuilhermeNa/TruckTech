package br.com.apps.model.model.travel

import br.com.apps.model.model.label.Label
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal
import java.security.InvalidParameterException
import java.time.LocalDateTime

data class Expend(
    val masterUid: String? = null,
    val id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,
    val travelId: String? = null,
    var labelId: String? = null,

    var company: String? = null,
    var date: LocalDateTime? = null,
    var description: String? = null,
    var value: BigDecimal? = null,
    var label: Label? = null,

    var paidByEmployee: Boolean? = null,
    var alreadyRefunded: Boolean? = null

) {

    /**
     * Responsible for updating this object.
     *
     * @param mappedFields A HashMap of Strings containing a key
     * with the field name and value containing its new value.
     */
    fun updateFields(mappedFields: HashMap<String, String>) {
        mappedFields.forEach { (key, value) ->
            when (key) {
                TAG_DATE -> this.date = value.toLocalDateTime()
                TAG_COMPANY -> this.company = value
                TAG_DESCRIPTION -> this.description = value
                TAG_VALUE -> this.value = BigDecimal(value)
                TAG_LABEL_ID -> this.labelId = value
                else -> throw InvalidParameterException("Invalid key")
            }
        }
    }

    /**
     * Field names used as keys for mapping in [updateFields]
     */
    companion object {
        const val TAG_COMPANY = "company"
        const val TAG_DATE = "date"
        const val TAG_DESCRIPTION = "description"
        const val TAG_VALUE = "value"
        const val TAG_LABEL_ID = "labelId"
    }

}