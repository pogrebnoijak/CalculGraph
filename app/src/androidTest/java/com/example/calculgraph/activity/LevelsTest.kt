package com.example.calculgraph.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.calculgraph.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LevelsTest {
    @get:Rule
    val mActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    @Before
    fun to_levels() {
        onView(withId(R.id.levels)).perform(click())
    }

    @Test
    fun test_scroll_easy() {
        onView(withText(R.string.easy)).perform(scrollTo())
        onView(withId(R.id.modeLevels)).check(matches(isDisplayed()))
        onView(withId(R.id.menu)).check(matches(isDisplayed()))
    }

    @Test
    fun test_scroll_medium() {
        onView(withText(R.string.medium)).perform(scrollTo())
        onView(withId(R.id.modeLevels)).check(matches(isDisplayed()))
        onView(withId(R.id.menu)).check(matches(isDisplayed()))
    }

    @Test
    fun test_scroll_hard() {
        onView(withText(R.string.hard)).perform(scrollTo())
        onView(withId(R.id.modeLevels)).check(matches(isDisplayed()))
        onView(withId(R.id.menu)).check(matches(isDisplayed()))
    }

    @Test
    fun test_scroll_insane() {
        onView(withText(R.string.insane)).perform(scrollTo())
        onView(withId(R.id.modeLevels)).check(matches(isDisplayed()))
        onView(withId(R.id.menu)).check(matches(isDisplayed()))
    }
}
