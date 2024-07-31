package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.repository.employee.EmployeeReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class EmployeeReadImplTest {

    private val repository = mockk<EmployeeReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the employee by id`() = runTest {
        coEvery {
            repository.fetchById(
                id ="1",
                type = EmployeeType.DRIVER,
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getEmployeeBankAccounts()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the employee bank account list by id and employeeType`() = runTest {
        coEvery {
            repository.getEmployeeBankAccounts(
                id ="1",
                type = EmployeeType.DRIVER,
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getBankAccountById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the bank account by id`() = runTest {
        coEvery {
            repository.getBankAccountById(
                employeeId ="1",
                bankId = "2",
                type = EmployeeType.DRIVER,
                flow = true
            )
        }.returns(MutableLiveData())
    }

}