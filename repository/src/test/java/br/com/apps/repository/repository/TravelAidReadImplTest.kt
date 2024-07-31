package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.travel_aid.TravelAidReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TravelAidReadImplTest {

    private val repository = mockk<TravelAidReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchTravelAidByDriverIdAndIsNotDiscountedYet()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return travel aid list by driver ID and is not discounted yet`() = runTest {
        coEvery {
            repository.fetchTravelAidByDriverIdAndIsNotDiscountedYet(
                employeeId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchTravelAidListByTravelId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return travel aid list by travel ID`() = runTest {
        coEvery {
            repository.fetchTravelAidListByTravelId(
                travelId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchTravelAidListByTravelIds()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return travel aid list by list of travel IDs`() = runTest {
        coEvery {
            repository.fetchTravelAidListByTravelIds(
                travelIdList = listOf("1", "2", "3"),
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchTravelAidListByDriverId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return travel aid list by driver ID`() = runTest {
        coEvery {
            repository.fetchTravelAidListByDriverId(
                driverId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

}