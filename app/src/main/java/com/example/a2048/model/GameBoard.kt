package com.example.a2048.model

import android.util.Log
import com.example.a2048.util.MoveOutcome
import com.example.a2048.util.Direction
import com.example.a2048.util.Utils
import kotlin.random.Random

class GameBoard {
    data class GameBoardState(
        val cellMatrix: MutableList<MutableList<Cell>>,
        val unfilledCells: HashSet<Int>,
        val score: Int,
        val result: MoveOutcome
    )

    private lateinit var _state: GameBoardState
    val state: GameBoardState
        get() = _state


    private lateinit var prevState: GameBoardState

    companion object {
        const val winningCellValue: Int = 2048
    }

    init {

        initState()
    }


    private fun placeRandomCell() {
        val number = if (Random.nextDouble() >= 0.1) 2 else 4
        var index: Int
        val newBoardState = _state.copy(
            cellMatrix = _state.cellMatrix.map { it.toMutableList() }.toMutableList(),
            unfilledCells = _state.unfilledCells.toHashSet(),
        )
        if (newBoardState.unfilledCells.isNotEmpty()) {
            index = newBoardState.unfilledCells.random()
            newBoardState.unfilledCells.remove(index)

            Utils.idxToCoordinates(index).also { (row, col) ->
                newBoardState.cellMatrix[row][col] =
                    newBoardState.cellMatrix[row][col].copy(value = number)
            }
            _state = newBoardState

            Log.i("TAG", "placeRandomCell:\n ${state.cellMatrix.joinToString("\n") { it.map { e->e.value }.joinToString("\t") }}")

        }
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


    private fun isLoosing(): Boolean {

        if (state.unfilledCells.size == 0) {
            Log.i("Losing", "isLoosing:\n ${state.cellMatrix.joinToString("\n") { it.map { e->e.value }.joinToString("\t") }}")
            val allMoves = listOf(
                upMove().cellMatrix,
                downMove().cellMatrix,
                rightMove().cellMatrix,
                leftMove().cellMatrix
            )
            allMoves.onEachIndexed() { i,m->Log.i("Losing", "allMoves[$i]:\n ${m.joinToString("\n") { it.map { e->e.value }.joinToString("\t") }}") }
            return ((allMoves[0] == allMoves[1]) &&
                    (allMoves[1] == allMoves[2]) &&
                    (allMoves[2] == allMoves[3]))

        }
        return false
    }


    fun move(direction: Direction): Move {
        var newBoardState = when (direction) {
            Direction.UP -> upMove()
            Direction.DOWN -> downMove()
            Direction.RIGHT -> rightMove()
            Direction.LEFT -> leftMove()
        }

        var outcome: MoveOutcome = MoveOutcome.NOTHING
        if (newBoardState != _state) {
            prevState = _state
            newBoardState.cellMatrix.flatten().onEachIndexed { index, cell ->
                if (cell.value == winningCellValue && state.result!=MoveOutcome.WON) {
                    newBoardState = newBoardState.copy(result = MoveOutcome.WON)
                    outcome = MoveOutcome.WON
                }

                if (cell.value != 0)
                    newBoardState.unfilledCells.remove(index)
                else newBoardState.unfilledCells.add(
                    index
                )

            }


            _state = newBoardState
            placeRandomCell()
            if (isLoosing()) {
                _state = _state.copy(result = MoveOutcome.LOST)
                outcome = MoveOutcome.LOST
            }
            Log.i("TAG", "move: ${state.score} ${state.result}")
            return Move(successful = true, outcome = outcome)
        }
        return Move(successful = false, outcome = outcome)
    }

    private fun leftMove(): GameBoardState {
        var newCellMatrix = _state.cellMatrix.map { it.toMutableList() }.toMutableList()

        var score: Int = 0
        for (rowIdx in newCellMatrix.indices) {
            slideLeft(newCellMatrix[rowIdx]).also {
                newCellMatrix[rowIdx] = it.first
                score += it.second
            }
        }

        return state.copy(
            cellMatrix = newCellMatrix,
            unfilledCells = _state.unfilledCells.toHashSet(),
            score = _state.score + score,
        )
    }

    private fun rightMove(): GameBoardState {
        var newCellMatrix = _state.cellMatrix.map { it.toMutableList() }.toMutableList()

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
        return state.copy(
            cellMatrix = newCellMatrix,
            unfilledCells = _state.unfilledCells.toHashSet(),
            score = _state.score + score,
        )


    }

    private fun upMove(): GameBoardState {
        var newCellMatrix = _state.cellMatrix.map { it.toMutableList() }.toMutableList()

        newCellMatrix = rotateMatrix(newCellMatrix, clockwise = false)

        var score: Int = 0
        for (rowIdx in newCellMatrix.indices) {
            slideLeft(newCellMatrix[rowIdx]).also {
                newCellMatrix[rowIdx] = it.first
                score += it.second
            }

        }

        newCellMatrix = rotateMatrix(newCellMatrix)

        return state.copy(
            cellMatrix = newCellMatrix,
            unfilledCells = _state.unfilledCells.toHashSet(),
            score = _state.score + score,
        )


    }

    private fun downMove(): GameBoardState {
        var newCellMatrix = _state.cellMatrix.map { it.toMutableList() }.toMutableList()

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

        return state.copy(
            cellMatrix = newCellMatrix,
            unfilledCells = _state.unfilledCells.toHashSet(),
            score = _state.score + score,
        )


    }


    fun undoState() {
        if (_state != prevState) {
            _state = prevState
        }
    }

    fun initState() {
        _state = GameBoardState(MutableList<MutableList<Cell>>(4) { rowIdx ->
            MutableList<Cell>(4) { colIdx ->
                Cell(
                    0, rowIdx * 4 + colIdx
                )
            }
        }, (0..15).toHashSet(), 0, MoveOutcome.NOTHING)
        placeRandomCell()
        placeRandomCell()
        prevState = _state
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