package com.example.calculgraph.page.common

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.calculgraph.matchers.withColorText

open class AnyTextPage : Page() {
    private fun getItem(data: String) = onView(withText(data))

    fun click(data: String) = also { getItem(data).perform(ViewActions.click()) }

    fun text(data: String) = also {  getItem(data).check(matches(withText(data))) }

    fun textColor(data: String, color: Int) =
        also {  getItem(data).check(matches(withColorText(color))) }

    fun scroll(data: Int) = also { onView(withText(data)).perform(scrollTo()) }
}
