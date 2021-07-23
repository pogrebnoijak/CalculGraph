package com.example.calculgraph

import android.content.Intent
import android.os.Bundle
import android.view.View

//import kotlinx.android.synthetic.main.activity_statistic.*

class StatisticActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_statistic)
        setButtons()
    }

    override fun setButtons() {
        findViewById<View>(R.id.back).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            startActivity(intent)
            finish()
        }
    }
}