package com.example.a2048

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a2048.Util.Companion.idxToCoordinates
import kotlinx.coroutines.delay
import kotlin.random.Random

class GameViewModel : ViewModel() {
    var boardState = MutableLiveData<List<List<Util.PositionedInt>>>().apply {
        value=List<List<Util.PositionedInt>>(4) { rowIdx ->
            MutableList<Util.PositionedInt>(4) { colIdx ->
                Util.PositionedInt(
                    0,
                    rowIdx * 4 + colIdx
                )
            }
        }
    }

    private val unfilledCells = (0..15).toHashSet()


    fun placeRandomCell() {
        val number = if (Random.nextDouble() >= 0.1) 2 else 4
        var index: Int

        if (unfilledCells.isNotEmpty()) {
            index = unfilledCells.random()
            unfilledCells.remove(index)
            idxToCoordinates(index)
                .apply {
                    val newBoardState: List<MutableList<Util.PositionedInt>> =
                        boardState.value as List<MutableList<Util.PositionedInt>>
                    newBoardState[first][second] = Util.PositionedInt(number, index)
                    boardState.value = newBoardState
                }


        }


    }

    fun slideLeft(rowIdx: Int): MutableList<Util.PositionedInt> {
        val temp = MutableList<Util.PositionedInt>(4) { i -> Util.PositionedInt(0, rowIdx * 4 + i) }
        var i = 0
        var j = 0
        while (j <= 3) {
            if (boardState.value!![rowIdx][j].value == 0) {
                j++
            } else if (boardState.value!![rowIdx][j].value != 0 && temp[i].value == 0) {
                temp[i].value =
                    boardState.value!![rowIdx][j].value
                j++
            } else if (boardState.value!![rowIdx][j].value == temp[i].value
            ) {
                temp[i].value +=
                    boardState.value!![rowIdx][j].value
                i++
                j++
            } else {
                i++
            }
        }
        return temp
    }

    fun onLeft(gridAdapter: GridAdapter) {
        var newBoardState = arrayListOf<MutableList<Util.PositionedInt>>()
        for (rowIdx in boardState.value!!.indices) {
            newBoardState.add(slideLeft(rowIdx))
        }
        boardState.value = newBoardState
        boardState.value!!.flatten()
            .onEach { if (it.value != 0) unfilledCells.remove(it.position) else unfilledCells.add(it.position)}



    }


}

