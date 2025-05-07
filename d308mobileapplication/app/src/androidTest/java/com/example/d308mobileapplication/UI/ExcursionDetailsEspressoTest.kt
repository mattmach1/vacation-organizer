package com.example.d308mobileapplication.UI

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider
import android.content.Intent
import com.example.d308mobileapplication.R
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExcursionDetailsEspressoTest {

    @get:Rule
    val rule: ActivityScenarioRule<ExcursionDetails> = ActivityScenarioRule(
        Intent(
            ApplicationProvider.getApplicationContext(),
            ExcursionDetails::class.java
        ).apply {
            putExtra("vacationID", 1)
            putExtra("name", "Beach Day")
            putExtra("date", "06/05/25")
            putExtra("excursionID", -1)
        }
    )

    @Test
    fun invalidDateShowsFormatErrorToast() {
        // bad date
        onView(withId(R.id.datetext))
            .perform(clearText(), typeText("2025/06/05"), closeSoftKeyboard())

        // tap Save in the overflow menu
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(withText(R.id.excursionsave)).perform(click())

        // verify the toast is shown
        rule.scenario.onActivity { activity ->
            onView(withText("Excursion date must be in MM/dd/yy format"))
                .inRoot(withDecorView(not(`is`(activity.window.decorView))))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun validDateWithinBoundsFinishesActivity() {
        // valid date
        onView(withId(R.id.datetext))
            .perform(clearText(), typeText("06/05/25"), closeSoftKeyboard())

        // tap Save
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(withId(R.id.excursionsave)).perform(click())

        // the activity should finish -> title field no longer exists
        onView(withId(R.id.titletext)).check(doesNotExist())
    }
}