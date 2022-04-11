package com.example.calculgraph.secondary

import com.example.calculgraph.page.common.AnyTextPage
import com.example.calculgraph.page.common.Page
import com.example.calculgraph.page.main.button.MenuButtonPage
import com.example.calculgraph.page.main.button.SettingsButtonPage
import com.example.calculgraph.page.settings.spinner.LanguageSpinnerPage

fun set_english() {
    Page.on<SettingsButtonPage>()
        .click()
        .sleep(250L)
        .on<LanguageSpinnerPage>()
        .click()
        .on<AnyTextPage>()
        .click("English")
        .sleep(250L)
        .on<MenuButtonPage>()
        .click()
}
