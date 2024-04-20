package br.com.apps.model.model

data class Label(

    val uid: String? = null,
    val id: String? = null,

    var name: String? = "",
    var icon: Int? = 0,
    var color: Int? = 0,
    val type: LabelType? = null,
    val isDefaultLabel: Boolean? = null,
    val isOperational: Boolean? = null
) {



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

