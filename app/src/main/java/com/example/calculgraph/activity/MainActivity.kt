package com.example.calculgraph.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.calculgraph.R
import com.example.calculgraph.activity.SettingsActivity.Companion.initSettings
import com.example.calculgraph.dataBase.DBHelper
import com.example.calculgraph.playField.Field
import com.example.calculgraph.states.PreGenerationState
import com.example.calculgraph.states.SettingsState
import kotlin.system.exitProcess


class MainActivity : AnyActivity() {
    companion object {
        private var startKol = 0
    }
//    var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (startKol == 0) firstStart()
        super.onCreate(savedInstanceState)
        startKol++
        setContentView(R.layout.activity_main)
        setButtons()
//        prefs = getSharedPreferences("com.example.calculgraph", MODE_PRIVATE);
    }

//    override fun onResume() {
//        super.onResume()
//        if (prefs?.getBoolean("firstRun", true) == true) {  // executed once after update
//            prefs?.edit()?.putBoolean("firstRun", false)?.apply()
//
//        }
//    }

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
            }
        }

        findViewById<Button>(R.id.continue_).setOnClickListener { continueGame() }
        findViewById<Button>(R.id.levels).setOnClickListener {
            val intent = Intent(this, LevelsActivity :: class.java )
            startActivity(intent)
            finish()
        }
        findViewById<Button>(R.id.statistic).setOnClickListener {
            val intent = Intent(this, StatisticActivity :: class.java )
            startActivity(intent)
            finish()
        }
        findViewById<Button>(R.id.settings).setOnClickListener {
            val intent = Intent(this, SettingsActivity :: class.java )
            startActivity(intent)
            finish()
        }
        findViewById<Button>(R.id.exit).setOnClickListener {
            finish()
            exitProcess(0)
        }
    }

    private fun startGame(str: String) {
        val intent = Intent(this, WaitActivity :: class.java )
        intent.putExtra("mode", str)
        intent.putExtra("isNewGame", true)
        startActivity(intent)
        finish()
    }

    private fun continueGame() {
        val intent = Intent(this, WaitActivity :: class.java )
        intent.putExtra("mode", "standard")                                             // default mode if not start before
        intent.putExtra("isNewGame", false)
        startActivity(intent)
        finish()
    }

    private fun firstStart() {
        settings = (DBHelper(this).read("settings") ?: throw error("No settings in the db")) as SettingsState
        initSettings(baseContext)
        preGen = PreGenerationState(Field().apply { preparationField("any", this@MainActivity) })
    }
}
