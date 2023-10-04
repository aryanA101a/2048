package com.example.a2048.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2048.model.GameBoard
import com.example.a2048.model.Cell
import com.example.a2048.util.Direction
import com.example.a2048.util.MoveOutcome
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val gameBoard = GameBoard()

    private val _gameBoardState = MutableLiveData<GameBoard.GameBoardState>()
    val gameBoardState: LiveData<GameBoard.GameBoardState> = _gameBoardState

//    private val _gameBoardScore = MutableLiveData<Int>()
//    val gameBoardScore: LiveData<Int> = _gameBoardScore
//
    private val _moveOutcome = MutableSharedFlow<MoveOutcome>()
    val moveOutcome: SharedFlow<MoveOutcome> = _moveOutcome


    init {
        _gameBoardState.value = gameBoard.state

    }


    fun onSwipe(direction: Direction) {
        val (stateChanged, moveOutcome) = gameBoard.move(direction = direction)
        if (stateChanged) {
            _gameBoardState.value = gameBoard.state
            if(moveOutcome!=MoveOutcome.NOTHING)
                viewModelScope.launch {
                    _moveOutcome.emit(moveOutcome)
                }
        }

    }

    fun onUndo() {
        gameBoard.undoState()
        _gameBoardState.value = gameBoard.state

    }

    fun onReset() {
        gameBoard.initState()
        _gameBoardState.value = gameBoard.state
    }


}





