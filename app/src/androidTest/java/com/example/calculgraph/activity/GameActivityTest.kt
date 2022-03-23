package com.example.calculgraph.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.calculgraph.R
import com.example.calculgraph.matchers.getText
import com.example.calculgraph.swipe.swipeLeftDown
import com.example.calculgraph.swipe.swipeLeftUp
import com.example.calculgraph.swipe.swipeRightDown
import com.example.calculgraph.swipe.swipeRightUp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep


@RunWith(AndroidJUnit4::class)
class GameActivityTest {
    @get:Rule
    val mActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    @Test
    fun test_swipe_any() {  // check not fall
        onView(withId(R.id.new_game)).perform(click())
        onView(withId(R.id.standard)).perform(click())

        repeat(3) {
            onView(withId(R.id.draw)).perform(swipeRightDown())
            onView(withId(R.id.draw)).perform(swipeLeftDown())
            onView(withId(R.id.draw)).perform(swipeLeftUp())
            onView(withId(R.id.draw)).perform(swipeRightUp())
        }
    }

    @Test
    fun test_swipe() {  // smth going on
        repeat(5) {
            onView(withId(R.id.new_game)).perform(click())
            onView(withId(R.id.standard)).perform(click())
            val historyMoves = mutableListOf<String>()
            sleep(500L)
            listOf({ swipeRightDown() }, { swipeLeftDown() }, { swipeLeftUp() }, { swipeRightUp() }).forEach { swipe ->
                onView(withId(R.id.draw)).perform(swipe())
                val kolMoves = getText(onView(withId(R.id.kolMoves)))
                historyMoves.add(kolMoves)
            }
            println(historyMoves)
            assert(historyMoves.toSet().size > 1)
            onView(withId(R.id.menu)).perform(click())
            sleep(500L)
        }
    }
}
