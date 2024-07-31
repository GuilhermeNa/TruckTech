package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.fleet.FleetReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FleetReadImplTest {

    private val repository = mockk<FleetReadImpl>()


    //---------------------------------------------------------------------------------------------//
    // getTruckListByMasterUid()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return truck list by master UID`() = runTest {
        coEvery {
            repository.fetchTruckListByMasterUid(
                uid = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getTruckById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return truck by ID`() = runTest {
        coEvery {
            repository.fetchTruckById(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getTruckByDriverId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return truck by driver ID`() = runTest {
        coEvery {
            repository.fetchTruckByDriverId(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getTrailerListLinkedToTruckById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return trailer list linked to truck by ID`() = runTest {
        coEvery {
            repository.fetchTrailerListLinkedToTruckById(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

}
