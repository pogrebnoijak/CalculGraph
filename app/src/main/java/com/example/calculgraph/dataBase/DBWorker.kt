package com.example.calculgraph.dataBase

import android.content.Context
import com.example.calculgraph.states.StatisticState
import java.lang.Integer.max

class DBWorker {
    private lateinit var db: DBHelper
    private lateinit var statistic: StatisticState

    fun init(context: Context) {
        db = DBHelper(context)
        statistic = db.read() ?: throw error("No statistic in the db")
    }

    fun updateExit() {
        updateStatistic()
        updateGameParams()
        updateSaveState()
    }

    private fun updateStatistic() {
        db.update(statistic)
    }

    private fun updateGameParams() {

    }

    private fun updateSaveState() {

    }

    fun updateSettings() {

    }

    fun tempUpdateStatistic(score: Int) {
        statistic.apply {
            sredScore = (sredScore * kolGame + score) / (kolGame + 1)
            kolGame++
            maxScore = max(maxScore, score)
        }
    }
}