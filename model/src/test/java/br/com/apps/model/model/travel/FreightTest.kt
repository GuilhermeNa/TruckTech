package br.com.apps.model.model.travel

import br.com.apps.model.model.travel.Freight
import java.math.BigDecimal
import java.time.LocalDateTime

class FreightTest {

    private val freight = Freight(
        masterUid = "1",
        id = "2",
        truckId = "3",
        driverId = "4",
        travelId = "5",

        origin = "Origem",
        company = "Companhia",
        destiny = "Destino",
        weight = BigDecimal(30000),
        cargo = "Carga",
        breakDown = BigDecimal(100),
        value = BigDecimal(12000),
        loadingDate = LocalDateTime.of(2022, 1, 1, 12, 0),

        dailyValue = BigDecimal(10),
        daily = 3,
        dailyTotalValue = BigDecimal(30),

        isCommissionPaid = true,
        commissionPercentual = BigDecimal(11),
    )

}