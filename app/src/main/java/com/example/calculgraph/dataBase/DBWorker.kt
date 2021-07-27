package com.example.calculgraph.dataBase

import android.content.Context
import com.example.calculgraph.activity.AnyActivity.Companion.settings
import com.example.calculgraph.states.SaveState
import com.example.calculgraph.states.StatisticState
import java.lang.Integer.max

class DBWorker {
    private lateinit var db: DBHelper
    private lateinit var statistic: StatisticState
    private lateinit var saveState: SaveState

    fun init(context: Context) {
        db = DBHelper(context)
        statistic = (db.read("statistic") ?: throw error("No statistic in the db")) as StatisticState
    }

    fun updateExit() {
        updateStatistic()
        updateSaveState()
    }

    private fun updateStatistic() {
        db.update(statistic)
    }

    private fun updateSaveState() {
        db.update(saveState)
    }

    fun updateSettings() {
        db.update(settings)
    }

    fun tempUpdateSaveState(_saveState: SaveState) {
        saveState = _saveState
    }

    fun tempUpdateStatistic(score: Int) {
        statistic.apply {
            sredScore = (sredScore * kolGame + score) / (kolGame + 1)
            kolGame++
            maxScore = max(maxScore, score)
        }
    }
}