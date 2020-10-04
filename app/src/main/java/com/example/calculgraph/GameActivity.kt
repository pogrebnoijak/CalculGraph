package com.example.calculgraph

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_game)
        setButtons()
        getIntents()
    }

    override fun setButtons() {
        back.setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            finish()
            startActivity(intent)
        }
    }

    private fun getIntents() {
//        val a = intent.getStringExtra("mode")
//        back.text = a
    }
}