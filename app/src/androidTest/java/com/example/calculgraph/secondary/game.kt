package com.example.calculgraph.secondary

import com.example.calculgraph.page.common.Page
import com.example.calculgraph.page.main.MainActivityPage
import com.example.calculgraph.page.main.button.NewGameButtonPage
import com.example.calculgraph.page.main.button.StandardButtonPage


fun start_game_standard() {
    Page.on<MainActivityPage>()
        .on<NewGameButtonPage>()
        .click()
        .on<StandardButtonPage>()
        .click()
}
