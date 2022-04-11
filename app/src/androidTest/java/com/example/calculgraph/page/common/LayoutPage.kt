package com.example.calculgraph.page.common

import com.example.calculgraph.swipe.swipeLeftUp as swipeLeftUp_
import com.example.calculgraph.swipe.swipeLeftDown as swipeLeftDown_
import com.example.calculgraph.swipe.swipeRightUp as swipeRightUp_
import com.example.calculgraph.swipe.swipeRightDown as swipeRightDown_

open class LayoutPage : Page() {
    fun swipeLeftUp() = also { item.perform(swipeLeftUp_()) }

    fun swipeLeftDown() = also { item.perform(swipeLeftDown_()) }

    fun swipeRightUp() = also { item.perform(swipeRightUp_()) }

    fun swipeRightDown() = also { item.perform(swipeRightDown_()) }
}
