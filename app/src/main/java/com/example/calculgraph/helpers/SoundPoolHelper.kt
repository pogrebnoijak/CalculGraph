package com.example.calculgraph.helpers

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.Log
import com.example.calculgraph.R
import com.example.calculgraph.activity.AnyActivity.Companion.logTAG
import com.example.calculgraph.activity.AnyActivity.Companion.settings
import com.example.calculgraph.constant.MAGIC
import com.example.calculgraph.constant.MAX_STREAM
import com.example.calculgraph.enums.Sounds
import com.example.calculgraph.enums.Sounds.*


object SoundPoolHelper {
    private var soundIdONo = MAGIC
    private var soundIdLose = MAGIC
    private var soundIdMenu = MAGIC
    private var soundIdShift = MAGIC
    private var soundIdTap  = MAGIC
    private var soundIdTo  = MAGIC
    private var soundIdWin = MAGIC
    private lateinit var playerWaiting: MediaPlayer
    private val sp = doSoundPool()

    private fun doSoundPool(): SoundPool = SoundPool.Builder()
        .setAudioAttributes(
            AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build())
        .setMaxStreams(MAX_STREAM)
        .build()

    fun setSounds(context: Context, list: List<Sounds> = Sounds.values().toList()) {
        list.forEach {
            when(it) {
                O_NO    -> soundIdONo = sp.load(context, R.raw.o_no, 1)
                LOSE    -> soundIdLose = sp.load(context, R.raw.lose, 1)
                MENU    -> soundIdMenu = sp.load(context, R.raw.menu, 1)
                SHIFT   -> soundIdShift = sp.load(context, R.raw.shift, 1)
                TAP     -> soundIdTap = sp.load(context, R.raw.tap, 1)
                TO      -> soundIdTo = sp.load(context, R.raw.to, 1)
                WIN     -> soundIdWin = sp.load(context, R.raw.win, 1)
            }
        }
        playerWaiting = MediaPlayer.create(context, R.raw.waiting)
        playerWaiting.isLooping = true
    }

    fun playWaitingStart() {
        Log.d(logTAG, "SoundPoolHelper: playWaitingStart")
        if (!settings.sound || playerWaiting.isPlaying) return
        playerWaiting.seekTo(0)
        playerWaiting.start()
    }

    fun playWaitingPause() {
        Log.d(logTAG, "SoundPoolHelper: playWaitingPause")
        if (!settings.sound || !playerWaiting.isPlaying) return
        playerWaiting.pause()
    }

    fun playSound(sound: Sounds) {
        Log.d(logTAG, "SoundPoolHelper: playSound $sound")
        val soundId = soundToId(sound)
        if (!settings.sound || soundId == MAGIC) return
        sp.play(soundId, 1F, 1F, 0, 0, 1F)
    }

    private fun soundToId(sound: Sounds) = when(sound) {
        O_NO    -> soundIdONo
        LOSE    -> soundIdLose
        MENU    -> soundIdMenu
        SHIFT   -> soundIdShift
        TAP     -> soundIdTap
        TO      -> soundIdTo
        WIN     -> soundIdWin
    }
}