package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.outlay.OutlayReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OutlayReadImplTest {

    private val repository = mockk<OutlayReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchExpendListByDriverId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return expend list by driver id`() = runTest {
        coEvery {
            repository.fetchOutlayListByDriverId(
                id = "1",
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
            repository.fetchOutlayListByDriverIdsAndRefundableStatus(
                ids = listOf("1", "2"),
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
            repository.fetchOutlayListByDriverIdAndRefundableStatus(
                id = "1",
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
            repository.fetchOutlayListByTravelId(
                id = "1",
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
            repository.fetchOutlayListByTravelIds(
                ids = listOf("1", "2"),
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
            repository.fetchOutlayById(
                id = "1",
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
            repository.fetchOutlayListByDriverIdAndIsNotRefundYet(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

}