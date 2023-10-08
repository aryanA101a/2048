package com.aryan.a2048.data.model

import android.util.Log
import com.aryan.a2048.util.Utils
import kotlin.random.Random

typealias Score = Int

class Board {
    data class State(
        val cellMatrix: MutableList<MutableList<Cell>>,
        val unfilledCellsIndices: HashSet<Int>,
    )

    data class Move(val state: State, val score: Int)


    fun placeRandomCell(boardState: State): State {
        val number = if (Random.nextDouble() >= 0.1) 2 else 4
        var index: Int
        val newBoardState = State(
            cellMatrix = boardState.cellMatrix.map { it.toMutableList() }.toMutableList(),
            unfilledCellsIndices = boardState.unfilledCellsIndices.toHashSet(),
        )
        if (newBoardState.unfilledCellsIndices.isNotEmpty()) {
            index = newBoardState.unfilledCellsIndices.random()
            newBoardState.unfilledCellsIndices.remove(index)

            Utils.idxToCoordinates(index).also { (row, col) ->
                newBoardState.cellMatrix[row][col] =
                    newBoardState.cellMatrix[row][col].copy(value = number)
            }

            Log.i(
                "TAG",
                "placeRandomCell:\n ${
                    newBoardState.cellMatrix.toString()
                }"
            )

        }
        return newBoardState
    }

    private fun slideLeft(row: List<Cell>): Pair<MutableList<Cell>, Int> {
//        Log.i("TAG", "slideLeft: ${row}")
        var score: Int = 0
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
                score += temp[i].value + row[j].value
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
        return Pair(temp, score)
    }

    fun leftMove(boardState: State): Move {
        var newCellMatrix = boardState.cellMatrix.map { it.toMutableList() }.toMutableList()

        var score: Int = 0
        for (rowIdx in newCellMatrix.indices) {
            slideLeft(newCellMatrix[rowIdx]).also {
                newCellMatrix[rowIdx] = it.first
                score += it.second
            }
        }

        val newUnfilledCellIndices = updateUnfilledCells(
            State(
                cellMatrix = newCellMatrix,
                unfilledCellsIndices = boardState.unfilledCellsIndices
            )
        )

        return Move(
            state = State(
                cellMatrix = newCellMatrix,
                unfilledCellsIndices = newUnfilledCellIndices,
            ),
            score = score
        )
    }

    fun rightMove(boardState: State): Move {
        var newCellMatrix = boardState.cellMatrix.map { it.toMutableList() }.toMutableList()

        repeat(2) {
            newCellMatrix = rotateMatrix(newCellMatrix, clockwise = false)
        }

        var score: Int = 0
        for (rowIdx in newCellMatrix.indices) {
            slideLeft(newCellMatrix[rowIdx]).also {
                newCellMatrix[rowIdx] = it.first
                score += it.second
            }

        }

        repeat(2) {
            newCellMatrix = rotateMatrix(newCellMatrix)
        }
        val newUnfilledCellIndices = updateUnfilledCells(
            State(
                cellMatrix = newCellMatrix,
                unfilledCellsIndices = boardState.unfilledCellsIndices
            )
        )

        return Move(
            state = State(
                cellMatrix = newCellMatrix,
                unfilledCellsIndices = newUnfilledCellIndices,
            ),
            score = score
        )

    }

    fun upMove(boardState: State): Move {
        var newCellMatrix = boardState.cellMatrix.map { it.toMutableList() }.toMutableList()

        newCellMatrix = rotateMatrix(newCellMatrix, clockwise = false)

        var score: Int = 0
        for (rowIdx in newCellMatrix.indices) {
            slideLeft(newCellMatrix[rowIdx]).also {
                newCellMatrix[rowIdx] = it.first
                score += it.second
            }

        }

        newCellMatrix = rotateMatrix(newCellMatrix)

        val newUnfilledCellIndices = updateUnfilledCells(
            State(
                cellMatrix = newCellMatrix,
                unfilledCellsIndices = boardState.unfilledCellsIndices
            )
        )

        return Move(
            state = State(
                cellMatrix = newCellMatrix,
                unfilledCellsIndices = newUnfilledCellIndices,
            ),
            score = score
        )

    }

    fun downMove(boardState: State): Move {
        var newCellMatrix = boardState.cellMatrix.map { it.toMutableList() }.toMutableList()

        repeat(3) {
            newCellMatrix = rotateMatrix(newCellMatrix, clockwise = false)
        }

        var score: Int = 0
        for (rowIdx in newCellMatrix.indices) {
            slideLeft(newCellMatrix[rowIdx]).also {
                newCellMatrix[rowIdx] = it.first
                score += it.second
            }

        }

        repeat(3) {
            newCellMatrix = rotateMatrix(newCellMatrix)
        }


        val newUnfilledCellIndices = updateUnfilledCells(
            State(
                cellMatrix = newCellMatrix,
                unfilledCellsIndices = boardState.unfilledCellsIndices
            )
        )

        return Move(
            state = State(
                cellMatrix = newCellMatrix,
                unfilledCellsIndices = newUnfilledCellIndices,
            ),
            score = score
        )

    }

    fun updateUnfilledCells(boardState: State): HashSet<Int> {

        val newUnfilledCells = boardState.unfilledCellsIndices.toHashSet()


        boardState.cellMatrix.flatten().onEachIndexed { index, cell ->

            if (cell.value != 0)
                newUnfilledCells.remove(index)
            else newUnfilledCells.add(
                index
            )
        }

        return newUnfilledCells

    }

    private fun rotateMatrix(
        matrix: MutableList<MutableList<Cell>>,
        clockwise: Boolean = true
    ): MutableList<MutableList<Cell>> {
        val newCellMatrix = matrix.map { it.toMutableList() }.toMutableList()

        for (i in newCellMatrix.indices) {
            for (j in i until newCellMatrix.size) {
                val temp = newCellMatrix[i][j]
                newCellMatrix[i][j] = newCellMatrix[j][i]
                newCellMatrix[j][i] = temp
            }
        }

        if (clockwise) {
            for (row in newCellMatrix) {
                row.reverse()
            }
        } else {
            for (j in newCellMatrix.indices) {
                for (i in 0 until newCellMatrix.size / 2) {
                    val temp = newCellMatrix[i][j]
                    newCellMatrix[i][j] = newCellMatrix[newCellMatrix.size - i - 1][j]
                    newCellMatrix[newCellMatrix.size - i - 1][j] = temp
                }
            }
        }
        return newCellMatrix
    }


}