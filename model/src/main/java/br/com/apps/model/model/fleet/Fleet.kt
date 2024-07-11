package br.com.apps.model.model.fleet

import br.com.apps.model.exceptions.InvalidTypeException

abstract class Fleet(
    open val masterUid: String,
    open val id: String? = null,
    open val plate: String,
    open val fleetType: FleetType
)

enum class FleetType(description: String) {
    TRUCK("TRUCK"),
    THREE_AXIS("THREE_AXIS"),
    FOUR_AXIS("FOUR_AXIS"),
    ROAD_TRAIN_FRONT("ROAD_TRAIN_FRONT"),
    ROAD_TRAIN_REAR("ROAD_TRAIN_REAR"),
    DOLLY("DOLLY"),
    BI_TRUCK_FRONT("BI_TRUCK_FRONT"),
    BI_TRUCK_REAR("BI_TRUCK_REAR");

    companion object {

        fun getType(type: String): FleetType {
            return when (type) {
                "TRUCK" -> TRUCK
                "THREE_AXIS" -> THREE_AXIS
                "FOUR_AXIS" -> FOUR_AXIS
                "ROAD_TRAIN_FRONT" -> ROAD_TRAIN_FRONT
                "ROAD_TRAIN_REAR" -> ROAD_TRAIN_REAR
                "DOLLY" -> DOLLY
                "BI_TRUCK_FRONT" -> BI_TRUCK_FRONT
                "BI_TRUCK_REAR" -> BI_TRUCK_REAR
                else -> throw InvalidTypeException("Invalid Fleet type ($type)")
            }
        }

    }

}

