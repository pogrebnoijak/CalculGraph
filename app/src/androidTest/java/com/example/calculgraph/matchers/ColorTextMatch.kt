package com.example.calculgraph.matchers

import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class ColorTextMatch(private val color: Int) : TypeSafeMatcher<View?>(View::class.java) {
    override fun describeTo(description: Description) {
        description.appendText("with text color: ")
        description.appendValue(color)
    }

    override fun matchesSafely(item: View?) = when(item) {
        is TextView -> item.currentTextColor == color
        else -> false
    }
}

fun withColorText(color: Int): Matcher<View?> {
    return ColorTextMatch(color)
}
