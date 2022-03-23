package com.example.calculgraph.swipe

import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.actionWithAssertions

fun swipeLeftUp(): ViewAction = actionWithAssertions(GeneralSwipeAction(
    Swipe.FAST,
    GeneralLocation.CENTER,
    GeneralLocation.TOP_LEFT,
    Press.FINGER))

fun swipeLeftDown(): ViewAction = actionWithAssertions(GeneralSwipeAction(
    Swipe.FAST,
    GeneralLocation.CENTER,
    GeneralLocation.BOTTOM_LEFT,
    Press.FINGER))

fun swipeRightUp(): ViewAction = actionWithAssertions(GeneralSwipeAction(
    Swipe.FAST,
    GeneralLocation.CENTER,
    GeneralLocation.TOP_RIGHT,
    Press.FINGER))

fun swipeRightDown(): ViewAction = actionWithAssertions(GeneralSwipeAction(
    Swipe.FAST,
    GeneralLocation.CENTER,
    GeneralLocation.BOTTOM_RIGHT,
    Press.FINGER))
