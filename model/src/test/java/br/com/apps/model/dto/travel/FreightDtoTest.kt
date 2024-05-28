package br.com.apps.model.dto.travel

import org.junit.Assert
import org.junit.Test
import java.util.Date

class FreightDtoTest {

    private val freight = FreightDto(
        masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
        origin = "Origin", customer = "Company", destiny = "Destiny", weight = 10.0, cargo = "Cargo",
        value = 100.0, breakDown = 10.0, loadingDate = Date(),
        dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
        isCommissionPaid = true, commissionPercentual = 10.0
    )

    @Test
    fun `should return true if there is no null field in non-null model fields`() {
        val isValid = freight.validateFields()
        Assert.assertTrue(isValid)
    }

    @Test
    fun `should return false if there is any null field in non-null model fields`() {
        val dtoA = FreightDto(
            masterUid = null, id = "1", truckId = "2", travelId = "3", driverId = "4",
            origin = "Origin", customer = "Company", destiny = "Destiny", weight = 10.0, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoB = FreightDto(
            masterUid = "1", id = "2", truckId = null, travelId = "3", driverId = "4",
            origin = "Origin", customer = "Company", destiny = "Destiny", weight = 10.0, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoC = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = null, driverId = "4",
            origin = "Origin", customer = "Company", destiny = "Destiny", weight = 10.0, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoD = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = null,
            origin = "Origin", customer = "Company", destiny = "Destiny", weight = 10.0, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoE = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            origin = null, customer = "Company", destiny = "Destiny", weight = 10.0, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoF = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            origin = "Origin", customer = null, destiny = "Destiny", weight = 10.0, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoG = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            origin = "Origin", customer = "Company", destiny = null, weight = 10.0, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoH = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            origin = "Origin", customer = "Company", destiny = "Destiny", weight = null, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoI = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            origin = "Origin", customer = "Company", destiny = "Destiny", weight = 10.0, cargo = null,
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoJ = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            origin = "Origin", customer = "Company", destiny = "Destiny", weight = 10.0, cargo = "Cargo",
            value = null, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoK = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            origin = "Origin", customer = "Company", destiny = "Destiny", weight = 10.0, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = null,
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = 10.0
        )
        val dtoL = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            origin = "Origin", customer = "Company", destiny = "Destiny", weight = 10.0, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = null, commissionPercentual = 10.0
        )
        val dtoM = FreightDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            origin = "Origin", customer = "Company", destiny = "Destiny", weight = 10.0, cargo = "Cargo",
            value = 100.0, breakDown = 10.0, loadingDate = Date(),
            dailyValue = 20.0, daily = 2, dailyTotalValue = 40.0, complement = emptyList(),
            isCommissionPaid = true, commissionPercentual = null
        )

        val isValidA = dtoA.validateFields()
        val isValidB = dtoB.validateFields()
        val isValidC = dtoC.validateFields()
        val isValidD = dtoD.validateFields()
        val isValidE = dtoE.validateFields()
        val isValidF = dtoF.validateFields()
        val isValidG = dtoG.validateFields()
        val isValidH = dtoH.validateFields()
        val isValidI = dtoI.validateFields()
        val isValidJ = dtoJ.validateFields()
        val isValidK = dtoK.validateFields()
        val isValidL = dtoL.validateFields()
        val isValidM = dtoM.validateFields()

        Assert.assertFalse(isValidA)
        Assert.assertFalse(isValidB)
        Assert.assertFalse(isValidC)
        Assert.assertFalse(isValidD)
        Assert.assertFalse(isValidE)
        Assert.assertFalse(isValidF)
        Assert.assertFalse(isValidG)
        Assert.assertFalse(isValidH)
        Assert.assertFalse(isValidI)
        Assert.assertFalse(isValidJ)
        Assert.assertFalse(isValidK)
        Assert.assertFalse(isValidL)
        Assert.assertFalse(isValidM)
    }

}