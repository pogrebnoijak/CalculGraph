package com.example.calculgraph.matchers

import android.view.View
import android.widget.TextView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher
import kotlin.reflect.typeOf


fun getText(matcher: ViewInteraction): String {
    lateinit var text: String
    matcher.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(TextView::class.java)
        override fun getDescription(): String = "Text of the view"
        override fun perform(uiController: UiController, view: View) {
            text = (view as TextView).text.toString()
        }
    })
    return text
}
