package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.refuel.RefuelReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RefuelReadImplTest {

    private val repository = mockk<RefuelReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchRefuelListByDriverId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return refuel list by driver ID`() = runTest {
        coEvery {
            repository.fetchRefuelListByDriverId(
                driverId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchRefuelListByTravelId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return refuel list by travel ID`() = runTest {
        coEvery {
            repository.fetchRefuelListByTravelId(
                travelId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchRefuelListByTravelIds()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return refuel list by list of travel IDs`() = runTest {
        coEvery {
            repository.fetchRefuelListByTravelIds(
                idList = listOf("1", "2"),
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchRefuelById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return refuel by ID`() = runTest {
        coEvery {
            repository.fetchRefuelById(
                refuelId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }


}