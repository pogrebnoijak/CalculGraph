package com.example.calculgraph.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.calculgraph.enums.toComputability
import com.example.calculgraph.states.SettingsState
import com.example.calculgraph.states.State
import com.example.calculgraph.states.StatisticState

class DBHelper(context: Context) : SQLiteOpenHelper(context, "DBHelper", null, 1) {
    private val LOG_TAG = "DBLog"

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(LOG_TAG, "--- onCreate statistic ---")
        db.execSQL(
            ("create table IF NOT EXISTS statistic ("
                    + "id integer primary key autoincrement,"
                    + "kolGame integer,"
                    + "sredScore double,"
                    + "maxScore integer" + ");")
        )

        Log.d(LOG_TAG, "--- onCreate settings ---")
        db.execSQL(
            ("create table IF NOT EXISTS settings ("
                    + "id integer primary key autoincrement,"
                    + "language text,"
                    + "sound integer,"
                    + "topic text,"
                    + "computability text,"
                    + "time integer" + ");")
        )
        addDefaults(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    private fun addDefaults(db: SQLiteDatabase) {
        val cvStat = ContentValues()
        cvStat.put("kolGame", 0)
        cvStat.put("sredScore", 0.0)
        cvStat.put("maxScore", 0)
        db.insert("statistic", null, cvStat)

        val cvSett = ContentValues()
        cvSett.put("language", "English")
        cvSett.put("sound", 1)
        cvSett.put("topic", "Standard")
        cvSett.put("computability", "EASY")
        cvSett.put("time", 60)
        db.insert("settings", null, cvSett)
    }

    fun update(state : State) {
        val cv = ContentValues()
        val db = writableDatabase
        var rowID = -1
        lateinit var tableName: String
        when (state) {
            is StatisticState -> {
                tableName = "statistic"
                Log.d(LOG_TAG, "--- Insert in $tableName: ---")

                cv.put("kolGame", state.kolGame)
                cv.put("sredScore", state.sredScore)
                cv.put("maxScore", state.maxScore)
            }

            is SettingsState -> {
                tableName = "settings"
                Log.d(LOG_TAG, "--- Insert in $tableName: ---")

                cv.put("language", state.language)
                cv.put("sound", state.sound)
                cv.put("topic", state.topic)
                cv.put("computability", state.computability.toString())
                cv.put("time", state.time)
            }
        }
        rowID = db.update(tableName, cv, null, null)
        Log.d(LOG_TAG, "row inserted, ID = $rowID")
        close()
    }

    fun read(table: String) : State? {
        val list: Array<String> = when(table) {
            "statistic" -> arrayOf("id", "kolGame", "sredScore", "maxScore")
            "settings"  -> arrayOf("id", "language", "sound", "topic", "computability", "time")
            else        -> throw error("wrong table name")
        }

        val db = writableDatabase
        val state: State?
        Log.d(LOG_TAG, "--- Rows in $table: ---")
        val cv = db.query(table, list, null, null, null, null, null)

        state = when(table) {
            "statistic" -> {
                if (cv.moveToNext()) {
                    StatisticState(cv.getInt(cv.getColumnIndex("kolGame")),
                        cv.getDouble(cv.getColumnIndex("sredScore")),
                        cv.getInt(cv.getColumnIndex("maxScore")))
                } else {
                    null
                }
            }
            "settings"  -> {
                if (cv.moveToNext()) {
                    SettingsState(cv.getString(cv.getColumnIndex("language")),
                        cv.getInt(cv.getColumnIndex("sound")) != 0,
                        cv.getString(cv.getColumnIndex("topic")),
                        cv.getString(cv.getColumnIndex("computability")).toComputability(),
                        cv.getInt(cv.getColumnIndex("time")))
                } else {
                    null
                }
            }
            else        -> throw error("wrong table name")
        }

        cv.close()
        db.close()
        return state
    }
}