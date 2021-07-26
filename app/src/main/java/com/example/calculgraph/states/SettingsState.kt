package com.example.calculgraph.states

import com.example.calculgraph.enums.Computability

data class SettingsState(var language: String,
                         var sound: Boolean,
                         var topic: String,
                         var compatibility: Computability,
                         var time: Int)
