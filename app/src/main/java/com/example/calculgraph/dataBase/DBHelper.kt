package com.example.calculgraph.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
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
        addDefaults(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    private fun addDefaults(db: SQLiteDatabase) {
        val cv = ContentValues()
        cv.put("kolGame", 0)
        cv.put("sredScore", 0.0)
        cv.put("maxScore", 0)
        db.insert("statistic", null, cv)
    }

    fun update(state : StatisticState) {
        val cv = ContentValues()
        val db = writableDatabase
        Log.d(LOG_TAG, "--- Insert in statistic: ---")

        cv.put("kolGame", state.kolGame)
        cv.put("sredScore", state.sredScore)
        cv.put("maxScore", state.maxScore)

        val rowID = db.update("statistic",  cv, null, null)
        Log.d(LOG_TAG, "row inserted, ID = $rowID")
        close()
    }

    fun read() : StatisticState? {
        val db = writableDatabase
        val state : StatisticState?
        Log.d(LOG_TAG, "--- Rows in statistic: ---")
        val cv = db.query("statistic", arrayOf("id",
            "kolGame",
            "sredScore",
            "maxScore"), null, null, null, null, null)

        state = if (cv.moveToNext()) {
            StatisticState(cv.getInt(cv.getColumnIndex("kolGame")),
                cv.getDouble(cv.getColumnIndex("sredScore")),
                cv.getInt(cv.getColumnIndex("maxScore")))
        } else {
            null
        }

        cv.close()
        db.close()
        return state
    }

//    fun clear(){
//        val db = this.writableDatabase
//
//        Log.d(LOG_TAG, "--- Clear statistic: ---")
//        val clearCount = db.delete("statistic", null, null)
//        Log.d(LOG_TAG, "deleted rows count = $clearCount")
//
//        db.close()
//    }
}