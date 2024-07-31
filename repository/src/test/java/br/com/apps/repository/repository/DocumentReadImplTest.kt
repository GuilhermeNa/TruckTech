package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.document.DocumentReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DocumentReadImplTest {

    private val repository = mockk<DocumentReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchDocumentById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the document list by id`() = runTest {
        coEvery {
            repository.fetchDocumentById(id = "1", flow = true)
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchDocumentListByTruckId()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the document list by truck id`() = runTest {
        coEvery {
            repository.fetchDocumentListByFleetId(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchDocumentListByTruckIdList()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the document list by truck id list`() = runTest {
        coEvery {
            repository.fetchDocumentListByFleetIdList(
                ids = listOf("1", "2"),
                flow = true
            )
        }.returns(MutableLiveData())
    }

}