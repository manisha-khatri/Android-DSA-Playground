package com.example.study2025.unittests.notesapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.rules.activityScenarioRule
import com.example.study2025.R

class FirstActivityTest {

    @get:Rule
    val activityScenario = activityScenarioRule<FirstActivity>()

    @Test
    fun testSubmitButton_expectedCorrectValue() {
        onView(withId(R.id.etTitle)).perform(typeText("Hello"))
        onView(withId(R.id.etDescription)).perform(typeText("World!"), closeSoftKeyboard())

        onView(withId(R.id.btnSubmit)).perform(click())

        onView(withId(R.id.tvNote)).check(matches(withText("Title: Hello\n\nDescription: World!")))
    }
}