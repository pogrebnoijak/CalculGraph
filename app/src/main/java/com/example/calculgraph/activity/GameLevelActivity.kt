package com.example.calculgraph.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MotionEventCompat
import com.example.calculgraph.R
import com.example.calculgraph.constant.*
import com.example.calculgraph.dataBase.getGroupLevelId
import com.example.calculgraph.enums.GameState.*
import com.example.calculgraph.enums.Operation.*
import com.example.calculgraph.enums.*
import com.example.calculgraph.playField.Field
import java.util.*
import kotlin.math.*
import com.example.calculgraph.enums.Sounds.*
import com.example.calculgraph.helpers.SoundPoolHelper.playSound
import com.example.calculgraph.states.LastLevelsState


class GameLevelActivity : AnyGameActivity() {
    private var num = MAGIC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbWorker.initLevels(this)
        getIntents()
        startLevel()
    }

    override fun onTouchEvent(event: MotionEvent) = when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            touchDown = Pair(event.rawX, event.rawY)
            true
        }
        MotionEvent.ACTION_UP -> {
            treatment(touchDown, Pair(event.rawX, event.rawY))
            true
        }
        else -> super.onTouchEvent(event)
    }

    private fun getIntents() {
        mode = intent.getStringExtra("mode") ?: throw error("Wrong intent!")
        computability = intent.getStringExtra("computability")?.toComputability() ?: throw error("Wrong intent!")
        num = intent.getIntExtra("num", MAGIC)
    }

    private fun startLevel() {
        Log.d(logTAG, "GameLevelActivity: startLevel")
        val curLevel = dbWorker.getLevel(mode, computability, num)
        curField = Field(curLevel.kolMoves, KOL_NODES[computability] ?: throw error("wrong computability"))
        curField.set(curLevel.currentNode, curLevel.numbers, curLevel.totalNumbers, listOf(), listOf(), curLevel.data)
        sets()
    }

    override fun writeField() {
        super.writeField()
        findViewById<TextView>(R.id.score).text = getString(R.string.level, num)
    }

    override fun addDraws() {
        super.addDraws()
        background = DrawView(this@GameLevelActivity)
        findViewById<ConstraintLayout>(R.id.draw).addView(background)
    }

    override fun move(to: Int) {
        Log.d(logTAG, "GameLevelActivity: move")
        val win = curField.move(to)
        findViewById<TextView>(R.id.kolMoves).text = getString(R.string.updateMoves, curField.kolMoves)
        if (win) {
            Log.d(logTAG, "GameLevelActivity: win")
            playSound(WIN)
            endGame()
        }
    }

    override fun doDialogEnd() {
        Log.d(logTAG, "GameLevelActivity: doDialogEnd")
        Dialog(this@GameLevelActivity, R.style.AlertDialogCustom).apply {
            val params = window?.attributes ?: throw error("dialog error")
            params.y = -(size.height * DIALOG_K).toInt()
            setCanceledOnTouchOutside(false)
            setCancelable(false)

            setContentView(R.layout.dialog_yes_no)
            findViewById<TextView>(R.id.dial_text).text = getString(R.string.after_level)
            findViewById<Button>(R.id.yes).setOnClickListener {
                Log.d(logTAG, "GameLevelActivity: doDialogEnd -> yes")
                playSound(TO)
                dismiss()
                if (setNextLevel()) startLevel()
                else exitGame()
            }
            findViewById<Button>(R.id.no).setOnClickListener {
                Log.d(logTAG, "GameLevelActivity: doDialogEnd -> no")
                playSound(TO)
                dismiss()
                num++
                exitGame()
            }
            show()
            window?.let {
                it.attributes = params
                it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                it.setLayout((size.width * WIDTH_DIALOG_K).toInt(), (size.height * HEIGHT_DIALOG_YES_NO).toInt())
            }
        }
    }

    private fun setNextLevel() = num++ != KOL_LEVELS

    override fun exitGame() {
        Log.d(logTAG, "GameLevelActivity: exitGame")
        dbWorker.updateGroupsLevels(LastLevelsState(dbWorker.getGroupsLevels().list.apply {
            val id = getGroupLevelId(mode, computability)
            this[id] = max(this[id], num)
        }))
        val intent = Intent(this, LevelsActivity :: class.java )
        intent.putExtra("mode", mode)
        motion.cancel()
        timer.cancel()
        startActivity(intent, transitionActivity.toBundle())
        finish()
    }

    private inner class DrawView(context: Context?) : DrawHelper(context)  {
        override fun onDraw(canvas: Canvas) {
            drawText(canvas)

            drawCircles(canvas)

            drawPosCircle(canvas, centers[curField.currentNode])
        }
    }
}
