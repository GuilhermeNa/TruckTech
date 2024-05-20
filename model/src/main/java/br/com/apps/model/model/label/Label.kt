package br.com.apps.model.model.label

const val DEFAULT_FREIGHT_LABEL_ID = "pI7UEBdJQPz8jl7WVUao"
const val DEFAULT_COMMISSION_LABEL_ID = "n9M651rUc0CCNM9LcE8c"
const val DEFAULT_REFUEL_LABEL_ID = "IUFnqrpUlDK9u4Bn1Ibn"
const val DEFAULT_FINANCIAL_LABEL_ID = "1kmv9NwCjA7xG9F4fpXR"
const val DEFAULT_FINANCIAL_COMPLEMENT_ID = "vYAf2oXK4cEgRzgTLMKP"
const val DEFAULT_FINANCIAL_DAILY_ID = "58Fh8JUftUbc5i3h2VbG"

data class Label(
    val masterUid: String? = null,
    val id: String? = null,

    var name: String? = "",
    var urlIcon: String? = null,
    var color: Int? = 0,
    val type: LabelType? = null,
    @field:JvmField
    val isDefaultLabel: Boolean? = null,
    @field:JvmField
    val isOperational: Boolean? = null
) {

    companion object {


        fun List<Label>.getListOfTitles(): List<String> {
            return this.mapNotNull { it.name }
        }

        fun List<Label>.containsByName(name: String): Boolean {
            return this.mapNotNull { it.name }.contains(name)
        }

        fun List<Label>.getIdByName(name: String): String? {
            return this.firstOrNull { it.name == name }?.id
        }

    }
}

enum class LabelType(val description: String) {
    COST("COST"),
    EXPENSE("EXPENSE"),
    INCOME("INCOME"),
    TRUCK_WALLET("TRUCK_WALLET"),
    DOCUMENT("DOCUMENT");

    companion object {
        fun getType(s: String?): LabelType? {
            return when (s) {
                "COST" -> COST
                "EXPENSE" -> EXPENSE
                "INCOME" -> INCOME
                "TRUCK_WALLET" -> TRUCK_WALLET
                "DOCUMENT" -> DOCUMENT
                else -> null
            }
        }
    }

}

