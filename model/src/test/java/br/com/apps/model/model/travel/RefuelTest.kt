package br.com.apps.model.model.travel

import br.com.apps.model.model.travel.Refuel
import java.math.BigDecimal
import java.time.LocalDateTime

class RefuelTest {

    private val refuel = Refuel(
        masterUid = "1",
        id = "2",
        truckId = "3",
        driverId = "4",
        travelId = "5",
        costId = "6",

        date = LocalDateTime.of(2022, 1, 1, 12, 0),
        station = "Posto",
        odometerMeasure = BigDecimal(1000),
        valuePerLiter = BigDecimal(5),
        amountLiters = BigDecimal(500),
        totalValue = BigDecimal(2500),
        isCompleteRefuel = false
    )

}