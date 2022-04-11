package com.example.calculgraph.activity

import android.graphics.Typeface
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.calculgraph.page.common.Page
import com.example.calculgraph.page.main.MainActivityPage
import com.example.calculgraph.page.main.button.*
import com.example.calculgraph.page.main.textView.MainTextViewPage
import com.example.calculgraph.secondary.set_english
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val mActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    @Before
    fun set_page() {
        Page.on<MainActivityPage>()
    }

    @Test
    fun test_new_game() {
        Page.on<NewGameButtonPage>()
            .click()
            .on<StandardButtonPage>()
    }

    @Test
    fun test_continue() {
        Page.on<ContinueButtonPage>()
            .click()
            .on<BackButtonPage>()
    }

    @Test
    fun test_levels() {
        Page.on<LevelsButtonPage>()
            .click()
            .on<MenuButtonPage>()
    }

    @Test
    fun test_statistic() {
        Page.on<StatisticButtonPage>()
            .click()
            .on<MenuButtonPage>()
    }

    @Test
    fun test_settings() {
        Page.on<SettingsButtonPage>()
            .click()
            .on<MenuButtonPage>()
    }

    @Test
    fun test_game_name() {
        set_english()
        Page.on<MainTextViewPage>()
            .text("CalculGraph")
    }

    @Test
    fun check_style() {
        Page.on<MainTextViewPage>()
            .style(Typeface.ITALIC)
    }
}
