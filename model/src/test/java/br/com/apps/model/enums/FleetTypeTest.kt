package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.fleet.FleetType
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class FleetTypeTest {

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return type TRUCK when param is string TRUCK`() {
        val type = FleetType.getType("TRUCK")
        assertEquals(FleetType.TRUCK, type)
    }

    @Test
    fun `should return type THREE_AXIS when param is string THREE_AXIS`() {
        val type = FleetType.getType("THREE_AXIS")
        assertEquals(FleetType.THREE_AXIS, type)
    }

    @Test
    fun `should return type FOUR_AXIS when param is string FOUR_AXIS`() {
        val type = FleetType.getType("FOUR_AXIS")
        assertEquals(FleetType.FOUR_AXIS, type)
    }

    @Test
    fun `should return type ROAD_TRAIN_FRONT when param is string ROAD_TRAIN_FRONT`() {
        val type = FleetType.getType("ROAD_TRAIN_FRONT")
        assertEquals(FleetType.ROAD_TRAIN_FRONT, type)
    }

    @Test
    fun `should return type ROAD_TRAIN_REAR when param is string ROAD_TRAIN_REAR`() {
        val type = FleetType.getType("ROAD_TRAIN_REAR")
        assertEquals(FleetType.ROAD_TRAIN_REAR, type)
    }

    @Test
    fun `should return type DOLLY when param is string DOLLY`() {
        val type = FleetType.getType("DOLLY")
        assertEquals(FleetType.DOLLY, type)
    }

    @Test
    fun `should return type BI_TRUCK_FRONT when param is string BI_TRUCK_FRONT`() {
        val type = FleetType.getType("BI_TRUCK_FRONT")
        assertEquals(FleetType.BI_TRUCK_FRONT, type)
    }

    @Test
    fun `should return type BI_TRUCK_REAR when param is string BI_TRUCK_REAR`() {
        val type = FleetType.getType("BI_TRUCK_REAR")
        assertEquals(FleetType.BI_TRUCK_REAR, type)
    }

    @Test
    fun `should throw InvalidTypeException when type is not registered`() {
        Assert.assertThrows(InvalidTypeException::class.java) {
            FleetType.getType("")
        }
    }

}