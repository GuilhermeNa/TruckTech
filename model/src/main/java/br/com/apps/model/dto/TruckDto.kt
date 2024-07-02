package br.com.apps.model.dto

data class TruckDto(
    var id: String? = null,
    val masterUid: String? = null,
    val driverId: String? = null,

    val averageAim: Double? = null,
    val performanceAim: Double? = null,
    val plate: String? = null,
    val color: String? = null,
    val commissionPercentual: Double? = null
) {

    fun validateFields(): Boolean {
        var isValid = true

        if(id == null) isValid = false
        if(masterUid == null) isValid = false
        if(driverId == null) isValid = false
        if(averageAim == null) isValid = false
        if(performanceAim == null) isValid = false

        return isValid
    }

}