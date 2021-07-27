package com.example.calculgraph.states

import com.example.calculgraph.enums.Computability
import com.example.calculgraph.enums.Topic

data class SettingsState(var language: String,
                         var sound: Boolean,
                         var topic: Topic,
                         var computability: Computability,
                         var time: Int): State()
