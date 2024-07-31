package br.com.apps.model.dto.fleet

import br.com.apps.model.dto.DtoObjectsInterface
import br.com.apps.model.exceptions.CorruptedFileException

data class TrailerDto(
    var masterUid: String? = null,
    var id: String? = null,
    var plate: String? = null,
    var fleetType: String? = null,
    var truckId: String? = null,
) : DtoObjectsInterface {

    override fun validateDataIntegrity() {
        if (id == null ||
            plate == null ||
            truckId == null ||
            fleetType == null ||
            masterUid == null
        ) throw CorruptedFileException("TrailerDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {

    }

}