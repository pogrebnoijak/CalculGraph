package com.example.calculgraph.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import com.example.calculgraph.R
import com.example.calculgraph.constant.*
import com.example.calculgraph.dataBase.DBHelper
import com.example.calculgraph.dataBase.DBWorker
import com.example.calculgraph.enums.*
import com.example.calculgraph.enums.Computability.*
import com.example.calculgraph.states.SettingsState


class SettingsActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_settings)
        setButtons()
        setOther()
        setSettings()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            val dbWorker = DBWorker()
            dbWorker.init(this)
            dbWorker.updateSettings()
            startActivity(intent)
            finish()
        }
    }

    private fun setOther() {
        fun tuningSpinner(rIdSmth: Int, list: Array<Any>, update: (String) -> Unit) {
            findViewById<Spinner>(rIdSmth).apply {
                adapter = ArrayAdapter(this@SettingsActivity, R.layout.spinner, R.id.sp, list)
                onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        update(list[position].toString())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }

        findViewById<Switch>(R.id.sound).setOnCheckedChangeListener {
                _: CompoundButton, isChecked: Boolean -> updateSound(isChecked)
        }
        tuningSpinner(R.id.language, LANGUAGES as Array<Any>) { updateLanguage(it) }
        tuningSpinner(R.id.topic, topicValues() as Array<Any>) { updateTopic(it) }
        tuningSpinner(R.id.computability, Computability.values() as Array<Any>) { updateComputability(it) }
        findViewById<EditText>(R.id.moves).apply {
            doAfterTextChanged { text ->
                text.toString().let {
                    if(it.isNotEmpty()) {
                        if (it.toInt() > MAX_MOVES) {
                            setText(MAX_MOVES.toString())
                        } else {
                            updateMoves(it.toInt())
                        }
                    }
                }
            }
        }
        tuningSpinner(R.id.time, TIMES.map { showTime(it) }.toTypedArray() as Array<Any>) { updateTime(it) }
    }

    private fun setSettings() {
        val _settings: SettingsState = (DBHelper(this).read("settings") ?: throw error("No settings in the db")) as SettingsState

        fun getIndexByName(spin: Spinner, name: Any): Int {
            (0 until spin.count).forEach { i ->
                if (spin.getItemAtPosition(i) == name) return i
            }
            throw error("spinner error")
        }

        findViewById<Switch>(R.id.sound).isChecked = _settings.sound
        findViewById<Spinner>(R.id.language).let {
            it.setSelection(getIndexByName(it, _settings.language))
        }
        findViewById<Spinner>(R.id.topic).let {
            it.setSelection(getIndexByName(it, _settings.topic.topToString()))
        }
        findViewById<Spinner>(R.id.computability).let {
            it.setSelection(getIndexByName(it, _settings.computability))
        }
        findViewById<EditText>(R.id.moves).setText(_settings.moves.toString())
        findViewById<Spinner>(R.id.time).let {
            it.setSelection(getIndexByName(it, showTime(_settings.time)))
        }
    }

    private fun updateLanguage(language: String) {
        settings.language = language
//        TODO("finish this")
    }

    private fun updateSound(sound: Boolean) {
        settings.sound = sound
//        TODO("finish this")
    }

    private fun updateTopic(topic: String) {
        settings.topic = topic.toTopic()
//        TODO("finish this")
    }

    private fun updateComputability(compatibility: String) {
        settings.computability = compatibility.toComputability()
    }

    private fun updateTime(time: String) {
        settings.time = time.toTime()
    }

    private fun updateMoves(moves: Int) {
        settings.moves = moves
    }
}

private fun showTime(time: Int): String = when(time) {
    in 1 until 60 -> "${time}s"
    in 60 until 3600 -> {
        val minutes = time / 60
        val sec = time % 60
        if (sec == 0) "${minutes}m" else "${minutes}m ${sec}s"
    }
    3600 -> "1h"
    else -> throw error("wrong time game")
}

private fun String.toTime(): Int {
    data class Time(var h: Int = 0, var m: Int = 0, var s: Int = 0)
    val time = Time()
    this.split(" ").forEach {
        when(it.last()) {
            'h' -> time.h = it.dropLast(1).toInt()
            'm' -> time.m = it.dropLast(1).toInt()
            's' -> time.s = it.dropLast(1).toInt()
        }
    }
    return time.h * HOUR + time.m * MINUTE + time.s
}
