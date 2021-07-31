package com.example.calculgraph.states

import com.example.calculgraph.enums.Operation
import com.example.calculgraph.enums.opToString

class Inscription(val oper: Operation, val num: Int?) {
    override fun toString() = oper.opToString() + num.toString()
}