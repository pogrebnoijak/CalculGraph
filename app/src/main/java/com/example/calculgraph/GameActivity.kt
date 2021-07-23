package com.example.calculgraph

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

//import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_game)
        setButtons()
        getIntents()
    }

    override fun setButtons() {
        findViewById<View>(R.id.back).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            startActivity(intent)
            finish()
        }
    }

    private fun getIntents() {
        val a = intent.getStringExtra("mode")
        (findViewById<View>(R.id.some) as TextView).text = a
    }
}