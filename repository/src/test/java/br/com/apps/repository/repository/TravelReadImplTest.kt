package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.travel.TravelReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TravelReadImplTest {

    private val repository = mockk<TravelReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // getTravelListByDriverIdAndIsFinished()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return travel list by driver ID and is finished`() = runTest {
        coEvery {
            repository.fetchTravelListByDriverIdAndIsFinished(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getTravelListByDriverId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return travel list by driver ID`() = runTest {
        coEvery {
            repository.fetchTravelListByDriverId(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getTravelById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return travel by ID`() = runTest {
        coEvery {
            repository.fetchTravelById(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }


}