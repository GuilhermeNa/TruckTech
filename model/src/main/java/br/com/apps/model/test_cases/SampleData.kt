package br.com.apps.model.test_cases

import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.finance.TransactionDto
import br.com.apps.model.dto.finance.payable.EmployeePayableDto
import br.com.apps.model.dto.finance.receivable.EmployeeReceivableDto
import br.com.apps.model.dto.fleet.TruckDto
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.dto.travel.OutlayDto
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.enums.AdvanceType
import br.com.apps.model.enums.EmployeePayableTicket
import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.enums.FleetCategory
import br.com.apps.model.enums.LabelCategory
import br.com.apps.model.enums.PixType
import br.com.apps.model.enums.TransactionType
import br.com.apps.model.model.Customer
import br.com.apps.model.model.TruckDocument
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.finance.Transaction
import br.com.apps.model.model.finance.payable.EmployeePayable
import br.com.apps.model.model.finance.receivable.EmployeeReceivable
import br.com.apps.model.model.fleet.Trailer
import br.com.apps.model.model.fleet.Truck
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.time.LocalDateTime

fun sampleBank(): Bank = Bank(
    id = "bankId1",
    name = "Name1",
    code = 1,
    urlImage = "urlImage"
)

fun sampleBankAccount(): BankAccount = BankAccount(
    masterUid = "masterUid1",
    id = "bankAccountId1",
    employeeId = "employeeId1",
    bankId = "bankId1",
    insertionDate = LocalDateTime.of(2024, 1, 1, 0, 0, 0),
    branch = 123,
    accNumber = 123456,
    mainAccount = true,
    pixType = PixType.PHONE,
    pix = "(xx) x xxxx xxxx",
    bank = sampleBank()
)

fun sampleBankAccountDto(): BankAccountDto = BankAccountDto(
    masterUid = "masterUid1",
    id = "bankAccountId1",
    employeeId = "employeeId1",
    bankId = "bankId1",
    insertionDate = LocalDateTime.of(2024, 1, 1, 0, 0, 0).toDate(),
    branch = 123,
    accNumber = 123456,
    mainAccount = true,
    pixType = PixType.PHONE.toString(),
    pix = "(xx) x xxxx xxxx",
)

fun sampleFreight(): Freight = Freight(
    masterUid = "masterUid1",
    id = "freightId1",
    truckId = "truckId1",
    travelId = "travelId1",
    employeeId = "employeeId1",
    customerId = "customerId1",
    cargo = "Cargo1",
    origin = "Origin1",
    destiny = "Destiny1",
    value = BigDecimal("1000.0"),
    weight = BigDecimal("10000.0"),
    loadingDate = LocalDateTime.of(2024, 1, 1, 0, 0, 0),
    commissionPercentual = BigDecimal("10.0"),
    isValid = false,
    customer = sampleCustomer()
)

fun sampleFreightDto(): FreightDto = FreightDto(
    masterUid = "masterUid1",
    id = "freightDto1",
    truckId = "truckId1",
    travelId = "travelId1",
    employeeId = "employeeId1",
    customerId = "customerId1",
    cargo = "Cargo1",
    origin = "Origin1",
    destiny = "Destiny1",
    value = BigDecimal("1000.0").toDouble(),
    weight = BigDecimal("10000.0").toDouble(),
    loadingDate = LocalDateTime.of(2024, 1, 1, 0, 0, 0).toDate(),
    commissionPercentual = BigDecimal("10.0").toDouble(),
    isValid = false
)

fun sampleCustomer(): Customer = Customer(
    masterUid = "masterUid1",
    id = "customerId1",
    cnpj = "xxx.xxx.xxx/xxxx-xx",
    name = "Name1"
)

fun sampleExpend(): Outlay = Outlay(
    masterUid = "masterUid1",
    id = "expendId1",
    truckId = "truckId1",
    employeeId = "employeeId1",
    travelId = "travelId1",
    labelId = "labelId1",
    company = "Company1",
    date = LocalDateTime.of(2024, 1, 1, 0, 0, 0),
    description = "Description1",
    value = BigDecimal("100.0"),
    isPaidByEmployee = false,
    isValid = true,
    label = sampleCostLabel()
)

fun sampleCostLabel(): Label = Label(
    masterUid = "masterUid1",
    id = "labelId1",
    name = "Name1",
    urlIcon = "UrlIcon1",
    color = 123,
    type = LabelCategory.COST,
    isDefaultLabel = false,
    isOperational = true
)

fun sampleTruckDocument(): TruckDocument = TruckDocument(
    masterUid = "masterUid1",
    id = "truckDocumentId1",
    fleetId = "fleetId1",
    labelId = "labelId2",
    urlImage = "UrlImage1",
    expeditionDate = LocalDateTime.of(2024, 1, 1, 0, 0, 0),
    expirationDate = LocalDateTime.of(2025, 1, 1, 0, 0, 0),
    label = sampleDocumentLabel()
)

fun sampleDocumentLabel(): Label = Label(
    masterUid = "masterUid1",
    id = "labelId2",
    name = "Name2",
    urlIcon = "UrlIcon2",
    color = 123,
    type = LabelCategory.DOCUMENT,
    isDefaultLabel = false,
    isOperational = true
)

fun sampleTruck(): Truck = Truck(
    masterUid = "masterUid1",
    id = "truckId1",
    plate = "ABC1234",
    fleetType = FleetCategory.TRUCK,
    employeeId = "driverId1",
    averageAim = 2.5,
    performanceAim = 50.0,
    color = "Color1",
    commissionPercentual = BigDecimal("10.0")
)

