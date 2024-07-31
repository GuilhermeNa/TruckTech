package br.com.apps.trucktech.screens

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.apps.model.expressions.getMonthAndYearInPtBr
import br.com.apps.repository.repository.request.RequestReadImpl
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.repository.request.RequestWriteImpl
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REQUESTS
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.trucktech.BaseInstrumentedTest
import br.com.apps.trucktech.R
import br.com.apps.trucktech.ui.activities.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class RequestListScreenTest: BaseInstrumentedTest() {

    private val read = RequestReadImpl(FirebaseFirestore.getInstance())
    private val write = RequestWriteImpl(FirebaseFirestore.getInstance())
    private val repo = RequestRepository(read, write)

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    //---------------------------------------------------------------------------------------------//
    // Start
    //---------------------------------------------------------------------------------------------//

    @Test
    fun should_show_empty_layout() {
        navigateToTestScreen()

        shouldBeVisible(viewId = R.id.empty)
        shouldBeGone(viewId = R.id.error)
        shouldBeGone(viewId = R.id.box_gif)

    }

    @Test
    fun should_insert_request() {
        navigateToTestScreen()

        performCLick(viewId = R.id.fragment_requests_list_fab)
        performAlertDialogCLick("Ok")
        pressBack()

        awaitAnimation(1500)
        shouldBeVisible(LocalDateTime.now().getMonthAndYearInPtBr())
        shouldBeGone(viewId = R.id.empty)
        shouldBeGone(viewId = R.id.error)
        shouldBeGone(viewId = R.id.box_gif)
    }

    //---------------------------------------------------------------------------------------------//
    // Finish
    //---------------------------------------------------------------------------------------------//

    @After
    fun tearDown() = clearTestData()

    override fun insertTestData(params: DataGenParams) = runTest {

    }

    override fun clearTestData() {
        FirebaseFirestore
            .getInstance()
            .collection(FIRESTORE_COLLECTION_REQUESTS)
            .whereEqualTo(MASTER_UID, T_MASTER_UID)
            .get()
            .addOnSuccessListener {
                for (document in it.documents) {
                    document.reference.delete()
                }
            }
    }

    override fun navigateToTestScreen() {
        if (FirebaseAuth.getInstance().currentUser == null) performLogin()

        awaitAnimation(1000)
        performCLick(viewId = R.id.nav_requests)

        shouldBeVisible(R.id.box_gif)
        awaitAnimation(1500)
    }

}