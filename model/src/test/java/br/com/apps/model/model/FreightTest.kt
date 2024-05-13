package br.com.apps.model.model

import br.com.apps.model.model.travel.Freight
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class FreightTest {

    private val freight = Freight(
        masterUid = "1",
        id = "2",
        truckId = "3",
        driverId = "4",
        travelId = "5",

        origin = "Origem",
        company = "Companhia",
        destiny = "Destino",
        weight = BigDecimal(30000),
        cargo = "Carga",
        breakDown = BigDecimal(100),
        value = BigDecimal(12000),
        loadingDate = LocalDateTime.of(2022, 1, 1, 12, 0),

        dailyValue = BigDecimal(10),
        daily = 3,
        dailyTotalValue = BigDecimal(30),

        isCommissionPaid = true,
        commissionPercentual = BigDecimal(11),
    )

    @Test
    fun `should return true when all ids are valid`() {
        val isIdsValid = freight.validateIds()
        Assert.assertTrue(isIdsValid)
    }

    @Test
    fun `should return false when any id is null or empty`() {
        val freightWithMasterUidNull = Freight(masterUid = null, id = "2", truckId = "3", driverId = "4", travelId = "5")
        val freightWithIdNull = Freight(masterUid = "1", id = null, truckId = "3", driverId = "4", travelId = "5")
        val freightWithTruckIdNull = Freight(masterUid = "1", id = "2", truckId = null, driverId = "4", travelId = "5")
        val freightWithDriverIdNull = Freight(masterUid = "1", id = "2", truckId = "3", driverId = null, travelId = "5")
        val freightWithTravelIdNull = Freight(masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = null)
        val isValidA = freightWithMasterUidNull.validateIds()
        val isValidB = freightWithIdNull.validateIds()
        val isValidC = freightWithTruckIdNull.validateIds()
        val isValidD = freightWithDriverIdNull.validateIds()
        val isValidE = freightWithTravelIdNull.validateIds()
        Assert.assertFalse(isValidA)
        Assert.assertFalse(isValidB)
        Assert.assertFalse(isValidC)
        Assert.assertFalse(isValidD)
        Assert.assertFalse(isValidE)

        val freightWithMasterUidEmpty = Freight(masterUid = "", id = "2", truckId = "3", driverId = "4", travelId = "5")
        val freightWithIdEmpty = Freight(masterUid = "1", id = "", truckId = "3", driverId = "4", travelId = "5")
        val freightWithTruckIdEmpty = Freight(masterUid = "1", id = "2", truckId = "", driverId = "4", travelId = "5")
        val freightWithDriverIdEmpty = Freight(masterUid = "1", id = "2", truckId = "3", driverId = "", travelId = "5")
        val freightWithTravelIdEmpty= Freight(masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = "")
        val isValidF = freightWithMasterUidEmpty.validateIds()
        val isValidG = freightWithIdEmpty.validateIds()
        val isValidH = freightWithTruckIdEmpty.validateIds()
        val isValidI = freightWithDriverIdEmpty.validateIds()
        val isValidJ = freightWithTravelIdEmpty.validateIds()
        Assert.assertFalse(isValidF)
        Assert.assertFalse(isValidG)
        Assert.assertFalse(isValidH)
        Assert.assertFalse(isValidI)
        Assert.assertFalse(isValidJ)
    }


}