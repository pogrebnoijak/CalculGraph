package com.example.calculgraph.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.calculgraph.R
import kotlin.system.exitProcess


class MainActivity : AnyActivity() {
//    var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
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
            val dialog = Dialog(this@MainActivity)
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
        findViewById<Button>(R.id.continue_).setOnClickListener {
            val intent = Intent(this, GameActivity :: class.java )
            startActivity(intent)
            finish()
        }

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
        startActivity(intent)
        finish()
    }
}
