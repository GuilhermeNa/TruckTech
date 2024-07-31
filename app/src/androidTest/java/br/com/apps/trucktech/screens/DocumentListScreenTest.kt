package br.com.apps.trucktech.screens

import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.apps.trucktech.BaseInstrumentedTest
import br.com.apps.trucktech.R
import br.com.apps.trucktech.ui.activities.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import org.junit.Rule
import org.junit.Test

class DocumentListScreenTest: BaseInstrumentedTest() {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    //---------------------------------------------------------------------------------------------//
    // Start
    //---------------------------------------------------------------------------------------------//

    @Test
    fun should_show_data() {
        navigateToTestScreen()
        shouldBeAtRecyclerPos(
            viewId = R.id.frag_doc_list_recycler,
            textViewId = R.id.item_document_recycler_title,
            pos = 0,
            text = "TEST3A2 - TestDoc"
        )

    }

    //---------------------------------------------------------------------------------------------//
    // Finish
    //---------------------------------------------------------------------------------------------//

    override fun insertTestData(params: DataGenParams) = Unit

    override fun clearTestData() = Unit

    override fun navigateToTestScreen() {
        if (FirebaseAuth.getInstance().currentUser == null) performLogin()

        awaitAnimation(1000)
        performCLick(viewId = R.id.nav_documents)
        awaitAnimation(1500)
    }

}