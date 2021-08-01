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

    fun init(context: Context, downloadStat: Boolean = true): SaveState {
        db = DBHelper(context)
        saveState = (db.read("saveState") ?: throw error("No saveState in the db")) as SaveState
        if (downloadStat) downloadStatistic()
        return saveState
    }

    private fun downloadStatistic() {
        statistic = (db.read("statistic", saveState.generateId()) ?: throw error("No statistic in the db")) as StatisticState
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

    fun updateSettings() {
        db.update(settings)
    }

    fun updateSaveState(_saveState: SaveState) {
        saveState = _saveState
        db.update(saveState)
    }
}
