package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.request.RequestReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RequestReadImplTest {

    private val repository = mockk<RequestReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // getRequestListByDriverId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return request list by driver ID`() = runTest {
        coEvery {
            repository.fetchRequestListByDriverId(
                driverId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getItemListByRequests()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return item list by list of request IDs`() = runTest {
        coEvery {
            repository.fetchItemListByRequests(
                idList = listOf("1", "2"),
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getRequestById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return request by ID`() = runTest {
        coEvery {
            repository.fetchRequestById(
                requestId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getItemListByRequestId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return item list by request ID`() = runTest {
        coEvery {
            repository.fetchItemListByRequestId(
                requestId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getItemById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return item by request ID and item ID`() = runTest {
        coEvery {
            repository.fetchItemById(
                requestId = "1",
                itemId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }



}