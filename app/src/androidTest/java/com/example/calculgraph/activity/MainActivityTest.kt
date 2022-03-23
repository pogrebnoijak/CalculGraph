package com.example.calculgraph.activity

import android.graphics.Typeface
import com.example.calculgraph.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.calculgraph.matchers.withStyle
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val mActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    @Test
    fun test_new_game() {
        onView(withId(R.id.new_game)).perform(click())
        onView(withId(R.id.standard)).check(matches(isDisplayed()))
    }

    @Test
    fun test_continue() {
        onView(withId(R.id.continue_)).perform(click())
        onView(withId(R.id.back)).check(matches(isDisplayed()))
    }

    @Test
    fun test_levels() {
        onView(withId(R.id.levels)).perform(click())
        onView(withId(R.id.menu)).check(matches(isDisplayed()))
    }

    @Test
    fun test_statistic() {
        onView(withId(R.id.statistic)).perform(click())
        onView(withId(R.id.menu)).check(matches(isDisplayed()))
    }

    @Test
    fun test_settings() {
        onView(withId(R.id.settings)).perform(click())
        onView(withId(R.id.menu)).check(matches(isDisplayed()))
    }

    @Test
    fun test_game_name() {
        onView(withId(R.id.textView)).check(matches(withText("CalculGraph")))
    }

    @Test
    fun check_style() {
        onView(withId(R.id.textView)).check(matches(withStyle(Typeface.ITALIC)))
    }
}
