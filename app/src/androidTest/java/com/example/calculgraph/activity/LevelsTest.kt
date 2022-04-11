package com.example.calculgraph.activity

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.calculgraph.R
import com.example.calculgraph.page.common.AnyTextPage
import com.example.calculgraph.page.common.Page
import com.example.calculgraph.page.levels.spinner.ModeLevelsSpinnerPage
import com.example.calculgraph.page.main.MainActivityPage
import com.example.calculgraph.page.main.button.LevelsButtonPage
import com.example.calculgraph.page.main.button.MenuButtonPage
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
        Page.on<MainActivityPage>()
            .on<LevelsButtonPage>()
            .click()
    }

    @Test
    fun test_scroll_easy() {
        Page.on<AnyTextPage>()
            .scroll(R.string.easy)
            .on<ModeLevelsSpinnerPage>()
            .on<MenuButtonPage>()
    }

    @Test
    fun test_scroll_medium() {
        Page.on<AnyTextPage>()
            .scroll(R.string.medium)
            .on<ModeLevelsSpinnerPage>()
            .on<MenuButtonPage>()
    }

    @Test
    fun test_scroll_hard() {
        Page.on<AnyTextPage>()
            .scroll(R.string.hard)
            .on<ModeLevelsSpinnerPage>()
            .on<MenuButtonPage>()
    }

    @Test
    fun test_scroll_insane() {
        Page.on<AnyTextPage>()
            .scroll(R.string.insane)
            .on<ModeLevelsSpinnerPage>()
            .on<MenuButtonPage>()
    }
}
