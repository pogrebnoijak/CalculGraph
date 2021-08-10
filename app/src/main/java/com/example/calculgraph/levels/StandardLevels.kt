package com.example.calculgraph.levels

import com.example.calculgraph.constant.KOL_NODES
import com.example.calculgraph.enums.Computability.*
import com.example.calculgraph.enums.Operation
import com.example.calculgraph.states.Inscription
import com.example.calculgraph.states.LevelState


object StandardLevels : AnyLevels {
    override val easyLevels by lazy {
        listOf(
            LevelState(2, 1, listOf(1), listOf(3), List(KOL_NODES[EASY]!!) { List(KOL_NODES[EASY]!!) { Inscription(Operation.PLUS, 1) } })
        )
    }
    override val mediumLevels: List<LevelState>
        get() = TODO("Not yet implemented")
    override val hardLevels: List<LevelState>
        get() = TODO("Not yet implemented")
    override val insaneLevels: List<LevelState>
        get() = TODO("Not yet implemented")
}