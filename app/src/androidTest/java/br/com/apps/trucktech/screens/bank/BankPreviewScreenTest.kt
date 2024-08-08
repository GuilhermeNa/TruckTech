package br.com.apps.trucktech.screens.bank

import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.apps.model.dto.bank.BankAccountDto
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

class BankPreviewScreenTest : BaseInstrumentedTest() {

    private val write = EmployeeWriteImpl(FirebaseFirestore.getInstance())
    private val read = EmployeeReadImpl(FirebaseFirestore.getInstance())
    private val repo = EmployeeRepository(write, read)

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    //---------------------------------------------------------------------------------------------//
    // Start
    //---------------------------------------------------------------------------------------------//

    @Test
    fun should_show_data() {
        insertTestData(DataGenParams(size = 1))
        navigateToTestScreen()

        shouldBeVisibleWithText(viewId = R.id.frag_bank_preview_bank, text = "Banco do Brasil")
        shouldBeVisibleWithText(viewId = R.id.frag_bank_preview_branch, text = "1231")
        shouldBeVisibleWithText(viewId = R.id.frag_bank_preview_number, text = "1234561")
        shouldBeVisibleWithText(viewId = R.id.frag_bank_preview_type, text = "Cpf")
        shouldBeVisibleWithText(viewId = R.id.frag_bank_preview_pix, text = "001.001.001-01")

    }

    @Test
    fun should_delete_data() {
        insertTestData(DataGenParams(size = 1))
        navigateToTestScreen()
        performCLick(R.id.menu_preview_delete)
        performAlertDialogCLick("Ok")

        // Check
        awaitAnimation(500)
        shouldBeVisible(R.id.empty)
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

    override fun clearTestData() {
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
        awaitAnimation(2000)
        performRecyclerCLick(
            viewId = R.id.fragment_bank_recycler,
            pos = 0
        )
        awaitAnimation(500)
    }

}