fun sampleTruckDto(): TruckDto = TruckDto(
    masterUid = "masterUid1",
    id = "truckId1",
    plate = "ABC1234",
    fleetType = FleetCategory.TRUCK.toString(),
    driverId = "driverId1",
    averageAim = 2.5,
    performanceAim = 50.0,
    color = "Color1",
    commissionPercentual = BigDecimal("10.0").toDouble()
)

fun sampleTrailer(): Trailer = Trailer(
    "masterUid1",
    "trailerId1",
    "XYZ5678",
    FleetCategory.FOUR_AXIS,
    truckId = "truckId1"
)

fun sampleEmployeeReceivable(): EmployeeReceivable = EmployeeReceivable(
    masterUid = "masterUid1",
    id = "employeeReceivableId1",
    parentId = "parentId1",
    employeeId = "employeeId1",
    installments = 10,
    value = BigDecimal("1000.00"),
    type = EmployeeReceivableTicket.TRAVEL_AID,
    generationDate = LocalDateTime.of(2024, 1, 15, 10, 30),
    _isReceived = false
)

fun sampleEmployeeReceivableDto(): EmployeeReceivableDto = EmployeeReceivableDto(
    masterUid = "masterUid1",
    id = "employeeReceivableId1",
    parentId = "parentId1",
    employeeId = "employeeId1",
    installments = 10,
    value = 1000.00,
    type = EmployeeReceivableTicket.TRAVEL_AID.name,
    generationDate = LocalDateTime.of(2024, 1, 15, 10, 30).toDate(),
    isReceived = false
)

fun sampleEmployeePayable(): EmployeePayable = EmployeePayable(
    masterUid = "masterUid1",
    id = "employeePayableId1",
    employeeId = "employeeId1",
    parentId = "parentId2",
    installments = 10,
    value = BigDecimal("500.00"),
    generationDate = LocalDateTime.of(2024, 2, 20, 14, 45),
    type = EmployeePayableTicket.COMMISSION,
    _isPaid = false
)

fun sampleEmployeePayableDto(): EmployeePayableDto = EmployeePayableDto(
    masterUid = "masterUid1",
    id = "employeePayableId1",
    parentId = "parentId2",
    employeeId = "employeeId1",
    value = 500.00,
    installments = 10,
    generationDate = LocalDateTime.of(2024, 2, 20, 14, 45).toDate(),
    type = EmployeePayableTicket.COMMISSION.name,
    isPaid = false
)

fun sampleOutlay(): Outlay = Outlay(
    masterUid = "masterUid1",
    id = "outlayId1",
    truckId = "truckId1",
    employeeId = "employeeId1",
    travelId = "travelId1",
    labelId = "labelId1",
    company = "Company1",
    date = LocalDateTime.of(2024, 2, 20, 14, 45),
    description = "Description",
    value = BigDecimal("100.00"),
    label = null,
    isPaidByEmployee = true,
    isValid = false
)

fun sampleOutlayDto(): OutlayDto = OutlayDto(
    masterUid = "masterUid1",
    id = "outlayId1",
    truckId = "truckId1",
    employeeId = "employeeId1",
    travelId = "travelId1",
    labelId = "labelId1",
    company = "Company1",
    date = LocalDateTime.of(2024, 2, 20, 14, 45).toDate(),
    description = "Description",
    value = 100.00,
    isPaidByEmployee = true,
    isValid = false
)

fun sampleRefuelDto(): RefuelDto = RefuelDto(
    masterUid = "masterUid1",
    id = "refuelId1",
    truckId = "truckId1",
    travelId = "travelId1",
    date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
    station = "Station",
    odometerMeasure = 10.0,
    valuePerLiter = 4.5,
    amountLiters = 2.0,
    totalValue = 9.0,
    isCompleteRefuel = true,
    isValid = false
)

fun sampleTransaction(): Transaction = Transaction(
    masterUid = "masterUid1",
    id = "transactionId1",
    parentKey = "parentKey1",
    dueDate = LocalDateTime.of(2024, 2, 20, 14, 45),
    number = 1,
    value = BigDecimal("100.00"),
    type = TransactionType.PAYABLE,
    _isPaid = false
)

fun sampleTransactionDto(): TransactionDto = TransactionDto(
    masterUid = "masterUid1",
    id = "transactionId1",
    parentId = "parentKey1",
    dueDate = LocalDateTime.of(2024, 2, 20, 14, 45).toDate(),
    number = 1,
    value = 100.00,
    type = TransactionType.PAYABLE.name,
    isPaid = false
)

fun sampleAdvance(): Advance = Advance(
    masterUid = "masterUid1",
    id = "advanceId1",
    travelId = "travelId1",
    employeeId = "employeeId1",
    date = LocalDateTime.of(2024, 1, 1, 0, 0),
    value = BigDecimal("100.00"),
    isPaid = false,
    isApproved = true,
    type = AdvanceType.COMMISSION
)

fun sampleLoan(): Loan = Loan(
    masterUid = "masterUid1",
    id = "loanId1",
    employeeId = "employeeId1",
    date = LocalDateTime.of(2024, 1, 1, 0, 0),
    value = BigDecimal("500.00"),
    installments = 5,
    installmentsAlreadyPaid = 1,
    isPaid = false
)

fun sampleTravelAid(): TravelAid = TravelAid(
    masterUid = "masterUid1",
    id = "travelAid1",
    employeeId = "employeeId1",
    travelId = "travelId1",
    date = LocalDateTime.of(2024, 1, 1, 0, 0),
    value = BigDecimal("100.00"),
    isValid = false
)