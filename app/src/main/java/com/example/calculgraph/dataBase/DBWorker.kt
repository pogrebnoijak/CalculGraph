package com.example.calculgraph.dataBase

import android.content.Context
import com.example.calculgraph.activity.AnyActivity.Companion.settings
import com.example.calculgraph.constant.KOL_LEVELS
import com.example.calculgraph.constant.MODES
import com.example.calculgraph.enums.Computability
import com.example.calculgraph.states.LastLevelsState
import com.example.calculgraph.states.LevelState
import com.example.calculgraph.states.SaveState
import com.example.calculgraph.states.StatisticState
import java.lang.Integer.max

class DBWorker {
    private lateinit var db: DBHelper
    private lateinit var statistic: StatisticState
    private lateinit var saveState: SaveState

    fun init(context: Context, downloadStat: Boolean = true): SaveState {
        db = DBHelper(context)
        saveState = db.read("saveState") as? SaveState ?: throw error("No saveState in the db")
        if (downloadStat) downloadStatistic()
        return saveState
    }

    fun initLevels(context: Context) { db = DBHelper(context) }

    private fun downloadStatistic() {
        statistic = db.read("statistic", saveState.generateId()) as? StatisticState ?: throw error("No statistic in the db")
    }

    fun updateStatistic(score: Int) {
        downloadStatistic()
        statistic.apply {
            sredScore = (sredScore * kolGame + score) / (kolGame + 1)
            kolGame++
            maxScore = max(maxScore, score)
        }
        db.update(statistic, saveState.generateId())
    }

    fun updateSettings() = db.update(settings)

    fun updateSaveState(_saveState: SaveState) {
        saveState = _saveState
        db.update(saveState)
    }

    fun getLevel(mode: String, computability: Computability, num: Int): LevelState {
        val id = getLevelId(mode, computability, num)
        return db.read("levels", id) as? LevelState ?: throw error("No levels in the db")
    }

    fun getGroupsLevels(): LastLevelsState =
        db.read("lastLevels") as? LastLevelsState ?: throw error("No lastLevels in the db")

    fun updateGroupsLevels(state: LastLevelsState) = db.update(state)
}

internal fun getGroupLevelId(mode: String, computability: Computability): Int =
    MODES.indexOf(mode) * Computability.values().size + Computability.values().indexOf(computability)

internal fun getLevelId(mode: String, computability: Computability, num: Int): Int =
    getGroupLevelId(mode, computability) * KOL_LEVELS + num