package com.example.calculgraph.page.common

import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

open class ButtonPage : Page() {
    override fun verify() = also { item.check(matches(isDisplayed())) }

    fun click() = also { item.perform(ViewActions.click()) }
}
