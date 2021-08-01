package com.example.calculgraph.helpers

import android.content.Context
import com.example.calculgraph.R
import com.example.calculgraph.constant.HOUR
import com.example.calculgraph.constant.MINUTE


object TimeWorking {
    fun showTime(time: Int, context: Context): String = when (time) {
        in 1 until 60 -> time.toString() + context.getString(R.string.seconds)
        in 60 until 3600 -> {
            val minutes = time / 60
            val sec = time % 60
            if (sec == 0) minutes.toString() + context.getString(R.string.minutes)
            else minutes.toString() + context.getString(R.string.minutes) + " " + sec.toString() + context.getString(R.string.seconds)
        }
        3600 -> "1" + context.getString(R.string.hours)
        else -> throw error("wrong time game")
    }

    fun String.toTime(context: Context): Int {
        class Time(var h: Int = 0, var m: Int = 0, var s: Int = 0)
        val time = Time()
        val h = context.getString(R.string.hours).first()
        val m = context.getString(R.string.minutes).first()
        val s = context.getString(R.string.seconds).first() // one char string
        this.split(" ").forEach {
            when(it.last()) {
                h -> time.h = it.dropLast(1).toInt()
                m -> time.m = it.dropLast(1).toInt()
                s -> time.s = it.dropLast(1).toInt()
            }
        }
        return time.h * HOUR + time.m * MINUTE + time.s
    }
}