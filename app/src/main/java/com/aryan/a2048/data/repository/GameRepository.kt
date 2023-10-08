package com.aryan.a2048.data.repository

import android.app.Application
import android.util.Log
import com.example.a2048.SavedGameProto
import com.aryan.a2048.data.model.Board
import com.aryan.a2048.data.model.Cell
import com.aryan.a2048.dataStore
import com.aryan.a2048.data.model.Game
import com.aryan.a2048.data.model.SavedGame
import com.aryan.a2048.util.MoveOutcome
import kotlinx.coroutines.flow.first

class GameRepository(private val context: Application) {

    suspend fun saveGameState(savedGame: SavedGame) {
        context.dataStore.updateData { currentData ->
            currentData.toBuilder().clear().setState(
                SavedGameProto.GameStateProto.newBuilder().setBoardState(
                    SavedGameProto.BoardStateProto.newBuilder()
                        .addAllCellMatrix(savedGame.state.boardState.cellMatrix.map { cells ->
                            SavedGameProto.CellRowProto.newBuilder()
                                .addAllCells(cells.map { cell ->
                                    SavedGameProto.CellProto.newBuilder().setValue(cell.value)
                                        .setId(cell.id)
                                        .build()
                                }).build()
                        }).addAllUnfilledCells(savedGame.state.boardState.unfilledCellsIndices)
                        .build()
                ).setResultValue(savedGame.state.result.ordinal)
                    .setScore(savedGame.state.score)
                    .setHighScore(savedGame.state.highScore)
            ).setPrevState(
                SavedGameProto.GameStateProto.newBuilder().setBoardState(
                    SavedGameProto.BoardStateProto.newBuilder()
                        .addAllCellMatrix(savedGame.prevState.boardState.cellMatrix.map { cells ->
                            SavedGameProto.CellRowProto.newBuilder()
                                .addAllCells(cells.map { cell ->
                                    SavedGameProto.CellProto.newBuilder().setValue(cell.value)
                                        .setId(cell.id)
                                        .build()
                                }).build()
                        }).addAllUnfilledCells(savedGame.prevState.boardState.unfilledCellsIndices)
                        .build()
                ).setResultValue(savedGame.prevState.result.ordinal)
                    .setScore(savedGame.prevState.score)
                    .setHighScore(savedGame.prevState.highScore)
            ).build()
        }
//
    }


    suspend fun getGameState(): SavedGame {
        Log.i("TAG", "getGameState: ")
        context.dataStore.data.first().let { savedGame ->
            Log.i("TAG", "getGameState: $savedGame")
            return SavedGame(
                state = Game.State(
                    boardState = Board.State(
                        cellMatrix = savedGame.state.boardState.cellMatrixList.map { cellRow ->
                            cellRow.cellsList.map { cell ->
                                Cell(
                                    value = cell.value, id = cell.id
                                )
                            }.toMutableList()
                        }.toMutableList(),
                        unfilledCellsIndices = savedGame.state.boardState.unfilledCellsList.toHashSet()
                    ),
                    score = savedGame.state.score,
                    highScore = savedGame.state.highScore,
                    result = MoveOutcome.entries[savedGame.state.result.ordinal]
                ),
                prevState = Game.State(
                    boardState = Board.State(
                        cellMatrix = savedGame.prevState.boardState.cellMatrixList.map { cellRow ->
                            cellRow.cellsList.map { cell ->
                                Cell(
                                    value = cell.value, id = cell.id
                                )
                            }.toMutableList()
                        }.toMutableList(),
                        unfilledCellsIndices = savedGame.prevState.boardState.unfilledCellsList.toHashSet()
                    ),
                    score = savedGame.prevState.score,
                    highScore = savedGame.prevState.highScore,
                    result = MoveOutcome.entries[savedGame.prevState.result.ordinal]
                )
            )
        }
    }


}