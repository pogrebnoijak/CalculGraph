package com.example.calculgraph.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.calculgraph.R
import com.example.calculgraph.dataBase.DBHelper
import com.example.calculgraph.states.SettingsState
import kotlin.system.exitProcess


class MainActivity : AnyActivity() {
    companion object {
        var startKol = 0
    }
//    var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        if (startKol == 0) firstStart()
        startKol++
        setContentView(R.layout.activity_main)
        setButtons()
//        prefs = getSharedPreferences("com.example.calculgraph", MODE_PRIVATE);
    }

//    override fun onResume() {
//        super.onResume()
//        if (prefs?.getBoolean("firstrun", true) == true) {  // executed once after update
//            prefs?.edit()?.putBoolean("firstrun", false)?.apply()
//
//        }
//    }

    override fun setButtons() {
        findViewById<Button>(R.id.new_game).setOnClickListener {
            val dialog = Dialog(this@MainActivity, R.style.AlertDialogCustom)
            dialog.setContentView(R.layout.dialog_game_mode)
            dialog.show()
            dialog.findViewById<Button>(R.id.standard).setOnClickListener {
                dialog.dismiss()
                startGame("standard")
            }
            dialog.findViewById<Button>(R.id.max).setOnClickListener {
                dialog.dismiss()
                startGame("max")
            }
            dialog.findViewById<Button>(R.id.set).setOnClickListener {
                dialog.dismiss()
                startGame("set")
            }
        }

//        TODO("update this")
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
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("mode", str)
        intent.putExtra("isNewGame", true)
        startActivity(intent)
        finish()
    }

    private fun continueGame() {
        val intent = Intent(this, GameActivity :: class.java )
        intent.putExtra("mode", "standard")                                             // default mode if not start before
        intent.putExtra("isNewGame", false)
        startActivity(intent)
        finish()
    }

    private fun firstStart() {
        settings = (DBHelper(this).read("settings") ?: throw error("No settings in the db")) as SettingsState
    }
}
