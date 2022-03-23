package com.example.calculgraph.matchers

import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class StyleMatch(private val style: Int) : TypeSafeMatcher<View?>(View::class.java) {
    override fun describeTo(description: Description) {
        description.appendText("with style: ")
        description.appendValue(style)
    }

    override fun matchesSafely(item: View?) = when(item) {
        is TextView -> item.typeface.style == style
        else -> false
    }
}

fun withStyle(style: Int): Matcher<View?> {
    return StyleMatch(style)
}
