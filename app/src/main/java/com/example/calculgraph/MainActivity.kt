package com.example.calculgraph

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlin.system.exitProcess

class MainActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_main)
        setButtons()
    }

    override fun setButtons() {
        findViewById<View>(R.id.new_game).setOnClickListener {
            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.dialog_game_mode)
            dialog.show()
            dialog.findViewById<View>(R.id.standard).setOnClickListener {
                dialog.dismiss()
                startGame("standard")
            }
            dialog.findViewById<View>(R.id.max).setOnClickListener {
                dialog.dismiss()
                startGame("max")
            }
            dialog.findViewById<View>(R.id.set).setOnClickListener {
                dialog.dismiss()
                startGame("set")
            }
        }

        findViewById<View>(R.id.levels).setOnClickListener {
            val intent = Intent(this, LevelsActivity :: class.java )
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.statistic).setOnClickListener {
            val intent = Intent(this, StatisticActivity :: class.java )
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.settings).setOnClickListener {
            val intent = Intent(this, SettingsActivity :: class.java )
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.exit).setOnClickListener {
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
