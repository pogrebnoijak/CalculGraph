package com.example.calculgraph.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.format.DateUtils.SECOND_IN_MILLIS
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import com.example.calculgraph.R
import com.example.calculgraph.constant.*
import com.example.calculgraph.dataBase.DBHelper
import com.example.calculgraph.dataBase.DBWorker
import com.example.calculgraph.enums.*
import com.example.calculgraph.enums.Computability.*
import com.example.calculgraph.enums.Sounds.*
import com.example.calculgraph.helpers.LanguageHelper.computabilityTranslation
import com.example.calculgraph.helpers.LanguageHelper.computabilityUnTranslation
import com.example.calculgraph.helpers.LanguageHelper.themeTranslation
import com.example.calculgraph.helpers.LanguageHelper.themeUnTranslation
import com.example.calculgraph.helpers.SoundPoolHelper.playSound
import com.example.calculgraph.helpers.SpinnerHelper.getIndexByName
import com.example.calculgraph.helpers.TimeWorking.showTime
import com.example.calculgraph.helpers.TimeWorking.toTime
import com.example.calculgraph.service.GraphGeneratorService.Companion.updatePreGen
import com.example.calculgraph.states.SettingsState
import java.util.*


class SettingsActivity : AnyActivity() {
    companion object {
        fun initSettings(baseContext: Context) {
            setLanguage(baseContext)
        }

        private fun setLanguage(baseContext: Context) {
            val locale = when (settings.language) {
                "Русский" -> Locale("ru")
                "English" -> Locale("en")
                else -> throw error("wrong language")
            }
            Configuration().apply {
                setLocale(locale)
                baseContext.resources.updateConfiguration(this, null)
            }
        }
    }
    private var configuring = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setButtons()
        setOther()
        setSettings()
        handler.postDelayed({ configuring = false }, SECOND_IN_MILLIS / 2)
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            playSound(MENU)
            val intent = Intent(this, MainActivity :: class.java )
            updateSettings(intent)
        }
    }

    private fun updateSettings(intent: Intent) {
        val dbWorker = DBWorker()
        dbWorker.init(this)
        dbWorker.updateSettings()
        updatePreGen(this@SettingsActivity) {
            startActivity(intent, transitionActivity.toBundle())
            finish()
        }
    }

    private fun setOther() {
        fun tuningSpinner(id: Int, list: Array<Any>, update: (String) -> Unit) {
            findViewById<Spinner>(id).apply {
                adapter = ArrayAdapter(this@SettingsActivity, R.layout.spinner, R.id.sp, list)
                onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (!configuring) playSound(SHIFT)
                        update(list[position].toString())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }

        tuningSpinner(R.id.language, Array(LANGUAGES.size) { LANGUAGES[it] } ) { updateLanguage(it) }
        tuningSpinner(R.id.theme, Array(topicValues().size) { topicValues()[it].themeTranslation(this) } ) { updateTopic(it.themeUnTranslation(this)) }
        tuningSpinner(R.id.computability, Array(Computability.values().size) {
            Computability.values()[it].toString().computabilityTranslation(this) } ) { updateComputability(it.computabilityUnTranslation(this)) }
        findViewById<EditText>(R.id.moves).apply {
            doAfterTextChanged { text ->
                text.toString().let {
                    if(it.isNotEmpty()) when {
                        it.toInt() > MAX_MOVES  -> setText(MAX_MOVES.toString())
                        it.toInt() < MIN_MOVES  -> setText(MIN_MOVES.toString())
                        else                    -> {
                            if (!configuring) playSound(SHIFT)
                            updateMoves(it.toInt())
                        }
                    }
                }
            }
        }
        tuningSpinner(R.id.time, Array(TIMES.size) { showTime(TIMES[it], this) }) { updateTime(it) }
        findViewById<Switch>(R.id.sound).setOnCheckedChangeListener {
                _: CompoundButton, isChecked: Boolean ->
            updateSound(isChecked)
            if (!configuring) playSound(SHIFT)
        }
        setTransitionActivity(R.id.settingsAll)
    }

    private fun setSettings() {
        val (sound, language, theme, computability, moves, time) = (DBHelper(this).read("settings") ?: throw error("No settings in the db")) as SettingsState

        updateSound(false)
        findViewById<Spinner>(R.id.language).let {
            it.setSelection(getIndexByName(it, language))
        }
        findViewById<Spinner>(R.id.theme).let {
            it.setSelection(getIndexByName(it, theme.thToString().themeTranslation(this)))
        }
        findViewById<Spinner>(R.id.computability).let {
            it.setSelection(getIndexByName(it, computability.toString().computabilityTranslation(this)))
        }
        findViewById<EditText>(R.id.moves).setText(moves.toString())
        findViewById<Spinner>(R.id.time).let {
            it.setSelection(getIndexByName(it, showTime(time, this)))
        }
        findViewById<Switch>(R.id.sound).isChecked = sound
    }

    private fun updateLanguage(language: String) {
        val needUpdate = (settings.language != language)
        settings.language = language
        setLanguage(baseContext)
        if (needUpdate) recreate()
    }

    private fun updateSound(sound: Boolean) {
        settings.sound = sound
    }

    private fun updateTopic(theme: String) {
        val newTheme = theme.toTheme()
        val needUpdate = (settings.theme != newTheme)
        settings.theme = newTheme
        if (needUpdate) recreate()
    }

    private fun updateComputability(compatibility: String) {
        settings.computability = compatibility.toComputability()
    }

    private fun updateTime(time: String) {
        settings.time = time.toTime(this)
    }

    private fun updateMoves(moves: Int) {
        settings.moves = moves
    }

    override fun recreate() = updateSettings(Intent(this, SettingsActivity :: class.java ))
}
