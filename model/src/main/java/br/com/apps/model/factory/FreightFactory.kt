package br.com.apps.model.factory

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.toDate
import java.security.InvalidParameterException
import java.time.LocalDateTime

object FreightFactory {

    fun createDto(
        nMasterId: String?,
        nTruckId: String?,
        nTravelId: String?,
        nOrigin: String?,
        nCompany: String?,
        nDestiny: String?,
        nWeight: String?,
        nCargo: String?,
        nValue: String?,
        nLoadingDate: LocalDateTime?,
    ): FreightDto {
        val masterUid = nMasterId ?: throw InvalidParameterException("Null masterUid")
        val truckId = nTruckId ?: throw InvalidParameterException("Null truckId")
        val travelId = nTravelId ?: throw InvalidParameterException("Null travelId")
        val origin = nOrigin ?: throw InvalidParameterException("Null origin")
        val company = nCompany ?: throw InvalidParameterException("Null company")
        val destiny = nDestiny ?: throw InvalidParameterException("Null destiny")
        val weight = nWeight ?: throw InvalidParameterException("Null weight")
        val cargo = nCargo ?: throw InvalidParameterException("Null cargo")
        val value = nValue ?: throw InvalidParameterException("Null value")
        val loadingDate = nLoadingDate ?: throw InvalidParameterException("Null date")

        return FreightDto(
            masterUid = masterUid,
            truckId = truckId,
            travelId = travelId,
            origin = origin,
            company = company,
            destiny = destiny,
            weight = weight.toDouble(),
            cargo = cargo,
            value = value.toDouble(),
            loadingDate = loadingDate.toDate()
        )
    }

}