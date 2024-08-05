package br.com.apps.model.model

import br.com.apps.model.model.fleet.Trailer
import br.com.apps.model.model.fleet.Truck
import br.com.apps.model.test_cases.sampleTrailer
import br.com.apps.model.test_cases.sampleTruck
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TruckTest {

    private lateinit var truck: Truck

    @Before
    fun setup() {
        truck = sampleTruck()
    }

    //---------------------------------------------------------------------------------------------//
    // getFleetIds()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the list of fleet id when there is one or more trailers`() {
        truck.addTrailer(sampleTrailer())
        val ids = listOf("truckId1", "trailerId1")
        assertEquals(ids, truck.getFleetIds())
    }

    @Test
    fun `should return the list just with truckId when there is no trailers`() {
        val ids = listOf("truckId1")
        assertEquals(ids, truck.getFleetIds())
    }

    //---------------------------------------------------------------------------------------------//
    // clearTrailers()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun`should clear the trailers`() {
        truck.addTrailer(sampleTrailer())
        truck.clearTrailers()
        assertEquals(mutableListOf<Trailer>(), truck.trailers)
    }

    //---------------------------------------------------------------------------------------------//
    // toDto()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return a dto representing this model`() {
        val dto = truck.toDto()

        assertEquals(truck.masterUid, dto.masterUid)
        assertEquals(truck.id, dto.id)
        assertEquals(truck.employeeId, dto.driverId)
        assertEquals(truck.averageAim, dto.averageAim)
        assertEquals(truck.performanceAim, dto.performanceAim)
        assertEquals(truck.plate, dto.plate)
        assertEquals(truck.color, dto.color)
        assertEquals(truck.commissionPercentual.toDouble(), dto.commissionPercentual)
        assertEquals(truck.fleetType.name, dto.fleetType)
    }

}