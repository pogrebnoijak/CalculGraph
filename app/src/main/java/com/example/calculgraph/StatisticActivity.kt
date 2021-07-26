package com.example.calculgraph

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.calculgraph.dataBase.DBHelper
import com.example.calculgraph.dataBase.StatisticState

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
        val statistic = DBHelper(this).read() ?: throw error("No statistic in the db")
        findViewById<TextView>(R.id.kolGame).text = "${statistic.kolGame}"
        findViewById<TextView>(R.id.sredScore).text = "${statistic.sredScore}"
        findViewById<TextView>(R.id.maxScore).text = "${statistic.maxScore}"
    }
}