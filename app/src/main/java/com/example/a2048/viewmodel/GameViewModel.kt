package com.example.a2048.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2048.data.repository.GameRepository
import com.example.a2048.data.model.Game
import com.example.a2048.data.model.SavedGame
import com.example.a2048.util.Direction
import com.example.a2048.util.MoveOutcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository, private val game: Game
) : ViewModel() {


    private val _gameState = MutableLiveData<Game.State>()
    val gameState: LiveData<Game.State> = _gameState

    private val _moveOutcome = MutableSharedFlow<MoveOutcome>()
    val moveOutcome: SharedFlow<MoveOutcome> = _moveOutcome

    init {
        notifyListeners()
    }


    fun onSwipe(direction: Direction) {
        val (stateChanged, moveOutcome) = game.move(direction = direction)
        if (stateChanged) {
            notifyListeners()
            if (moveOutcome != MoveOutcome.NOTHING) viewModelScope.launch {
                _moveOutcome.emit(moveOutcome)
            }
        }

    }

    fun onUndo() {
        game.undoState()
        notifyListeners()

    }

    fun onReset() {
        game.resetState()
        notifyListeners()
    }

    fun restorePersistedState() {
        Log.i("TAG", "restorePersistedState: ")

        viewModelScope.launch(Dispatchers.IO) {
            val savedGame = gameRepository.getGameState()

            withContext(Dispatchers.Main) {

                if (savedGame.state.boardState.cellMatrix.isNotEmpty()
                ) {
                    if (savedGame.state.result != MoveOutcome.LOST) {
                        game.restoreState(savedGame)
                        if (game.state.result == MoveOutcome.LOST) _moveOutcome.emit(game.state.result)
                    } else {
                        game.restoreState(
                            SavedGame(
                                state = game.state.copy(highScore = savedGame.state.highScore),
                                prevState = game.prevState
                            )
                        )
                    }
                    notifyListeners()

                }
            }
        }

    }

    private inline fun notifyListeners() {
        _gameState.value = game.state
    }

    fun persistState() {
        viewModelScope.launch(Dispatchers.IO) {
            gameRepository.saveGameState(SavedGame(state = game.state, prevState = game.prevState))
        }
    }


}





