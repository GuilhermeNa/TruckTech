package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.fine.FineReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FineReadImplTest {

    private val repository = mockk<FineReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchFineListByDriverId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return fine list by driver id`() = runTest {
        coEvery {
            repository.fetchFineListByDriverId(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchFineListByTruckId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return fine list by truck id`() = runTest {
        coEvery {
            repository.fetchFineListByFleetId(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchFineById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return fine list by id`() = runTest {
        coEvery {
            repository.fetchFineById(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

}