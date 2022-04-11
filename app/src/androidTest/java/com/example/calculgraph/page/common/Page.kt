package com.example.calculgraph.page.common

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId

open class Page {
    companion object {
        inline fun <reified T : Page> on(): T = Page().on()
    }

    @IdRes
    open val id: Int? = null

    val item: ViewInteraction
        get() = onView(withId(id!!))

    inline fun <reified T : Page> on(): T {
        val page = T::class.constructors.first().call()
        page.verify()
        return page
    }

    open fun verify(): Page = this

    fun back(): Page {
        pressBack()
        return this
    }

    fun sleep(millis: Long) = also { Thread.sleep(millis) }
}
