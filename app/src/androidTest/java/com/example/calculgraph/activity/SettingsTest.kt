package com.example.calculgraph.activity

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.calculgraph.R
import com.example.calculgraph.matchers.withColorText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep


@RunWith(AndroidJUnit4::class)
class SettingsTest {
    @get:Rule
    val mActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    @Before
    fun to_settings() {
        onView(withId(R.id.settings)).perform(click())
        sleep(250L)
    }

    @Test
    fun test_settings() {
        onView(withId(R.id.moves)).perform(replaceText("3"))
        onView(withId(R.id.moves)).check(matches(withText("3")))

        onView(withId(R.id.moves)).perform(replaceText("0"))
        onView(withId(R.id.moves)).check(matches(withText("2")))

        onView(withId(R.id.moves)).perform(replaceText("9"))
        onView(withId(R.id.moves)).check(matches(withText("8")))

        onView(withId(R.id.moves)).perform(replaceText("5643143215"))
        onView(withId(R.id.moves)).check(matches(withText("5")))
    }

    @Test
    fun test_update_settings() {
        onView(withId(R.id.language)).perform(click())
        onView(withText("English")).perform(click())
        onView(withText("Language:")).check(matches(withText("Language:")))
        sleep(250L)
        onView(withId(R.id.language)).perform(click())
        onView(withText("Русский")).perform(click())
        onView(withText("Язык:")).check(matches(withText("Язык:")))
    }

    @Test
    fun test_update_theme() {
        onView(withId(R.id.language)).perform(click())
        onView(withText("English")).perform(click())
        sleep(250L)

        onView(withId(R.id.theme)).perform(click())
        onView(withText("Standard")).perform(click())
        onView(withText("Theme:")).check(matches(withColorText(Color.WHITE)))
        sleep(250L)
        onView(withId(R.id.theme)).perform(click())
        onView(withText("Other")).perform(click())
        onView(withText("Theme:")).check(matches(withColorText(Color.BLACK)))
    }
}
