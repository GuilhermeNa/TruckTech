package br.com.apps.trucktech

import br.com.apps.model.model.travel.Travel
import br.com.apps.trucktech.model.employee.AuthorizationLevelType
import br.com.apps.trucktech.model.employee.Driver
import br.com.apps.trucktech.model.events.TimeLineEvent
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

