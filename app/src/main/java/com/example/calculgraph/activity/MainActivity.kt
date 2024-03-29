package com.example.calculgraph.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import com.example.calculgraph.R
import com.example.calculgraph.activity.SettingsActivity.Companion.initSettings
import com.example.calculgraph.constant.HEIGHT_DIALOG_MODE
import com.example.calculgraph.constant.WIDTH_DIALOG_K
import com.example.calculgraph.dataBase.DBHelper
import com.example.calculgraph.enums.Sounds.*
import com.example.calculgraph.helpers.Size
import com.example.calculgraph.helpers.SoundPoolHelper.playSound
import com.example.calculgraph.helpers.SoundPoolHelper.setSounds
import com.example.calculgraph.playField.Field
import com.example.calculgraph.states.PreGenerationState
import com.example.calculgraph.states.SettingsState
import kotlin.system.exitProcess


class MainActivity : AnyActivity() {
    companion object {
        private var startKol = 0
    }

    private val size: Size
        get() {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            return Size(metrics.widthPixels * 1F, metrics.heightPixels * 1F)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (startKol == 0) firstStart()
        super.onCreate(savedInstanceState)
        startKol++
        setContentView(R.layout.activity_main)
        setTransitionActivity(R.id.mainAll)
        setButtons()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.new_game).setOnClickListener {
            Dialog(this@MainActivity, R.style.AlertDialogCustom).apply {
                setContentView(R.layout.dialog_game_mode)
                findViewById<Button>(R.id.standard).setOnClickListener {
                    dismiss()
                    startGame("standard")
                }
                findViewById<Button>(R.id.max).setOnClickListener {
                    dismiss()
                    startGame("max")
                }
                findViewById<Button>(R.id.set).setOnClickListener {
                    dismiss()
                    startGame("set")
                }
                show()
                window?.setLayout((size.width * WIDTH_DIALOG_K).toInt(), (size.height * HEIGHT_DIALOG_MODE).toInt())
            }
        }

        findViewById<Button>(R.id.continue_).setOnClickListener {
            playSound(TO)
            continueGame()
        }
        findViewById<Button>(R.id.levels).setOnClickListener {
            playSound(TO)
            val intent = Intent(this, LevelsActivity :: class.java )
            startActivity(intent, transitionActivity.toBundle())
            finish()
        }
        findViewById<Button>(R.id.statistic).setOnClickListener {
            playSound(TO)
            val intent = Intent(this, StatisticActivity :: class.java )
            startActivity(intent, transitionActivity.toBundle())
            finish()
        }
        findViewById<Button>(R.id.settings).setOnClickListener {
            playSound(TO)
            val intent = Intent(this, SettingsActivity :: class.java )
            startActivity(intent, transitionActivity.toBundle())
            finish()
        }
        findViewById<Button>(R.id.exit).setOnClickListener {
            finish()
            exitProcess(0)
        }
    }

    private fun startGame(str: String) {
        playSound(TO)
        val intent = Intent(this, WaitActivity :: class.java )
        intent.putExtra("mode", str)
        intent.putExtra("isNewGame", true)
        startActivity(intent, transitionActivity.toBundle())
        finish()
    }

    private fun continueGame() {
        val intent = Intent(this, WaitActivity :: class.java )
        intent.putExtra("mode", "standard")                                             // default mode if not start before
        intent.putExtra("isNewGame", false)
        startActivity(intent, transitionActivity.toBundle())
        finish()
    }

    private fun firstStart() {
        Log.d(logTAG, "MainActivity: firstStart start")
        settings = (DBHelper(this).read("settings") ?: throw error("No settings in the db")) as SettingsState
        initSettings(baseContext)
        preGen = PreGenerationState(Field().apply { preparationField("any", this@MainActivity) })
        setSounds(this)
        Log.d(logTAG, "MainActivity: firstStart end")
    }
}
