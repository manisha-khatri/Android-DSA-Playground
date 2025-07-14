package com.example.study2025.unittests.quotify

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import com.example.study2025.R
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test

class QuotifyMainActivityTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<QuotifyMainActivity>()

    @Test
    fun testNextButton_expectedCorrectQuote() {
        onView(withId(R.id.nextButton)).perform(click())
        onView(withId(R.id.nextButton)).perform(click())
        onView(withId(R.id.nextButton)).perform(click())
        onView(withId(R.id.quoteText)).check(matches(withText("A room without books is like a body without a soul.")))
    }

    @Test
    fun testShareButton_expectedIntentChooser() {
        Intents.init()
        val expected = allOf(hasAction(Intent.ACTION_SEND))
        onView(withId(R.id.floatingActionButton)).perform(click())
        intended(expected)
        Intents.release()
    }
}