package br.com.apps.trucktech

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.apps.model.dto.bank.BankDto
import br.com.apps.model.dto.bank.BankAccountDto
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.util.toDate
import br.com.apps.trucktech.BaseInstrumentedTest.Companion.T_BANK_ID
import br.com.apps.trucktech.BaseInstrumentedTest.Companion.T_DRIVER_ID
import br.com.apps.trucktech.BaseInstrumentedTest.Companion.T_MASTER_UID
import br.com.apps.trucktech.BaseInstrumentedTest.Companion.T_TRUCK_ID
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.io.InvalidClassException
import java.time.LocalDateTime
import kotlin.reflect.KClass

/**
 * Base class for instrumented tests that provides common utility functions for UI interactions
 * and checks using Espresso. This class contains methods for performing actions like login,
 * clicking views, typing text, and verifying the visibility of UI elements.
 */
abstract class BaseInstrumentedTest : TestDataGenerator() {

    abstract fun insertTestData(params: DataGenParams)

    abstract fun clearTestData()

    abstract fun navigateToTestScreen()

    /**
     * The prefix 'T' stands for 'Test'. These constants represent the IDs of test users.
     * They are registered in the Firestore database and should not be changed to ensure
     * data consistency.
     */
    companion object {
        const val T_MASTER_UID = "aVSneOfxfUQX0lVxat3LYuV20Kj2"
        const val T_UID = "yU8yBqdWm1Z30StnEpZyuZ1atms2"
        const val T_DRIVER_ID = "8kQL8lVQMyt7I7ol2pBa"
        const val T_TRUCK_ID = "88YsWNH3BNwTvqcwxr9x"
        const val T_CUSTOMER_ID = "NA3TbrQGLn9kwHBpivWE"
        const val T_EXPEND_LABEL_ID = "zky8aUdOG407pNZjCOFn"
        const val T_BANK_ID = "dhkbskwG4vjn2FCEPYDX"
    }

    /**
     * Pauses the execution of the current thread for the specified amount of time.
     *
     * @param timer The duration in milliseconds to pause the thread.
     */
    protected fun awaitAnimation(timer: Long) = Thread.sleep(timer)

    //----------------------------------------------------------------------------------------------
    // PERFORM
    //----------------------------------------------------------------------------------------------

    /**
     * Performs a login action by typing text into the username and password fields,
     * and then clicking the login button.
     */
    protected fun performLogin() {
        Thread.sleep(3000)
        performTypeText(viewId = R.id.frag_field_user, text = "testuser@hotmail.com")
        performTypeText(viewId = R.id.frag_field_password, text = "123456")
        performCLick(viewId = R.id.frag_button_login)
        Thread.sleep(5000)
    }

    /**
     * Clicks on a view identified by its ID.
     *
     * @param viewId The ID of the view to click on.
     */
    protected fun performCLick(viewId: Int) {
        Espresso
            .onView(withId(viewId))
            .perform(click())
    }

    /**
     * Types the specified text into a view identified by its ID and then closes the
     * soft keyboard.
     * @param viewId The ID of the view to type text into.
     * @param text The text to be typed into the view.
     */
    protected fun performTypeText(viewId: Int, text: String) {
        Espresso
            .onView(withId(viewId))
            .perform(
                typeText(text),
                closeSoftKeyboard()
            )
    }

    protected fun performDropDownItemClick(viewId: Int, text: String) {
        Espresso.onView(withId(viewId)).perform(click())
        Espresso.closeSoftKeyboard()
        Espresso
            .onView(withText(text))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
    }

    protected fun performDropDownItemClick(viewId: Int, pos: Int) {
        Espresso.onView(withId(viewId)).perform(click())
        Espresso.closeSoftKeyboard()
        Espresso
            .onData(anything())
            .atPosition(pos)
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
    }

    protected fun performAlertDialogCLick(text: String) {
        Espresso
            .onView(withText("Ok"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
    }

    protected fun performClearText(viewId: Int) {
        Espresso
            .onView(withId(viewId))
            .perform(
                replaceText(""),
                closeSoftKeyboard()
            )
    }

    protected fun performClickConcatAdapter(text: String) {
        Espresso
            .onView(withText(text))
            .perform(click())
    }

    /**
     * Clicks on a specific item in a RecyclerView based on its position.
     *
     * @param viewId The ID of the RecyclerView.
     * @param pos The position of the item within the RecyclerView to click on.
     */
    protected fun performRecyclerCLick(viewId: Int, pos: Int) {
        Espresso
            .onView(withId(viewId))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    pos,
                    click()
                )
            )
    }

    //----------------------------------------------------------------------------------------------
    // SHOULD MATCH
    //----------------------------------------------------------------------------------------------

    /**
     * Checks that a view identified by its ID is displayed on the screen.
     *
     * @param viewId The ID of the view to check for visibility.
     */
    protected fun shouldBeVisible(viewId: Int) {
        Espresso
            .onView(withId(viewId))
            .check(matches(isDisplayed()))
    }

