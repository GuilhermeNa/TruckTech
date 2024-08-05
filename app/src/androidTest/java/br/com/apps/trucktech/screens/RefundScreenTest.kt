package br.com.apps.trucktech.screens

import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.apps.model.dto.travel.OutlayDto
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.util.toDate
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EXPENDS
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

class RefundScreenTest: BaseInstrumentedTest() {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    //---------------------------------------------------------------------------------------------//
    // Start
    //---------------------------------------------------------------------------------------------//

    @Test
    fun should_show_empty() {
        navigateToTestScreen()

        awaitAnimation(500)
        shouldBeVisible(R.id.empty)
        shouldBeGone(R.id.error)
    }

    @Test
    fun should_show_data() {
        insertTestData(params = DataGenParams())
        navigateToTestScreen()

        awaitAnimation(500)
        shouldBeAtRecyclerPos(
            viewId = R.id.fragment_refund_recycler,
            textViewId = R.id.item_to_receive_description,
            pos = 0,
            text = "Você realizou"
        )
        shouldBeGone(R.id.empty)
        shouldBeGone(R.id.error)

    }

    //---------------------------------------------------------------------------------------------//
    // Finish
    //---------------------------------------------------------------------------------------------//

    @After
    fun tearDown() = clearTestData()

    override fun insertTestData(params: DataGenParams) = runTest {
        val travel = TravelDto(
            masterUid = T_MASTER_UID,
            id = "1",
            employeeId = T_DRIVER_ID,
            truckId = T_TRUCK_ID,
            isFinished = false,
            isClosed = false,
            initialDate = LocalDateTime.of(2024, 6, 1, 1, 10).toDate(),
            initialOdometerMeasurement = 10000.0,
        )

        val expend = OutlayDto(
            masterUid = T_MASTER_UID,
            truckId = T_TRUCK_ID,
            employeeId = T_DRIVER_ID,
            travelId = "1",
            labelId = T_EXPEND_LABEL_ID,
            company = "Borracharia x",
            date = LocalDateTime.of(2024, 6, 10, 1, 10).toDate(),
            description = "Troca de pneus",
            value = 150.0,
            isPaidByEmployee = true,
            isAlreadyRefunded = false,
            isValid = true
        )

        FirebaseFirestore
            .getInstance()
            .collection(FIRESTORE_COLLECTION_TRAVELS)
            .document("1")
            .set(travel)

        FirebaseFirestore
            .getInstance()
            .collection(FIRESTORE_COLLECTION_EXPENDS)
            .document("2")
            .set(expend)

    }

    override fun clearTestData() = runTest {
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

        FirebaseFirestore
            .getInstance()
            .collection(FIRESTORE_COLLECTION_EXPENDS)
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
        performCLick(viewId = R.id.panel_to_receive_button)
        performCLick(viewId = R.id.panel_to_receive_fragment_refund_card)
    }

}