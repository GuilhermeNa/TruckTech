package br.com.apps.model.factory

import br.com.apps.model.dto.finance.TransactionDto
import br.com.apps.model.enums.EmployeePayableTicket
import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.enums.TransactionType
import br.com.apps.model.test_cases.sampleAdvance
import br.com.apps.model.test_cases.sampleEmployeePayableDto
import br.com.apps.model.test_cases.sampleEmployeeReceivableDto
import br.com.apps.model.test_cases.sampleFreight
import br.com.apps.model.test_cases.sampleLoan
import br.com.apps.model.test_cases.sampleOutlay
import br.com.apps.model.test_cases.sampleTravelAid
import br.com.apps.model.util.toDate
import io.mockk.spyk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class FinancialRecordFactoryTest {

    private lateinit var factory: FinancialRecordFactory

    @Before
    fun setup() {
        factory = spyk()
    }

    //---------------------------------------------------------------------------------------------//
    // OUTLAY
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should create a EmployeePayableDto from an outlay`() {
        val expectedPayable = sampleEmployeePayableDto()
            .copy(
                id = null,
                parentId = "outlayId1",
                installments = 1,
                value = 100.00,
                generationDate = LocalDateTime.of(2024, 2, 20, 14, 45).toDate(),
                type = EmployeePayableTicket.OUTLAY.name,
                isPaid = false
            )

        val outlay = sampleOutlay()

        val pair = factory.create(
            FinancialRecordsParams(
                data = outlay,
                installments = 1,
                firstDueDate = LocalDateTime.now().plusDays(10)
            )
        )

        val first = pair.first

        assertEquals(expectedPayable, first)
    }

    @Test
    fun `should create a list of TransactionsDto from an outlay with one item`() {
        val expectedList = listOf(
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 2, 20, 14, 45).plusDays(10).toDate(),
                number = 1,
                value = 100.00,
                type = TransactionType.PAYABLE.name,
                isPaid = false
            )
        )

        val outlay = sampleOutlay()
        val pair = factory.create(
            FinancialRecordsParams(
                data = outlay,
                installments = 1,
                firstDueDate = LocalDateTime.of(2024, 2, 20, 14, 45).plusDays(10)
            )
        )

        val second = pair.second

        assertEquals(expectedList, second)

    }

    @Test
    fun `should create a list with one TransactionsDto from an outlay with multiple items`() {
        val expectedList = listOf(
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0, 0).plusDays(10).toDate(),
                number = 1,
                value = 100.00,
                type = TransactionType.PAYABLE.name,
                isPaid = false
            )
        )

        val outlay = sampleOutlay()
        val pair = factory.create(
            FinancialRecordsParams(
                data = outlay,
                installments = 3,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val second = pair.second

        assertEquals(expectedList, second)

    }

    //---------------------------------------------------------------------------------------------//
    // FREIGHT
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should create a EmployeePayableDto from an freight`() {
        val expectedPayable = sampleEmployeePayableDto().copy(
            id = null,
            parentId = "freightId1",
            installments = 1,
            value = 100.00,
            generationDate = LocalDateTime.of(2024, 1, 1, 0, 0).toDate(),
            type = EmployeePayableTicket.COMMISSION.name,
            isPaid = false
        )

        val freight = sampleFreight()

        val pair = factory.create(
            FinancialRecordsParams(
                data = freight,
                installments = 1,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val first = pair.first

        assertEquals(expectedPayable, first)
    }

    @Test
    fun `should create a list of TransactionsDto from an freight with one item`() {
        val expectedList = listOf(
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10).toDate(),
                number = 1,
                value = 100.00,
                type = TransactionType.PAYABLE.name,
                isPaid = false
            )
        )

        val freight = sampleFreight()

        val pair = factory.create(
            FinancialRecordsParams(
                data = freight,
                installments = 1,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val second = pair.second

        assertEquals(expectedList, second)

    }

    @Test
    fun `should create a list with one TransactionsDto from an freight with multiple items`() {
        val expectedList = listOf(
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10).toDate(),
                number = 1,
                value = 100.00,
                type = TransactionType.PAYABLE.name,
                isPaid = false
            )
        )

        val freight = sampleFreight()
        val pair = factory.create(
            FinancialRecordsParams(
                data = freight,
                installments = 3,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val second = pair.second

        assertEquals(expectedList, second)

    }

    //---------------------------------------------------------------------------------------------//
    // ADVANCE
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should create a EmployeeReceivableDto from an advance`() {
        val expectedPayable = sampleEmployeeReceivableDto().copy(
            id = null,
            parentId = "advanceId1",
            installments = 1,
            value = 100.00,
            generationDate = LocalDateTime.of(2024, 1, 1, 0, 0).toDate(),
            type = EmployeeReceivableTicket.ADVANCE.name,
            isReceived = false
        )

        val advance = sampleAdvance()

        val pair = factory.create(
            FinancialRecordsParams(
                data = advance,
                installments = 1,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val first = pair.first

        assertEquals(expectedPayable, first)
    }

    @Test
    fun `should create a list of EmployeeReceivableDto from an advance with one item`() {
        val expectedList = listOf(
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10).toDate(),
                number = 1,
                value = 100.00,
                type = TransactionType.RECEIVABLE.name,
                isPaid = false
            )
        )

        val advance = sampleAdvance()

        val pair = factory.create(
            FinancialRecordsParams(
                data = advance,
                installments = 1,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val second = pair.second

        assertEquals(expectedList, second)

    }

    @Test
    fun `should create a list with one TransactionsDto from an advance with multiple items`() {
        val expectedList = listOf(
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10).toDate(),
                number = 1,
                value = 100.00,
                type = TransactionType.RECEIVABLE.name,
                isPaid = false
            )
        )

        val advance = sampleAdvance()
        val pair = factory.create(
            FinancialRecordsParams(
                data = advance,
                installments = 3,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val second = pair.second

        assertEquals(expectedList, second)

    }

    //---------------------------------------------------------------------------------------------//
    // LOAN
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should create a EmployeeReceivableDto from an loan`() {
        val expected = sampleEmployeeReceivableDto().copy(
            id = null,
            parentId = "loanId1",
            installments = 1,
            value = 500.00,
            generationDate = LocalDateTime.of(2024, 1, 1, 0, 0).toDate(),
            type = EmployeeReceivableTicket.LOAN.name,
            isReceived = false
        )

        val loan = sampleLoan()

        val pair = factory.create(
            FinancialRecordsParams(
                data = loan,
                installments = 1,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val first = pair.first

        assertEquals(expected, first)
    }

    @Test
    fun `should create a list of EmployeeReceivableDto from an loan with one item`() {
        val expectedList = listOf(
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10).toDate(),
                number = 1,
                value = 500.00,
                type = TransactionType.RECEIVABLE.name,
                isPaid = false
            )
        )

        val loan = sampleLoan()

        val pair = factory.create(
            FinancialRecordsParams(
                data = loan,
                installments = 1,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val second = pair.second

        assertEquals(expectedList, second)

    }

    @Test
    fun `should create a list with one TransactionsDto from an loan with multiple items`() {
        val expectedList = listOf(
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10).toDate(),
                number = 1,
                value = 100.00,
                type = TransactionType.RECEIVABLE.name,
                isPaid = false
            ),
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(40).toDate(),
                number = 2,
                value = 100.00,
                type = TransactionType.RECEIVABLE.name,
                isPaid = false
            ),
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(70).toDate(),
                number = 3,
                value = 100.00,
                type = TransactionType.RECEIVABLE.name,
                isPaid = false
            ),
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(100).toDate(),
                number = 4,
                value = 100.00,
                type = TransactionType.RECEIVABLE.name,
                isPaid = false
            ),
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(130).toDate(),
                number = 5,
                value = 100.00,
                type = TransactionType.RECEIVABLE.name,
                isPaid = false
            )
        )

        val loan = sampleLoan()
        val pair = factory.create(
            FinancialRecordsParams(
                data = loan,
                installments = 5,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val second = pair.second

        assertEquals(expectedList, second)

    }

    //---------------------------------------------------------------------------------------------//
    // TRAVEL AID
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should create a EmployeeReceivableDto from an travel aid`() {
        val expected = sampleEmployeeReceivableDto().copy(
            id = null,
            parentId = "travelAid1",
            installments = 1,
            value = 100.00,
            generationDate = LocalDateTime.of(2024, 1, 1, 0, 0).toDate(),
            type = EmployeeReceivableTicket.TRAVEL_AID.name,
            isReceived = false
        )

        val aid = sampleTravelAid()

        val pair = factory.create(
            FinancialRecordsParams(
                data = aid,
                installments = 1,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val first = pair.first

        assertEquals(expected, first)
    }

    @Test
    fun `should create a list of EmployeeReceivableDto from an travel aid with one item`() {
        val expectedList = listOf(
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10).toDate(),
                number = 1,
                value = 100.00,
                type = TransactionType.RECEIVABLE.name,
                isPaid = false
            )
        )

        val travelAid = sampleTravelAid()

        val pair = factory.create(
            FinancialRecordsParams(
                data = travelAid,
                installments = 1,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val second = pair.second

        assertEquals(expectedList, second)

    }

    @Test
    fun `should create a list with one TransactionsDto from an travel aid with multiple items`() {
        val expectedList = listOf(
            TransactionDto(
                masterUid = "masterUid1",
                id = null,
                parentId = null,
                dueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10).toDate(),
                number = 1,
                value = 100.00,
                type = TransactionType.RECEIVABLE.name,
                isPaid = false
            )
        )

        val aid = sampleTravelAid()
        val pair = factory.create(
            FinancialRecordsParams(
                data = aid,
                installments = 3,
                firstDueDate = LocalDateTime.of(2024, 1, 1, 0, 0).plusDays(10)
            )
        )

        val second = pair.second

        assertEquals(expectedList, second)

    }

}