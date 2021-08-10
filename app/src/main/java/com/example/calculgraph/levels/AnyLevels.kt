package com.example.calculgraph.levels

import com.example.calculgraph.constant.KOL_NODES
import com.example.calculgraph.enums.Computability
import com.example.calculgraph.enums.Computability.*
import com.example.calculgraph.enums.Operation
import com.example.calculgraph.states.Inscription
import com.example.calculgraph.states.LevelState


interface AnyLevels {
    val easyLevels: List<LevelState>
    val mediumLevels: List<LevelState>
    val hardLevels: List<LevelState>
    val insaneLevels: List<LevelState>
}

internal fun getLevel(mode: String, comp: Computability, num: Int): LevelState =
    when(mode) {
        "standard"  -> StandardLevels
        "set"       -> SetLevels
        "max"       -> MaxLevels
        else        -> throw error("AnyLevel: wrong mode")
    }.let {
        when(comp) {
            EASY    -> it.easyLevels[num - 1]
            MEDIUM  -> it.mediumLevels[num - 1]
            HARD    -> it.hardLevels[num - 1]
            INSANE  -> it.insaneLevels[num - 1]
        }
    }

// remove later
internal val levelDefault =  LevelState(2, 1, listOf(1), listOf(3),
    List(KOL_NODES[EASY]!!) { List(KOL_NODES[EASY]!!) { Inscription(Operation.PLUS, 1) } })