package com.example.calculgraph.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.calculgraph.R
import com.example.calculgraph.dataBase.DBHelper
import com.example.calculgraph.states.SaveState
import com.example.calculgraph.states.StatisticState

class StatisticActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_statistic)
        setButtons()
        setStatistic()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            startActivity(intent)
            finish()
        }
    }

    private fun setStatistic() {
        val db = DBHelper(this)
        val saveState: SaveState = (db.read("saveState") ?: throw error("No saveState in the db")) as SaveState
        val statistic: StatisticState = (db.read("statistic", saveState.generateId()) ?: throw error("No statistic in the db")) as StatisticState
        findViewById<TextView>(R.id.kolGame).text = "${statistic.kolGame}"
        findViewById<TextView>(R.id.sredScore).text = String.format("%.3f", statistic.sredScore)
        findViewById<TextView>(R.id.maxScore).text = "${statistic.maxScore}"
    }
}