package com.example.calculgraph.states

import com.example.calculgraph.enums.Computability
import com.example.calculgraph.enums.Theme

data class SettingsState(var sound: Boolean,
                         var language: String,
                         var theme: Theme,
                         var computability: Computability,
                         var moves: Int,
                         var time: Int): State()                                                    //in s
