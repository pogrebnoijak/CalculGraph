package com.example.calculgraph.page.common

import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.calculgraph.matchers.withStyle
import com.example.calculgraph.matchers.getText as getText_

open class TextViewPage : Page() {
    fun writeText(data: String) = also { item.perform(ViewActions.replaceText(data)) }

    fun text(data: String) = also { item.check(matches(withText(data))) }

    fun getText() = getText_(item)

    fun style(data: Int) = also { item.check(matches(withStyle(data))) }
}
