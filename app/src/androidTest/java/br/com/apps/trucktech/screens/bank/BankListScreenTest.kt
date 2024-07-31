package br.com.apps.trucktech.screens.bank

import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.repository.employee.EmployeeReadImpl
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.repository.employee.EmployeeWriteImpl
import br.com.apps.repository.util.FIRESTORE_COLLECTION_BANK
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DRIVER
import br.com.apps.trucktech.BaseInstrumentedTest
import br.com.apps.trucktech.R
import br.com.apps.trucktech.ui.activities.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

class BankListScreenTest : BaseInstrumentedTest() {

    private val write = EmployeeWriteImpl(FirebaseFirestore.getInstance())
    private val read = EmployeeReadImpl(FirebaseFirestore.getInstance())
    private val repo = EmployeeRepository(write, read)

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    //---------------------------------------------------------------------------------------------//
    // Start
    //---------------------------------------------------------------------------------------------//

    @Test
    fun should_show_empty_layout_when_data_is_empty() {
        navigateToTestScreen()

        shouldBeVisible(R.id.empty)
        shouldBeGone(R.id.error)
        shouldBeGone(R.id.box_gif)
        shouldBeGone(R.id.fragment_bank_recycler)
    }

    @Test
    fun should_show_error_layout_when_there_is_error_with_bank_accounts() {
        insertTestData(params = DataGenParams(corrupted = true))
        navigateToTestScreen()

        shouldBeVisible(R.id.error)
        shouldBeGone(R.id.empty)
        shouldBeGone(R.id.box_gif)
        shouldBeGone(R.id.fragment_bank_recycler)
    }

    @Test
    fun should_show_one_item() = runTest {
        insertTestData(params = DataGenParams(1))
        navigateToTestScreen()

        awaitAnimation(1000)
        shouldBeVisible("Banco do Brasil")
        shouldBeGone(R.id.empty)
        shouldBeGone(R.id.error)
        shouldBeGone(R.id.box_gif)
    }

    @Test
    fun should_show_five_items() {
        insertTestData(params = DataGenParams(5))
        navigateToTestScreen()

        shouldBeAtRecyclerPos(
            viewId = R.id.fragment_bank_recycler,
            textViewId = R.id.item_bank_recycler_name,
            pos = 0,
            text = "Banco do Brasil",
        )
        shouldBeAtRecyclerPos(
            viewId = R.id.fragment_bank_recycler,
            textViewId = R.id.item_bank_recycler_name,
            pos = 1,
            text = "Banco do Brasil",
        )
        shouldBeAtRecyclerPos(
            viewId = R.id.fragment_bank_recycler,
            textViewId = R.id.item_bank_recycler_name,
            pos = 2,
            text = "Banco do Brasil",
        )
        shouldBeAtRecyclerPos(
            viewId = R.id.fragment_bank_recycler,
            textViewId = R.id.item_bank_recycler_name,
            pos = 3,
            text = "Banco do Brasil",
        )
        shouldBeAtRecyclerPos(
            viewId = R.id.fragment_bank_recycler,
            textViewId = R.id.item_bank_recycler_name,
            pos = 4,
            text = "Banco do Brasil",
        )

        shouldBeGone(R.id.empty)
        shouldBeGone(R.id.error)
        shouldBeGone(R.id.box_gif)
    }

    //---------------------------------------------------------------------------------------------//
    // Finish
    //---------------------------------------------------------------------------------------------//

    @After
    fun tearDown() = clearTestData()

    override fun insertTestData(params: DataGenParams) = runTest {
        val teste = generateSampleData(BankAccountDto::class, params)
        teste.forEach { repo.saveBankAccount(it, EmployeeType.DRIVER) }
    }

    override fun clearTestData() = runTest {
        FirebaseFirestore
            .getInstance()
            .collection(FIRESTORE_COLLECTION_DRIVER)
            .document(T_DRIVER_ID)
            .collection(FIRESTORE_COLLECTION_BANK)
            .get()
            .addOnSuccessListener {
                for (document in it.documents) {
                    document.reference.delete()
                }
            }
    }

    override fun navigateToTestScreen() {
        if (FirebaseAuth.getInstance().currentUser == null) performLogin()

        // Navigate to settings and than bank list
        awaitAnimation(1000)
        performCLick(viewId = R.id.nav_settings)
        performRecyclerCLick(viewId = R.id.fragment_settings_recycler, pos = 3)

        // Reach bank List and show loading animation
        shouldBeVisible(R.id.box_gif)
        awaitAnimation(1500)
    }

}