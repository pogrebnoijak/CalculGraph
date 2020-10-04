package com.example.calculgraph

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_levels.*

class LevelsActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_levels)
        setButtons()
    }

    override fun setButtons() {
        back.setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            finish()
            startActivity(intent)
        }
    }
}