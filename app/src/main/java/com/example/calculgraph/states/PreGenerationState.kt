package com.example.calculgraph.states

import com.example.calculgraph.playField.Field
import java.util.concurrent.CountDownLatch

data class PreGenerationState(var filed: Field,
                              var data: MutableList<MutableList<Inscription>> = mutableListOf(),
                              var possibleNumbers: List<Int> = listOf(),
                              var actual: Boolean = false,
                              var latch: CountDownLatch = CountDownLatch(1))
