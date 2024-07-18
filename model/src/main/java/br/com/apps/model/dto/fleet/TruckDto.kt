package br.com.apps.model.dto.fleet

import br.com.apps.model.dto.DtoInterface
import br.com.apps.model.exceptions.CorruptedFileException

data class TruckDto(
    var id: String? = null,
    var masterUid: String? = null,
    var driverId: String? = null,

    var averageAim: Double? = null,
    var performanceAim: Double? = null,
    var plate: String? = null,
    var color: String? = null,
    var commissionPercentual: Double? = null,
    var fleetType: String? = null
) : DtoInterface {

    override fun validateDataIntegrity() {
        if (id == null ||
            plate == null ||
            color == null ||
            driverId == null ||
            masterUid == null ||
            fleetType == null ||
            averageAim == null ||
            performanceAim == null ||
            commissionPercentual == null
        ) throw CorruptedFileException("TruckDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {}

}