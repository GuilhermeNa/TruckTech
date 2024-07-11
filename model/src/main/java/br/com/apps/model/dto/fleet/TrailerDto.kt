package br.com.apps.model.dto.fleet

data class TrailerDto(
    val masterUid: String? = null,
    val id: String? = null,
    val plate: String? = null,
    val fleetType: String? = null,
    val truckId: String? = null,
) {

    fun validateFields(): Boolean {
        if (id == null) return false
        if (plate == null) return false
        if (truckId == null) return false
        if (fleetType == null) return false
        if (masterUid == null) return false
        return true
    }

}