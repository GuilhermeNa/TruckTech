package br.com.apps.trucktech.model.events

import java.time.LocalDateTime

data class TimeLineEvent(

    val id: Long,
    val truckId: Long,
    val driverId: Long,

    val title: String,
    val date: LocalDateTime,
    val description: String,

    val eventType: EventType? = null,

    ) {



}

enum class EventType {
    HIRING,
    RESIGNATION,
    GO_ON_VACATION,
    TAKE_OVER_TRUCK,
    LEAVE_TRUCK
}