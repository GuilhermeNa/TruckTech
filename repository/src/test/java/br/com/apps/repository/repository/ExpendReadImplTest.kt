package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.expend.ExpendReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ExpendReadImplTest {

    private val repository = mockk<ExpendReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchExpendListByDriverId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return expend list by driver id`() = runTest {
        coEvery {
            repository.fetchExpendListByDriverId(
                driverId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchExpendListByDriverIdsAndRefundableStatus()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return expend list by driver id list and refundable status`() = runTest {
        coEvery {
            repository.fetchExpendListByDriverIdsAndRefundableStatus(
                driverIdList = listOf("1", "2"),
                paidByEmployee = true,
                alreadyRefunded = true,
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchExpendListByDriverIdAndRefundableStatus()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return expend list by driver id and refundable status`() = runTest {
        coEvery {
            repository.fetchExpendListByDriverIdAndRefundableStatus(
                driverId = "1",
                paidByEmployee = true,
                alreadyRefunded = true,
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchExpendListByTravelId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return expend list by travel id`() = runTest {
        coEvery {
            repository.fetchExpendListByTravelId(
                travelId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchExpendListByTravelIds()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return expend list by travel id list`() = runTest {
        coEvery {
            repository.fetchExpendListByTravelIds(
                travelIdList = listOf("1", "2"),
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchExpendById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return expend by id`() = runTest {
        coEvery {
            repository.fetchExpendById(
                expendId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchExpendListByDriverIdAndIsNotRefundYet()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return expend by driver id and refund status`() = runTest {
        coEvery {
            repository.fetchExpendListByDriverIdAndIsNotRefundYet(
                driverId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

}