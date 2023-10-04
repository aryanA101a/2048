package com.example.a2048.util

object Utils {
    fun idxToCoordinates(idx: Int) = Pair(first = idx / 4, second = idx % 4)
}

enum class Direction { UP, DOWN, RIGHT, LEFT }
enum class MoveOutcome { WON, LOST, NOTHING, }