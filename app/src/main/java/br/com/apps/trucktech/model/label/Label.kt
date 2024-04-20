package br.com.apps.trucktech.model.label

abstract class Label(

    open val id: Long,
    open var name: String

)

const val DEFAULT_FREIGHT_LABEL_ID = "pI7UEBdJQPz8jl7WVUao"
const val DEFAULT_COMMISSION_LABEL_ID = "n9M651rUc0CCNM9LcE8c"
const val DEFAULT_REFUEL_LABEL_ID = "IUFnqrpUlDK9u4Bn1Ibn"
const val DEFAULT_FINANCIAL_LABEL_ID = "1kmv9NwCjA7xG9F4fpXR"