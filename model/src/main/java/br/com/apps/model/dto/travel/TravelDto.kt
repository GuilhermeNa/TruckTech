package br.com.apps.model.dto.travel

import br.com.apps.model.dto.DtoInterface
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.TravelAid
import java.util.Date

data class TravelDto(
    var masterUid: String? = null,
    var id: String? = null,
    var truckId: String? = null,
    var driverId: String? = null,
    @field:JvmField
    var isFinished: Boolean? = null,
    var considerAverage: Boolean? = null,
    var initialDate: Date? = null,
    var finalDate: Date? = null,
    val initialOdometerMeasurement: Double? = null,
    val finalOdometerMeasurement: Double? = null,
    val freightsList: List<Freight>? = null,
    val refuelsList: List<Refuel>? = null,
    val expendsList: List<Expend>? = null,
    val aidList: List<TravelAid>? = null
) : DtoInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            truckId == null ||
            driverId == null ||
            isFinished == null ||
            considerAverage == null ||
            initialDate == null ||
            initialOdometerMeasurement == null
        ) throw CorruptedFileException("TravelDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {
        if (masterUid == null ||
            truckId == null ||
            driverId == null ||
            isFinished == null ||
            considerAverage == null ||
            initialDate == null ||
            finalDate == null ||
            initialOdometerMeasurement == null ||
            finalOdometerMeasurement == null
        ) throw InvalidForSavingException("TravelDto data is invalid: ($this)")
    }

}


