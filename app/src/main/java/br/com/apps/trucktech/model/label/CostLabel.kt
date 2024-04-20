package br.com.apps.trucktech.model.label

data class CostLabel(

    override val id: Long,
    override var name: String

): Label(

    id = id,
    name = name

)