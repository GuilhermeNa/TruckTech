package br.com.apps.trucktech.screens

import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.expressions.getCompleteDateInPtBr
import br.com.apps.repository.repository.travel.TravelReadImpl
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.repository.travel.TravelWriteImpl
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRAVELS
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

class TravelListScreenTest : BaseInstrumentedTest() {

    private val write = TravelWriteImpl(FirebaseFirestore.getInstance())
    private val read = TravelReadImpl(FirebaseFirestore.getInstance())
    private val repo = TravelRepository(read, write)

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    //---------------------------------------------------------------------------------------------//
    // Start
    //---------------------------------------------------------------------------------------------//

    @Test
    fun should_show_empty_layout_when_data_is_empty() {
        navigateToTestScreen()

        shouldBeVisible(R.id.empty)
        shouldBeGone(R.id.box_gif)
        shouldBeGone(R.id.error)
    }

    @Test
    fun should_show_one_item() {
        insertTestData(params = DataGenParams(1))
        navigateToTestScreen()

        shouldBeVisible("01 de junho de 2024")
        shouldBeGone(R.id.empty)
        shouldBeGone(R.id.error)
        shouldBeGone(R.id.box_gif)
    }

    @Test
    fun should_insert_travel() {
        navigateToTestScreen()

        performCLick(R.id.frag_travel_fab)
        performAlertDialogCLick("Ok")
        performTypeText(
            viewId = R.id.dialog_edit_text,
            text = "10000"
        )
        performAlertDialogCLick("Ok")

        awaitAnimation(500)
        shouldBeVisible(LocalDateTime.now().getCompleteDateInPtBr())
    }

    //---------------------------------------------------------------------------------------------//
    // Finish
    //---------------------------------------------------------------------------------------------//

    @After
    fun tearDown() = clearTestData()

    override fun insertTestData(params: DataGenParams) = runTest {
        val teste = generateSampleData(TravelDto::class, params)
        teste.forEach { repo.save(it) }
    }

    override fun clearTestData() {
        FirebaseFirestore
            .getInstance()
            .collection(FIRESTORE_COLLECTION_TRAVELS)
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
        performCLick(viewId = R.id.nav_travel)

        shouldBeVisible(R.id.box_gif)
        awaitAnimation(1500)
    }

}