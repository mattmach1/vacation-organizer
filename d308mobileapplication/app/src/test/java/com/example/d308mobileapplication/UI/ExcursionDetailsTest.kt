package com.example.d308mobileapplication.UI

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExcursionDetailsTest {

    private lateinit var activity: ExcursionDetails

    @Before
    fun setUp() {
        // Prepare Intent with extras
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            ExcursionDetails::class.java
        ).apply {
            putExtra("vacationID", 1)
            putExtra("name", "Beach Day")
            putExtra("date", "06/05/25")
            putExtra("excursionID", -1)
        }

        // Build and start Activity under test
        activity = buildActivity(ExcursionDetails::class.java, intent)
            .create()
            .start()
            .resume()
            .get()
    }

    @Test
    fun `validate format returns true for correct format`() {
        assertTrue(activity.validateExcursionDateFormat("01/31/25"))
    }

    @Test
    fun `validate format returns false for bad format`() {
        assertFalse(activity.validateExcursionDateFormat("2025-01-31"))
    }

    @Test
    fun `validate date returns true if within bounds`() {
        activity.vacationStartDate = "06/01/25"
        activity.vacationEndDate   = "06/10/25"
        assertTrue(activity.validateExcursionDate("06/05/25",
            activity.vacationStartDate, activity.vacationEndDate))
    }

    @Test
    fun `validate date returns false if outside bounds`() {
        activity.vacationStartDate = "06/01/25"
        activity.vacationEndDate   = "06/10/25"
        assertFalse(activity.validateExcursionDate("05/31/25",
            activity.vacationStartDate, activity.vacationEndDate))
    }
}