package br.com.apps.model.factory

import br.com.apps.model.factory.FactoryUtil.Companion.checkIfStringsAreBlank
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.toLocalDateTime

object TravelFactory {

    fun create(factoryData: TravelFactoryData): Travel {
        val masterUid = factoryData.masterUid
            ?: throw NullPointerException("TravelFactory, create: masterUid is null")

        val truckId = factoryData.truckId
            ?: throw NullPointerException("TravelFactory, create: truckId is null")

        val driverId = factoryData.driverId
            ?: throw NullPointerException("TravelFactory, create: driverId is null")

        val isFinished = factoryData.isFinished
            ?: throw NullPointerException("TravelFactory, create: isFinished is null")

        val initialDate = factoryData.initialDate
            ?: throw NullPointerException("TravelFactory, create: initialDate is null")

        checkIfStringsAreBlank(masterUid, truckId, driverId, isFinished, initialDate)

        return Travel(
            masterUid = masterUid,
            truckId = truckId,
            driverId = driverId,
            isFinished = isFinished.toBoolean(),
            initialDate = initialDate.toLocalDateTime()
        )

    }

}

class TravelFactoryData(
    val masterUid: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,
    val isFinished: String? = null,
    val initialDate: String? = null,
)