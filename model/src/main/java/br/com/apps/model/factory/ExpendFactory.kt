package br.com.apps.model.factory

import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.toDate
import java.security.InvalidParameterException
import java.time.LocalDateTime

object ExpendFactory {
    fun createDto(
        nMasterUid: String?,
        nTravelId: String?,
        nTruckId: String?,
        nLabelId: String?,
        nDriverId: String?,
        nDate: LocalDateTime?,
        nCompany: String?,
        nDescription: String?,
        nValue: String?
    ): ExpendDto {
        val masterUid = nMasterUid ?: throw InvalidParameterException("Null masterUid")
        val travelId = nTravelId ?: throw InvalidParameterException("Null travelId")
        val truckId = nTruckId ?: throw InvalidParameterException("Null truckId")
        val labelId = nLabelId ?: throw InvalidParameterException("Null labelId")
        val driverId = nDriverId ?: throw InvalidParameterException("Null driverId")
        val date = nDate ?: throw InvalidParameterException("Null date")
        val company = nCompany ?: throw InvalidParameterException("Null company")
        val description = nDescription ?: throw InvalidParameterException("Null description")
        val value = nValue ?: throw InvalidParameterException("Null value")

        return ExpendDto(
            masterUid = masterUid,
            travelId = travelId,
            truckId = truckId,
            labelId = labelId,
            driverId = driverId,
            date = date.toDate(),
            company = company,
            description = description,
            value = value.toDouble()
        )
    }
}