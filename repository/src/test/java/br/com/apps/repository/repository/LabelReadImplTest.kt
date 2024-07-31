package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.label.LabelReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LabelReadImplTest {

    private val repository = mockk<LabelReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchLabelListByMasterUid()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return label list by master UID`() = runTest {
        coEvery {
            repository.fetchLabelListByMasterUid(
                masterUid = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchLabelListByMasterUidAndType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return label list by master UID and type`() = runTest {
        coEvery {
            repository.fetchLabelListByMasterUidAndType(
                masterUid = "1",
                type = "type",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchLabelById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return label by ID`() = runTest {
        coEvery {
            repository.fetchLabelById(
                labelId = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchLabelListByMasterUidAndTypeAndOperational()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return label list by master UID, type, and operational status`() = runTest {
        coEvery {
            repository.fetchLabelListByMasterUidAndTypeAndOperational(
                masterUid = "1",
                type = "type",
                isOperational = true,
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchDefaultLabelList()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return default label list by type and operational status`() = runTest {
        coEvery {
            repository.fetchDefaultLabelList(
                type = "type",
                isOperational = true,
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchDefaultExpendLabelList()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return default expend label list by operational status`() = runTest {
        coEvery {
            repository.fetchDefaultExpendLabelList(
                isOperational = true,
                flow = true
            )
        }.returns(MutableLiveData())
    }

}
