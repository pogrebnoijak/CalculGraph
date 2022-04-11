package com.example.calculgraph.activity

import android.graphics.Color
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.calculgraph.page.common.AnyTextPage
import com.example.calculgraph.page.common.Page
import com.example.calculgraph.page.main.MainActivityPage
import com.example.calculgraph.page.main.button.SettingsButtonPage
import com.example.calculgraph.page.settings.spinner.LanguageSpinnerPage
import com.example.calculgraph.page.settings.spinner.ThemeSpinnerPage
import com.example.calculgraph.page.settings.textView.MovesTextViewPage
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SettingsTest {
    @get:Rule
    val mActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    @Before
    fun set_page() {
        Page.on<MainActivityPage>()
            .on<SettingsButtonPage>()
            .click()
            .sleep(250L)
    }

    @Test
    fun test_settings() {
        Page.on<MovesTextViewPage>()
            .writeText("3")
            .text("3")
            .writeText("0")
            .text("2")
            .writeText("9")
            .text("8")
            .writeText("5643143215")
            .text("5")
    }

    @Test
    fun test_update_settings() {
        Page.on<LanguageSpinnerPage>()
            .click()
            .on<AnyTextPage>()
            .click("English")
            .on<AnyTextPage>()
            .text("Language:")
            .sleep(250L)
            .on<LanguageSpinnerPage>()
            .click()
            .on<AnyTextPage>()
            .click("Русский")
            .on<AnyTextPage>()
            .text("Язык:")
    }

    @Test
    fun test_update_theme() {
        Page.on<LanguageSpinnerPage>()
            .click()
            .on<AnyTextPage>()
            .click("English")
            .sleep(250L)
            .on<ThemeSpinnerPage>()
            .click()
            .on<AnyTextPage>()
            .click("Standard")
            .on<AnyTextPage>()
            .textColor("Theme:", Color.WHITE)
            .sleep(250L)
            .on<ThemeSpinnerPage>()
            .click()
            .on<AnyTextPage>()
            .click("Other")
            .on<AnyTextPage>()
            .textColor("Theme:", Color.BLACK)
    }
}
