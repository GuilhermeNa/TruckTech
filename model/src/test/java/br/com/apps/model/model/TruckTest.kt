package br.com.apps.model.model

import br.com.apps.model.model.fleet.FleetType
import br.com.apps.model.model.fleet.Trailer
import br.com.apps.model.model.fleet.Truck
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class TruckTest {

    private lateinit var truck: Truck

    @Before
    fun setup() {
        truck = Truck(
            masterUid = "1",
            id = "2",
            plate = "ABC1234",
            fleetType = FleetType.TRUCK,
            driverId = "3",
            averageAim = 10.0,
            performanceAim = 8.5,
            color = "Red",
            commissionPercentual = BigDecimal("5.0"),
            trailerList = listOf(
                Trailer("1", "3", "XYZ5678", FleetType.FOUR_AXIS, truckId = "2")
            )
        )
    }

    //---------------------------------------------------------------------------------------------//
    // getFleetIds()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the list of fleet id when there is one or more trailers`() {
        val ids = listOf("2", "3")
        Assert.assertEquals(ids, truck.getFleetIds())
    }

    @Test
    fun `should return the list just with truckId when there is no trailers`() {
        truck.trailerList = emptyList()
        val ids = listOf("2")
        Assert.assertEquals(ids, truck.getFleetIds())
    }

    @Test
    fun `should return the list just with truckId when trailer list is null`() {
        truck.trailerList = null
        val ids = listOf("2")
        Assert.assertEquals(ids, truck.getFleetIds())
    }

    @Test
    fun `should return the list just with truckId when trailer id is null`() {
        truck.trailerList!![0].id = null
        val ids = listOf("2")
        Assert.assertEquals(ids, truck.getFleetIds())
    }


}