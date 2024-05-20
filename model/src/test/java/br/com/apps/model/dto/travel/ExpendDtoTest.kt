package br.com.apps.model.dto.travel

import br.com.apps.model.model.label.Label
import org.junit.Assert
import org.junit.Test
import java.util.Date

class ExpendDtoTest {

    private val expend = ExpendDto(
        masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = "5", labelId = "6",
        company = "Company", date = Date(), description = "Description", value = 100.0, label = Label(),
        paidByEmployee = true, alreadyRefunded = true
    )

    @Test
    fun `should return true if there is no null field in non-null model fields`() {
        val isValid = expend.validateFields()
        Assert.assertTrue(isValid)
    }

    @Test
    fun `should return false if there is any null field in non-null model fields`() {
        val dtoA = ExpendDto(
            masterUid = null, id = "1", truckId = "2", driverId = "3", travelId = "4", labelId = "5",
            company = "Company", date = Date(), description = "Description", value = 100.0, label = Label(),
            paidByEmployee = true, alreadyRefunded = true
        )
        val dtoB = ExpendDto(
            masterUid = "1", id = "2", truckId = null, driverId = "3", travelId = "4", labelId = "5",
            company = "Company", date = Date(), description = "Description", value = 100.0, label = Label(),
            paidByEmployee = true, alreadyRefunded = true
        )
        val dtoC = ExpendDto(
            masterUid = "1", id = "2", truckId = "3", driverId = null, travelId = "4", labelId = "5",
            company = "Company", date = Date(), description = "Description", value = 100.0, label = Label(),
            paidByEmployee = true, alreadyRefunded = true
        )
        val dtoD = ExpendDto(
            masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = null, labelId = "5",
            company = "Company", date = Date(), description = "Description", value = 100.0, label = Label(),
            paidByEmployee = true, alreadyRefunded = true
        )
        val dtoE = ExpendDto(
            masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = "5", labelId = null,
            company = "Company", date = Date(), description = "Description", value = 100.0, label = Label(),
            paidByEmployee = true, alreadyRefunded = true
        )
        val dtoF = ExpendDto(
            masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = "5", labelId = "6",
            company = null, date = Date(), description = "Description", value = 100.0, label = Label(),
            paidByEmployee = true, alreadyRefunded = true
        )
        val dtoG = ExpendDto(
            masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = "5", labelId = "6",
            company = "Company", date = null, description = "Description", value = 100.0, label = Label(),
            paidByEmployee = true, alreadyRefunded = true
        )
        val dtoH = ExpendDto(
            masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = "5", labelId = "6",
            company = "Company", date = Date(), description = "Description", value = null, label = Label(),
            paidByEmployee = true, alreadyRefunded = true
        )
        val dtoI = ExpendDto(
            masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = "5", labelId = "6",
            company = "Company", date = Date(), description = "Description", value = 100.0, label = Label(),
            paidByEmployee = null, alreadyRefunded = true
        )
        val dtoJ = ExpendDto(
            masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = "5", labelId = "6",
            company = "Company", date = Date(), description = "Description", value = 100.0, label = Label(),
            paidByEmployee = true, alreadyRefunded = null
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
    }

}