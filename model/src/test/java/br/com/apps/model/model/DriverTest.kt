package br.com.apps.model.model

import br.com.apps.model.enums.WorkRole
import br.com.apps.model.model.employee.Driver
import org.junit.Before

class DriverTest {

    private lateinit var driver: Driver

    @Before
    fun setup() {
        driver = Driver(
            masterUid = "1",
            id = "2",
            truckId = "3",
            name = " João",
            type = WorkRole.DRIVER
        )
    }




}