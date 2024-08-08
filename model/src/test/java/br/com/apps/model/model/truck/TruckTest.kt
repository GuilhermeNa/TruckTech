package br.com.apps.model.model.truck

import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.model.fleet.Trailer
import br.com.apps.model.model.fleet.Truck
import br.com.apps.model.test_cases.sampleTrailer
import br.com.apps.model.test_cases.sampleTruck
import br.com.apps.model.test_cases.sampleTruckDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
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
    // addTrailer()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add trailers to the list`() {
        val trailerA = sampleTrailer()
        val trailerB = sampleTrailer().copy(id = "trailerId2", plate = "ABCDEFG")
        val expected = listOf(trailerA, trailerB)

        truck.addTrailer(trailerA)
        truck.addTrailer(trailerB)

        assertEquals(expected, truck.trailers)
    }

    @Test
    fun `should throw DuplicatedItemsException when adding trailers with an id already existing`() {
        val trailerA = sampleTrailer()
        val trailerB = sampleTrailer().copy(plate = "ABCDEFG")

        assertThrows(DuplicatedItemsException::class.java) {
            truck.addTrailer(trailerA)
            truck.addTrailer(trailerB)
        }

    }

    @Test
    fun `should throw DuplicatedItemsException when adding trailers with an plate already existing`() {
        val trailerA = sampleTrailer()
        val trailerB = sampleTrailer().copy(id = "trailerId2")

        assertThrows(DuplicatedItemsException::class.java) {
            truck.addTrailer(trailerA)
            truck.addTrailer(trailerB)
        }
    }

    @Test
    fun `should not edit truck list when access trailer val`() {
        truck.trailers.toMutableList().add(sampleTrailer())

        assertEquals(0, truck.trailers.size)
    }

    //---------------------------------------------------------------------------------------------//
    // toDto()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return a dto representing this model`() {
        val expected = sampleTruckDto()

        val dto = truck.toDto()

        assertEquals(expected, dto)
    }

}