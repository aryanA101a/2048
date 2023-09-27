package com.example.a2048.model

import android.util.Log
import com.example.a2048.util.Util
import kotlin.random.Random

class GameBoard {
    var state: List<List<Cell>>
    private val unfilledCells: HashSet<Int>

    init {
        state = List<List<Cell>>(4) { rowIdx ->
            List<Cell>(4) { colIdx ->
                Cell(
                    0, rowIdx * 4 + colIdx
                )
            }
        }
        unfilledCells = (0..15).toHashSet()
        placeRandomCell()
        placeRandomCell()

    }


    fun placeRandomCell() {
        val number = if (Random.nextDouble() >= 0.1) 2 else 4
        var index: Int

        if (unfilledCells.isNotEmpty()) {
            index = unfilledCells.random()
            unfilledCells.remove(index)
            Util.idxToCoordinates(index).also {
                val newBoardState = state.map { it -> it.toMutableList() }.toMutableList()
                newBoardState[it.first][it.second] =
                    newBoardState[it.first][it.second].copy(value = number)
                state = newBoardState
            }
            Log.i("TAG", "placeRandomCell: ${unfilledCells}")
            Log.i("TAG", "placeRandomCell: ${state}")


        }
    }

    fun slideLeft(row: List<Cell>): MutableList<Cell> {
        Log.i("TAG", "slideLeft: ${row}")
        val temp = MutableList<Cell>(4) { i ->
            Cell(
                0,
                row[i].id
            )
        }
        var i = 0
        var j = 0
        while (j <= 3) {
            if (row[j].value == 0) {
                j++
            } else if (row[j].value != 0 && temp[i].value == 0) {
                val ithId = temp[i].id
                temp[i] = temp[i].copy(
                    value = row[j].value,
                    id = temp[j].id
                )
                temp[j] = temp[j].copy(id = ithId)
                j++
            } else if (row[j].value == temp[i].value) {
                val ithId = temp[i].id
                temp[i] = temp[i].copy(
                    value = temp[i].value + row[j].value,
                    id = temp[j].id
                )
                temp[j] = temp[j].copy(id = ithId)
                i++
                j++
            } else {
                i++
            }
        }
        Log.i("TAG", "slideLeft: ${temp}")

        return temp
    }

    fun leftMove(): Boolean {
        var newBoardState = state.map { it.toMutableList() }.toMutableList()
        for (rowIdx in newBoardState.indices) {
            newBoardState[rowIdx] = slideLeft(newBoardState[rowIdx])
        }

        return updateBoardState(newBoardState)
    }

    private fun updateBoardState(newBoardState: MutableList<MutableList<Cell>>): Boolean {
        fun checkEquality(): Boolean {
            for (i in state.indices) {
                for (j in state.first().indices) {
                    if (state[i][j].value != newBoardState[i][j].value) return false
                }
            }
            return true
        }

        if (!checkEquality()) {
            state = newBoardState
            state.flatten().onEachIndexed { index, cell ->
                if (cell.value != 0) unfilledCells.remove(index) else unfilledCells.add(
                    index
                )

            }
//            Log.i("TAG", "update: ${unfilledCells}")


            return true
        }
        return false
    }


    fun rightMove(): Boolean {
        var newBoardState = state.map { it.toMutableList() }.toMutableList()

        repeat(2) {
            newBoardState = rotateMatrix(newBoardState, clockwise = false)
        }

        for (rowIdx in newBoardState.indices) {
            newBoardState[rowIdx] = slideLeft(newBoardState[rowIdx])
        }

        repeat(2) {
            newBoardState = rotateMatrix(newBoardState)
        }

        return updateBoardState(newBoardState)

    }
    fun upMove(): Boolean {
        var newBoardState = state.map { it.toMutableList() }.toMutableList()

            newBoardState = rotateMatrix(newBoardState, clockwise = false)

        for (rowIdx in newBoardState.indices) {
            newBoardState[rowIdx] = slideLeft(newBoardState[rowIdx])
        }

            newBoardState = rotateMatrix(newBoardState)

        return updateBoardState(newBoardState)

    }
    fun downMove(): Boolean {
        var newBoardState = state.map { it.toMutableList() }.toMutableList()

        repeat(3) {
            newBoardState = rotateMatrix(newBoardState, clockwise = false)
        }

        for (rowIdx in newBoardState.indices) {
            newBoardState[rowIdx] = slideLeft(newBoardState[rowIdx])
        }

        repeat(3) {
            newBoardState = rotateMatrix(newBoardState)
        }

        return updateBoardState(newBoardState)

    }
    private fun rotateMatrix(
        matrix: MutableList<MutableList<Cell>>,
        clockwise: Boolean = true
    ): MutableList<MutableList<Cell>> {
        val newMatrix = matrix.map { it.toMutableList() }.toMutableList()

        for (i in newMatrix.indices) {
            for (j in i until newMatrix.size) {
                val temp = newMatrix[i][j]
                newMatrix[i][j] = newMatrix[j][i]
                newMatrix[j][i] = temp
            }
        }

        if (clockwise) {
            for (row in newMatrix) {
                row.reverse()
            }
        } else {
            for (j in newMatrix.indices) {
                for (i in 0 until newMatrix.size / 2) {
                    val temp = newMatrix[i][j]
                    newMatrix[i][j] = newMatrix[newMatrix.size - i - 1][j]
                    newMatrix[newMatrix.size - i - 1][j] = temp
                }
            }
        }
        return newMatrix
    }

}