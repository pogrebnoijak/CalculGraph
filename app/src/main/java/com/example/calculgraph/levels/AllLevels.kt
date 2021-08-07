package com.example.calculgraph.levels

import com.example.calculgraph.constant.KOL_LEVELS
import com.example.calculgraph.constant.MODES
import com.example.calculgraph.enums.Computability
import com.example.calculgraph.enums.Operation
import com.example.calculgraph.levels.Levels.addLevel
import com.example.calculgraph.states.Inscription
import com.example.calculgraph.states.LevelState

object AllLevels {
    fun addLevels() {
        // standard
        Computability.values().forEach { comp ->
            (1..KOL_LEVELS).forEach { num ->
                addLevel(MODES[0], comp, num, LevelState(2, 1, listOf(1), listOf(3),
                    List(4) { List(4) { Inscription(Operation.PLUS, 1) } }))
            }
        }
        // set
        Computability.values().forEach { comp ->
            (1..KOL_LEVELS).forEach { num ->
                addLevel(MODES[1], comp, num, LevelState(2, 2, listOf(1,2,3), listOf(3,4,5),
                    List(4) { List(4) { Inscription(Operation.PLUS, 1) } }))
            }
        }
        // max
        Computability.values().forEach { comp ->
            (1..KOL_LEVELS).forEach { num ->
                addLevel(MODES[2], comp, num, LevelState(3, 0, listOf(2), listOf(5),
                    List(4) { List(4) { Inscription(Operation.PLUS, 1) } }))
            }
        }
    }
}
