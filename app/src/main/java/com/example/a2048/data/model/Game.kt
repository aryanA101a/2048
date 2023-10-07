package com.example.a2048.data.model

import android.util.Log
import com.example.a2048.util.MoveOutcome
import com.example.a2048.util.Direction
import javax.inject.Inject

class Game @Inject constructor(val board: Board) {

    data class State(
        val boardState: Board.State,
        val score: Int,
        val highScore: Int,
        val result: MoveOutcome
    )

    private var _state: State
    val state: State
        get() = _state


    private var _prevState: State
    val prevState: State
        get() = _prevState

    companion object {
        const val winningCellValue: Int = 2048
    }

    init {
        _state = createInitialState()
        _prevState = state
    }

    private fun isWinning(boardState: Board.State): Boolean {
        return boardState.cellMatrix.flatten().any { cell ->
            cell.value == winningCellValue && state.result != MoveOutcome.WON
        }
    }

    private fun isLosing(boardState: Board.State): Boolean {

        if (boardState.unfilledCellsIndices.size == 0) {
            Log.i(
                "Losing",
                "isLoosing:\n ${
                    boardState.cellMatrix.toString()
                }"
            )
            val allMoves = listOf(
                board.upMove(boardState).state.cellMatrix,
                board.downMove(boardState).state.cellMatrix,
                board.rightMove(boardState).state.cellMatrix,
                board.leftMove(boardState).state.cellMatrix,
            )
            allMoves.onEachIndexed() { i, m ->
                Log.i(
                    "Losing",
                    "allMoves[$i]:\n ${m.toString()}"
                )
            }
            return ((boardState.cellMatrix == allMoves[0]) && (allMoves[0] == allMoves[1]) &&
                    (allMoves[1] == allMoves[2]) &&
                    (allMoves[2] == allMoves[3]))

        }
        return false
    }


    fun move(direction: Direction): Move {

        var (newBoardState, score) = when (direction) {
            Direction.UP -> board.upMove(state.boardState)
            Direction.DOWN -> board.downMove(state.boardState)
            Direction.RIGHT -> board.rightMove(state.boardState)
            Direction.LEFT -> board.leftMove(state.boardState)
        }


        if (newBoardState != state.boardState) {
            _prevState = _state

            newBoardState = board.placeRandomCell(newBoardState)


            val outcome = if (isWinning(newBoardState)) {
                MoveOutcome.WON
            } else if (isLosing(newBoardState)) {
                MoveOutcome.LOST
            } else {
                MoveOutcome.NOTHING
            }

            val newState = state.copy(
                boardState = newBoardState,
                score = state.score + score,
                highScore = maxOf(state.score + score, state.highScore),
                result = if (outcome==MoveOutcome.NOTHING) state.result else outcome
            )

            _state = newState

            Log.i("TAG", "move: ${state.score} ${state.result}")
            return Move(successful = true, outcome = outcome)
        }
        return Move(successful = false, outcome = MoveOutcome.NOTHING)
    }


    fun undoState() {
        if (state.result!=MoveOutcome.LOST) {
            _state = prevState
        }
    }

    fun resetState() {
        _state = createInitialState().copy(highScore = _state.highScore)
        _prevState = state
    }

    fun createInitialState(): State {
        var newState = State(
            boardState = Board.State(
                cellMatrix = MutableList(4) { rowIdx ->
                    MutableList(4) { colIdx ->
                        Cell(0, rowIdx * 4 + colIdx)
                    }
                },
                unfilledCellsIndices = (0..15).toHashSet(),
            ),
            score = 0,
            highScore = 0,
            result = MoveOutcome.NOTHING
        )

        newState = newState.copy(boardState = board.placeRandomCell(newState.boardState))
        newState = newState.copy(boardState = board.placeRandomCell(newState.boardState))

        return newState
    }

    fun restoreState(savedGame: SavedGame) {
        _state = savedGame.state
        _prevState = savedGame.prevState
    }


}

fun List<List<Cell>>.toString(): String {
    return joinToString("\n") { row ->
        row.joinToString("\t") { cell -> cell.value.toString() }
    }
}