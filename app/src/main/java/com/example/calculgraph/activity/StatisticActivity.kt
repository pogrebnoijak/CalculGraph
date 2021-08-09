package com.example.calculgraph.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils.SECOND_IN_MILLIS
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.calculgraph.R
import com.example.calculgraph.constant.*
import com.example.calculgraph.dataBase.DBHelper
import com.example.calculgraph.enums.Computability
import com.example.calculgraph.enums.Sounds.*
import com.example.calculgraph.helpers.LanguageHelper.computabilityTranslation
import com.example.calculgraph.helpers.LanguageHelper.modeTranslation
import com.example.calculgraph.helpers.Size
import com.example.calculgraph.helpers.SoundPoolHelper.playSound
import com.example.calculgraph.helpers.TimeWorking.showTime
import com.example.calculgraph.states.SaveState
import com.example.calculgraph.states.StatisticState


class StatisticActivity : AnyActivity() {
    private lateinit var dbHelper: DBHelper

    private val size: Size
        get() {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            return Size(metrics.widthPixels * 1F, metrics.heightPixels * 1F)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)
        setTransitionActivity(R.id.statisticAll)
        setButtons()
        setStatistic()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            playSound(MENU)
            val intent = Intent(this, MainActivity :: class.java )
            startActivity(intent, transitionActivity.toBundle())
            finish()
        }
    }

    private fun setStatistic() {
        dbHelper = DBHelper(this)
        val saveState: SaveState = (dbHelper.read("saveState") ?: throw error("No saveState in the db")) as SaveState
        updateStatistic(saveState)
    }

    private fun updateStatistic(saveState: SaveState) {
        val statistic: StatisticState = (dbHelper.read("statistic", saveState.generateId()) ?: throw error("No statistic in the db")) as StatisticState
        findViewById<TextView>(R.id.kolGame).text = "${statistic.kolGame}"
        findViewById<TextView>(R.id.sredScore).text = String.format("%.3f", statistic.sredScore)
        findViewById<TextView>(R.id.maxScore).text = "${statistic.maxScore}"
        findViewById<ImageButton>(R.id.info).setOnClickListener {
            playSound(TO)
            doDialogInfo(saveState)
        }
    }

    private fun doDialogInfo(saveState: SaveState) {
        Dialog(this@StatisticActivity, R.style.AlertDialogCustom).apply {
            setContentView(R.layout.dialog_info)
            val modeMax = MODES.size
            val compatibilityMax = Computability.values().size
            val movesMax = MAX_MOVES - MIN_MOVES + 1
            val timeMax = TIMES.size
            var modeInd = MODES.indexOf(saveState.mode)
            var compInd = Computability.values().indexOf(saveState.computability)
            var movesInd = saveState.answer.size - 1 - MIN_MOVES
            var timeInd = TIMES.indexOf((saveState.allTime / SECOND_IN_MILLIS).toInt())

            fun updateMode() = run { findViewById<TextView>(R.id.mode).text = MODES[modeInd].modeTranslation(this@StatisticActivity) }
            fun updateComp() = run { findViewById<TextView>(R.id.computability).text = Computability.values()[compInd].toString().computabilityTranslation(this@StatisticActivity) }
            fun updateMoves() = run { findViewById<TextView>(R.id.moves).text = getString(R.string.updateMoves, movesInd + MIN_MOVES) }
            fun updateTime() = run { findViewById<TextView>(R.id.time).text = showTime(TIMES[timeInd], this@StatisticActivity) }
            fun setListener(ind: String, id: Int, isNext: Boolean) {
                findViewById<ImageButton>(id).setOnClickListener {
                    playSound(SHIFT)
                    val plus = if (isNext) 1 else -1
                    when(ind) {
                        "mode" -> { modeInd = (modeInd + plus + modeMax) % modeMax; updateMode() }
                        "computability" -> { compInd = (compInd + plus + compatibilityMax) % compatibilityMax; updateComp() }
                        "moves" -> { movesInd = (movesInd + plus + movesMax) % movesMax; updateMoves() }
                        "time" -> { timeInd = (timeInd + plus + timeMax) % timeMax; updateTime() }
                    }
                }
            }

            updateMode()
            updateComp()
            updateMoves()
            updateTime()

            setListener("mode", R.id.nextMode, true)
            setListener("mode", R.id.prevMode, false)
            setListener("computability", R.id.nextComputability, true)
            setListener("computability", R.id.prevComputability, false)
            setListener("moves", R.id.nextMoves, true)
            setListener("moves", R.id.prevMoves, false)
            setListener("time", R.id.nextTime, true)
            setListener("time", R.id.prevTime, false)

            findViewById<Button>(R.id.ok).setOnClickListener {
                playSound(TO)
                dismiss()
                saveState.apply {
                    mode = MODES[modeInd]
                    computability = Computability.values()[compInd]
                    kolMoves = movesInd + MIN_MOVES
                    allTime = TIMES[timeInd] * SECOND_IN_MILLIS
                }
                updateStatistic(saveState)
            }
            show()
            window?.setLayout((size.width * WIDTH_DIALOG_K).toInt(), (size.height * HEIGHT_DIALOG_STATISTIC).toInt())
        }
    }
}
