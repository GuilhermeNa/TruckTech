package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.freight.FreightReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FreightReadImplTest {

    private val repository = mockk<FreightReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchFreightListByDriverId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return freight list by driver id`() = runTest {
        coEvery {
            repository.fetchFreightListByDriverId(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchFreightListByDriverIdsAndPaymentStatus()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return freight list by driver ids and payment status`() = runTest {
        coEvery {
            repository.fetchFreightListByDriverIdsAndPaymentStatus(
                ids = listOf("1", "2"),
                isPaid = true,
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchFreightListByDriverIdAndPaymentStatus()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return freight list by driver id and payment status`() = runTest {
        coEvery {
            repository.fetchFreightListByDriverIdAndPaymentStatus(
                id = "1",
                isPaid = true,
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchFreightListByTravelId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return freight list by travel id`() = runTest {
        coEvery {
            repository.fetchFreightListByTravelId(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchFreightListByTravelIds()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return freight list by travel ids`() = runTest {
        coEvery {
            repository.fetchFreightListByTravelIds(
                ids = listOf("1", "2"),
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchFreightById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return freight by id`() = runTest {
        coEvery {
            repository.fetchFreightById(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchFreightListByDriverIdAndIsNotPaidYet()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return freight list by driver id and is not paid yet`() = runTest {
        coEvery {
            repository.fetchFreightListByDriverIdAndIsNotPaidYet(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

}