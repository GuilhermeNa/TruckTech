package br.com.apps.trucktech.screens.bank

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.apps.model.dto.bank.BankAccountDto
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.repository.employee.EmployeeReadImpl
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.repository.employee.EmployeeWriteImpl
import br.com.apps.repository.util.FIRESTORE_COLLECTION_BANK
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DRIVER
import br.com.apps.repository.util.SUCCESSFULLY_SAVED
import br.com.apps.trucktech.BaseInstrumentedTest
import br.com.apps.trucktech.R
import br.com.apps.trucktech.ui.activities.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

class BankEditorScreenTest : BaseInstrumentedTest() {

    private val write = EmployeeWriteImpl(FirebaseFirestore.getInstance())
    private val read = EmployeeReadImpl(FirebaseFirestore.getInstance())
    private val repo = EmployeeRepository(write, read)

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    //---------------------------------------------------------------------------------------------//
    // Start
    //---------------------------------------------------------------------------------------------//

    @Test
    fun should_insert() {
        navigateToTestScreen()
        performCLick(viewId = R.id.frag_bank_list_fab)
        awaitAnimation(500)

        // Perform
        performDropDownItemClick(
            viewId = R.id.frag_bank_editor_bank_auto_complete,
            text = "Bradesco"
        )
        performTypeText(
            viewId = R.id.frag_bank_editor_branch,
            text = "123"
        )
        performTypeText(
            viewId = R.id.frag_bank_editor_acc_number,
            text = "123456"
        )
        performDropDownItemClick(
            viewId = R.id.fragment_bank_editor_auto_complete,
            text = "Cpf"
        )
        performTypeText(
            viewId = R.id.frag_bank_editor_pix,
            text = "00000000000"
        )
        performCLick(viewId = R.id.frag_bank_editor_button)

        // Check
        awaitAnimation(500)
        shouldBeVisible(SUCCESSFULLY_SAVED)
        shouldBeAtRecyclerPos(
            viewId = R.id.fragment_bank_recycler,
            textViewId = R.id.item_bank_recycler_name,
            pos = 0,
            text = "Bradesco"
        )
    }

    @Test
    fun should_edit() {
        insertTestData(DataGenParams(size = 1))
        navigateToTestScreen()

        // Perform
        awaitAnimation(1000)
        performRecyclerCLick(
            viewId = R.id.fragment_bank_recycler,
            pos = 0
        )
        performCLick(viewId = R.id.menu_preview_edit)
        performClearText(viewId = R.id.frag_bank_editor_bank_auto_complete)
        performDropDownItemClick(
            viewId = R.id.frag_bank_editor_bank_auto_complete,
            text = "Bradesco"
        )
        performCLick(viewId = R.id.frag_bank_editor_button)
        Espresso.pressBack()

        // Check
        awaitAnimation(500)
        shouldBeAtRecyclerPos(
            viewId = R.id.fragment_bank_recycler,
            textViewId = R.id.item_bank_recycler_name,
            pos = 0,
            text = "Bradesco"
        )
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
        awaitAnimation(1500)
    }

}