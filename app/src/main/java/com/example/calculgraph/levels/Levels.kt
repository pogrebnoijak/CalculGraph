package com.example.calculgraph.levels

import android.util.Log
import com.example.calculgraph.activity.AnyActivity
import com.example.calculgraph.constant.KOL_LEVELS
import com.example.calculgraph.constant.LEVELS_ALL_KOL
import com.example.calculgraph.constant.MODES
import com.example.calculgraph.dataBase.getLevelId
import com.example.calculgraph.enums.Computability
import com.example.calculgraph.states.LevelState


object Levels {
    val levels = MutableList<LevelState?>(LEVELS_ALL_KOL) { null }

    private fun addLevel(mode: String, computability: Computability, num: Int, levelState: LevelState) {
        val id = getLevelId(mode, computability, num) - 1
        if (levels[id] == null) levels[id] = levelState else throw error("level exists")
    }

    fun addLevels() {
        MODES.forEach { mode ->
            Computability.values().forEach { comp ->
                (1..KOL_LEVELS).forEach { num ->
//                    addLevel(mode, comp, num, getLevel(mode, comp, num))
                    addLevel(mode, comp, num, levelDefault)
                }
            }
        }
        Log.d(AnyActivity.logTAG, "Levels: add all")
    }

    fun notExistLevels() = levels.count { it == null }
}