package com.example.calculgraph.levels

import com.example.calculgraph.constant.LEVELS_ALL_KOL
import com.example.calculgraph.dataBase.getLevelId
import com.example.calculgraph.enums.Computability
import com.example.calculgraph.states.LevelState


object Levels {
    val levels = MutableList<LevelState?>(LEVELS_ALL_KOL) { null }

    fun addLevel(mode: String, computability: Computability, num: Int, levelState: LevelState) {
        val id = getLevelId(mode, computability, num) - 1
        if (levels[id] == null) levels[id] = levelState else throw error("level exists")
    }

    fun notExistLevels() = levels.count { it == null }
}