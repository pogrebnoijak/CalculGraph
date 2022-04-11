package com.example.calculgraph.activity

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.calculgraph.page.common.Page
import com.example.calculgraph.page.game.layout.DrawLayoutPage
import com.example.calculgraph.page.game.textView.KolMovesTextViewPage
import com.example.calculgraph.page.main.button.MenuButtonPage
import com.example.calculgraph.secondary.start_game_standard
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
        start_game_standard()
        repeat(3) {
            Page.on<DrawLayoutPage>()
                .swipeRightDown()
                .on<DrawLayoutPage>()
                .swipeLeftDown()
                .on<DrawLayoutPage>()
                .swipeLeftUp()
                .on<DrawLayoutPage>()
                .swipeRightUp()
        }
    }

    @Test
    fun test_swipe() {  // smth going on
        repeat(5) {
            start_game_standard()
            val historyMoves = mutableListOf<String>()
            sleep(500L)
            listOf({ Page.on<DrawLayoutPage>().swipeRightDown() },
                { Page.on<DrawLayoutPage>().swipeLeftDown() },
                { Page.on<DrawLayoutPage>().swipeLeftUp() },
                { Page.on<DrawLayoutPage>().swipeRightUp() }).forEach { swipe ->
                swipe()
                val kolMoves = Page.on<KolMovesTextViewPage>()
                    .getText()
                historyMoves.add(kolMoves)
            }
            println(historyMoves)
            assert(historyMoves.toSet().size > 1)
            Page.on<MenuButtonPage>()
                .click()
                .sleep(500L)
        }
    }
}
