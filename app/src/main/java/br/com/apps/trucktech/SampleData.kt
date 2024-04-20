package br.com.apps.trucktech

import br.com.apps.model.model.request.payment_payroll.PaymentPayroll
import br.com.apps.model.model.request.payment_payroll.PaymentPayrollType
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.trucktech.model.Performance
import br.com.apps.trucktech.model.Truck
import br.com.apps.trucktech.model.costs.DefaultCost
import br.com.apps.trucktech.model.employee.AuthorizationLevelType
import br.com.apps.trucktech.model.employee.Driver
import br.com.apps.trucktech.model.events.TimeLineEvent
import br.com.apps.trucktech.model.payroll.PayrollAdvance
import br.com.apps.trucktech.model.payroll.PayrollLoan
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

val sampleDriver = Driver(
    id = "idOne",
    name = "José Anacrizio",
    userName = "JoseAnacrizio",
    password = "passwordOne",
    authorizationLevel = AuthorizationLevelType.OPERATIONAL
)

val sampleTruck = Truck(
    id = 1L,
    name = "RIF0A17"
)

val sampleFreightList = listOf(
    Freight(
        id = "1",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        breakDown = BigDecimal("200.00"),
        loadingDate = LocalDateTime.now()
    ),
    Freight(
        id = "2",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("500.09"),
        loadingDate = LocalDateTime.parse("2024-01-25T12:30:45", formatter)
    ),
    Freight(
        id = "3",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        loadingDate = LocalDateTime.parse("2024-01-05T12:30:45", formatter)
    ),
    Freight(
        id = "4",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        loadingDate = LocalDateTime.parse("2023-12-25T12:30:45", formatter)
    ),
    Freight(
        id = "5",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        loadingDate = LocalDateTime.parse("2023-12-20T12:30:45", formatter)
    ),
    Freight(
        id = "6",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        loadingDate = LocalDateTime.parse("2023-12-12T12:30:45", formatter)
    ),
    Freight(
        id = "7",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        loadingDate = LocalDateTime.parse("2023-12-18T12:30:45", formatter)
    ),
    Freight(
        id = "8",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        loadingDate = LocalDateTime.parse("2023-12-18T12:30:45", formatter)
    ),
    Freight(
        id = "9",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        loadingDate = LocalDateTime.parse("2023-12-18T12:30:45", formatter)
    ),
    Freight(
        id = "10",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        loadingDate = LocalDateTime.parse("2023-12-18T12:30:45", formatter)
    )
)

val sampleCostsList = listOf(
    DefaultCost(
        id = "1",
        date = LocalDateTime.now(),
        company = "Manutenção",
        description = "Troca de Pneus",
        value = BigDecimal("70.0"),
        label = 1L
    ),
    DefaultCost(
        id = "2",
        date = LocalDateTime.now(),
        company = "Manutenção",
        description = "Troca de Pneus",
        value = BigDecimal("70.0"),
        label = 4
    ),
    DefaultCost(
        id = "3",
        date = LocalDateTime.parse("2024-01-02T12:30:45", formatter),
        company = "Chapa",
        description = "Descarga de caminhão",
        value = BigDecimal("70.0"),
        label = 2
    ),
    DefaultCost(
        id = "4",
        date = LocalDateTime.parse("2024-01-05T12:30:45", formatter),
        company = "Manutenção",
        description = "Troca de valvula",
        value = BigDecimal("1200.0"),
        label = 1
    ),
    DefaultCost(
        id = "5",
        date = LocalDateTime.parse("2024-01-29T12:30:45", formatter),
        company = "Peças",
        description = "Compra de peças",
        value = BigDecimal("800.0"),
        label = 1
    ),
    DefaultCost(
        id = "6",
        date = LocalDateTime.parse("2024-01-29T12:30:45", formatter),
        company = "Peças",
        description = "Compra de peças",
        value = BigDecimal("800.0"),
        label = 2
    )
)

