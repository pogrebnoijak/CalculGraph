package com.example.calculgraph.levels

import com.example.calculgraph.constant.KOL_NODES
import com.example.calculgraph.enums.Computability.*
import com.example.calculgraph.enums.Operation
import com.example.calculgraph.helpers.doDataByLines
import com.example.calculgraph.states.Branch
import com.example.calculgraph.states.Inscription
import com.example.calculgraph.states.LevelState


object StandardLevels : AnyLevels {
    override val easyLevels by lazy {
        listOf(
            LevelState(2, 0, listOf(1), listOf(6),
                doDataByLines(KOL_NODES[EASY]!!, listOf(
                    Branch(0, 3, Inscription(Operation.PLUS, 2)),
                    Branch(3, 2, Inscription(Operation.PLUS, 3)),
                    Branch(3, 1, Inscription(Operation.MINUS, 5)),
                    Branch(0, 1, Inscription(Operation.MINUS, 3))
                ))
            )
        )
    }
    override val mediumLevels: List<LevelState>
        get() = TODO("Not yet implemented")
    override val hardLevels: List<LevelState>
        get() = TODO("Not yet implemented")
    override val insaneLevels: List<LevelState>
        get() = TODO("Not yet implemented")
}