    /**
     * Checks if the `View` with the specified ID is not visible because it is gone.
     * @param viewId The ID of the `View` to be checked.
     */
    protected fun shouldBeGone(viewId: Int) {
        Espresso
            .onView(withId(viewId))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    /**
     * Checks if the `View` with the specified ID is not visible because it is invisible.
     * @param viewId The ID of the `View` to be checked.
     */
    protected fun shouldBeInvisible(viewId: Int) {
        Espresso
            .onView(withId(viewId))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
    }

    /**
     * Checks if the specified text is visible on the screen.
     *
     * @param text The text to be checked for visibility.
     */
    protected fun shouldBeVisible(text: String) {
        Espresso
            .onView(withText(text))
            .check(matches(isDisplayed()))
    }

    protected fun shouldBeVisibleWithText(viewId: Int, text: String) {
        Espresso
            .onView(withId(viewId))
            .check(
                matches(
                    allOf(isDisplayed(), withText(text))
                )
            )
    }

    protected fun shouldBeAtRecyclerPos(
        viewId: Int,
        textViewId: Int,
        pos: Int,
        text: String
    ) {
        Espresso
            .onView(withId(viewId))
            .check(matches(checkRecyclerPos(pos, text, textViewId)))
    }

    private fun checkRecyclerPos(pos: Int, text: String, textViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.appendText("RecyclerView should contain item with text: $text")
            }

            override fun matchesSafely(item: View?): Boolean {
                if (item !is RecyclerView) return false

                val viewHolder = item.findViewHolderForAdapterPosition(pos) ?: return false
                val itemView = viewHolder.itemView

                val textView = itemView.findViewById<TextView>(textViewId)
                return textView != null && textView.text.contains(text)
            }
        }

    }

}

/**
 * Abstract class for generating sample data for different types of data transfer objects (DTOs).
 * This class provides a method to generate sample data based on the type of DTO specified and parameters provided.
 */
abstract class TestDataGenerator {

    /**
     * Data generation parameters for controlling the generated sample data.
     *
     * @property size The number of sample data items to generate.
     * @property corrupted Flag indicating if the data should be corrupted (for testing purposes).
     */
    class DataGenParams(val size: Int = 1, val corrupted: Boolean = false)

    /**
     * A map of supported DTO classes to their respective data generation functions.
     *
     * @property supportedClasses A map where keys are classes of type [KClass<out Any>],
     * and values are functions that generate a list of sample data for those classes.
     */
    private val supportedClasses: Map<KClass<out Any>, (DataGenParams) -> List<Any>> = mapOf(
        BankAccountDto::class to ::generateBankAccData,
        BankDto::class to ::generateBankData,
        TravelDto::class to ::generateTravelData
    )

    /**
     * Generates a list of sample data for the specified class type.
     *
     * @param kClass The class type for which to generate sample data. Must be a supported type.
     * @param params Parameters to control the data generation, including the size of the list to generate.
     * @return A list of sample data of the specified type [T].
     * @throws InvalidClassException If the specified class type is not supported.
     */
    fun <T : Any> generateSampleData(
        kClass: KClass<T>,
        params: DataGenParams = DataGenParams()
    ): List<T> {
        val generator = supportedClasses[kClass] ?: throw InvalidClassException("Invalid class")
        @Suppress("UNCHECKED_CAST")
        return generator(params) as List<T>
    }

    private fun generateBankAccData(params: DataGenParams): List<BankAccountDto> {
        return List(params.size) {
            val num = it + 1
            val branch = if (!params.corrupted) "123$num".toInt() else null
            val date = LocalDateTime.of(2024, 7, 1, 10, 10, num)

            BankAccountDto(
                masterUid = T_MASTER_UID,
                employeeId = T_DRIVER_ID,
                bankId = T_BANK_ID,
                insertionDate = date.toDate(),
                branch = branch,
                accNumber = "123456$num".toInt(),
                pix = "00$num.00$num.00$num-0$num",
                mainAccount = false,
                pixType = "CPF"
            )
        }
    }

    private fun generateBankData(params: DataGenParams): List<BankDto> {
        return List(params.size) {
            val num = it + 1
            val bankName = if (!params.corrupted) "BankAccountDto$num" else null
            BankDto(
                name = bankName,
                code = num
            )
        }
    }

    private fun generateTravelData(params: DataGenParams): List<TravelDto> {
        return List(params.size) {
            val num = it + 1
            val initDate = LocalDateTime.of(2024, 6, 1, 10, 10, num)

            TravelDto(
                masterUid = T_MASTER_UID,
                employeeId = T_DRIVER_ID,
                truckId = T_TRUCK_ID,
                isFinished = false,
                isClosed = false,
                initialDate = initDate.toDate(),
                initialOdometerMeasurement = 10000.0,
            )
        }
    }

}
