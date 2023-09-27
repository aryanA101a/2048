package com.example.a2048.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2048.model.GameBoard
import com.example.a2048.model.Cell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val gameBoard = GameBoard()
    private val _gameBoardState = MutableLiveData<List<List<Cell>>>()
    val gameBoardState: LiveData<List<List<Cell>>>
        get() = _gameBoardState


    init {
        // Initialize LiveData with the initial state
        _gameBoardState.value = gameBoard.state
    }

    enum class SwipeDirection { UP, DOWN, RIGHT, LEFT }

    fun onSwipe(swipeDirection: SwipeDirection) {
        val stateChanged = when (swipeDirection) {
            SwipeDirection.UP -> gameBoard.upMove()
            SwipeDirection.DOWN -> gameBoard.downMove()
            SwipeDirection.RIGHT -> gameBoard.rightMove()
            SwipeDirection.LEFT -> gameBoard.leftMove()
        }

        if (stateChanged) {
            _gameBoardState.value = gameBoard.state
            viewModelScope.launch {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(300L)
                    gameBoard.placeRandomCell()
                    _gameBoardState.value = gameBoard.state
                }
            }
        }

    }


}





