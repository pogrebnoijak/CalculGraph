package com.example.calculgraph.helpers

import com.example.calculgraph.constant.HOUR
import com.example.calculgraph.constant.MINUTE

object TimeWorking {
    fun showTime(time: Int): String = when(time) {
        in 1 until 60 -> "${time}s"
        in 60 until 3600 -> {
            val minutes = time / 60
            val sec = time % 60
            if (sec == 0) "${minutes}m" else "${minutes}m ${sec}s"
        }
        3600 -> "1h"
        else -> throw error("wrong time game")
    }

    fun String.toTime(): Int {
        class Time(var h: Int = 0, var m: Int = 0, var s: Int = 0)
        val time = Time()
        this.split(" ").forEach {
            when(it.last()) {
                'h' -> time.h = it.dropLast(1).toInt()
                'm' -> time.m = it.dropLast(1).toInt()
                's' -> time.s = it.dropLast(1).toInt()
            }
        }
        return time.h * HOUR + time.m * MINUTE + time.s
    }
}