/*val sampleBanksList = listOf(
    BankAccount(
        bankName = "Bradesco",
        driverId = 1,
        branch = 1234,
        number = 12,
        pix = "123123123",
        image = "https://scontent.fgyn2-1.fna.fbcdn.net/v/t39.30808-6/312113371_6657354517630618_5061336853734319396_n.jpg?_nc_cat=101&ccb=1-7&_nc_sid=9c7eae&_nc_ohc=Q3xwYkju4GwAX93rnUI&_nc_ht=scontent.fgyn2-1.fna&oh=00_AfBVGxVuem9avxNb35FaHKfyngSkZWa9pZ1Dfq7JyX2fvw&oe=65D2B819",
        isMainAccount = false
    ),
    BankAccount(
        bankName = "Inter",
        driverId = 1,
        branch = 1234,
        number = 12,
        pix = "123123123",
        image = "https://noticiasconcursos.com.br/wp-content/uploads/2022/10/noticiasconcursos.com.br-banco-inter-abre-vagas-de-emprego-pelo-brasil-veja-os-cargos-banco-inter-750x430.jpg",
        isMainAccount = true
    )
)*/

val sampleTravelsList = listOf(
    Travel(
        id = "1L",
        truckId = "1L",
        driverId = "1L",
        initialDate = LocalDateTime.now(),
        finalDate = null,
        isFinished = false
    ),
    Travel(
        id = "2L",
        truckId = "1L",
        driverId = "1L",
        initialDate = LocalDateTime.parse("2024-06-18T12:30:45", formatter),
        finalDate = LocalDateTime.parse("2024-06-29T12:30:45", formatter),
        isFinished = true
    )

)

val samplePerformanceList = listOf(
    Performance(
        title = "Média",
        meta = "2.50",
        hit = "2.40",
        percent = "96%",
        progressBar = 96
    ),
    Performance(
        title = "Resultado",
        meta = "50%",
        hit = "38%",
        percent = "76%",
        progressBar = 76
    )

)

val sampleEventsList = listOf(
    TimeLineEvent(
        id = 1,
        truckId = 1L,
        driverId = 1L,
        title = "Contratado",
        date = LocalDateTime.parse("2023-01-18T12:30:45", formatter),
        description = "Você foi contratado nesta data"
    ),
    TimeLineEvent(
        id = 1,
        truckId = 1L,
        driverId = 1L,
        title = "Recebe caminhão",
        date = LocalDateTime.parse("2023-01-20T12:30:45", formatter),
        description = "O caminhão de placa RIF6A07 foi designado para você"
    ),
    TimeLineEvent(
        id = 1,
        truckId = 1L,
        driverId = 1L,
        title = "Devolve caminhão",
        date = LocalDateTime.parse("2024-01-18T12:30:45", formatter),
        description = "O caminhão de placa RIF6A07 foi devolvido por você"
    ),
    TimeLineEvent(
        id = 1,
        truckId = 1L,
        driverId = 1L,
        title = "Férias",
        date = LocalDateTime.parse("2024-01-19T12:30:45", formatter),
        description = "Você saiu de férias"
    )
)

val samplePaymentsList = listOf(
    PaymentPayroll(
        id = 1L,
        value = BigDecimal(1580.20),
        type = PaymentPayrollType.COMMISSION,
        date = LocalDateTime.now()
    ),
    PaymentPayroll(
        id = 2L,
        value = BigDecimal(1060.07),
        type = PaymentPayrollType.COMMISSION,
        date = LocalDateTime.now()
    ),
    PaymentPayroll(
        id = 3L,
        value = BigDecimal(980.59),
        type = PaymentPayrollType.COMMISSION,
        date = LocalDateTime.now()
    )
)

val sampleFreightForCommission = listOf(
    Freight(
        id = "1",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        breakDown = BigDecimal("200.00"),
        loadingDate = LocalDateTime.now()
    ),
    Freight(
        id = "2",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("500.09"),
        loadingDate = LocalDateTime.parse("2024-01-25T12:30:45", formatter)
    ),
    Freight(
        id = "3",
        company = "Rápido 900",
        origin = "Goiânia",
        destiny = "São paulo",
        cargo = "Amianto",
        value = BigDecimal("1800.09"),
        loadingDate = LocalDateTime.parse("2024-01-05T12:30:45", formatter)
    )
)

val sampleLoanList = listOf(
    PayrollLoan(
        id = 1,
        date = LocalDateTime.now(),
        value = BigDecimal("3000.00"),
        installmentsNumber = 3,
        installmentsValue = BigDecimal("1000.00"),
        isPaid = false
    )
)

val sampleAdvancesList = listOf(
    PayrollAdvance(
        id = 1,
        date = LocalDateTime.now(),
        value = BigDecimal("800.00"),
        isPaid = false
    ),
    PayrollAdvance(
        id = 2,
        date = LocalDateTime.now(),
        value = BigDecimal("1000.00"),
        isPaid = false
    )
)