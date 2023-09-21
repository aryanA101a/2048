package com.example.a2048

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a2048.Util.Companion.idxToCoordinates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {
    var boardState = MutableLiveData<List<List<Util.Cell>>>().apply {
        value = List<List<Util.Cell>>(4) { rowIdx ->
            MutableList<Util.Cell>(4) { colIdx ->
                Util.Cell(
                    0, rowIdx * 4 + colIdx
                )
            }
        }
    }

    private val unfilledCells = (0..15).toHashSet()
    var nextId=20


    fun placeRandomCell() {
        val number = if (Random.nextDouble() >= 0.1) 2 else 4
        var index: Int

        if (unfilledCells.isNotEmpty()) {
            index = unfilledCells.random()
            unfilledCells.remove(index)
            idxToCoordinates(index).apply {
                val newBoardState: List<MutableList<Util.Cell>> =
                    boardState.value as List<MutableList<Util.Cell>>
                newBoardState[first][second] =
                    newBoardState[first][second].copy(value = number)
                boardState.value = newBoardState
            }
            Log.i("TAG", "placeRandomCell: ${unfilledCells}")

        }


    }

    fun slideLeft(rowIdx: Int): MutableList<Util.Cell> {
        Log.i("TAG", "slideLeft: ${boardState.value!![rowIdx]}")
        val temp = MutableList<Util.Cell>(4) { i ->
            Util.Cell(
                0,
                boardState.value!![rowIdx][i].id
            )
        }
        var i = 0
        var j = 0
        while (j <= 3) {
            if (boardState.value!![rowIdx][j].value == 0) {
                j++
            } else if (boardState.value!![rowIdx][j].value != 0 && temp[i].value == 0) {
                val ithId=temp[i].id
                temp[i] = temp[i].copy(
                    value = boardState.value!![rowIdx][j].value,
                    id = temp[j].id
                )
                temp[j]=temp[j].copy(id=ithId)
                j++
            } else if (boardState.value!![rowIdx][j].value == temp[i].value) {
                val ithId=temp[i].id
                temp[i] = temp[i].copy(
                    value = temp[i].value + boardState.value!![rowIdx][j].value,
                    id = temp[j].id
                )
                temp[j]=temp[j].copy(id=ithId)
                i++
                j++
            } else {
                i++
            }
        }
        Log.i("TAG", "slideLeft: ${temp}")

        return temp
    }

    suspend fun onLeft(gridAdapter: GridAdapter) {
        var newBoardState = arrayListOf<MutableList<Util.Cell>>()
        for (rowIdx in boardState.value!!.indices) {
            newBoardState.add(slideLeft(rowIdx))
        }
        if (boardState.value != newBoardState) {
            boardState.value = newBoardState
            boardState.value!!.flatten().onEachIndexed { index, cell->
                if (cell.value != 0) unfilledCells.remove(index) else unfilledCells.add(
                    index
                )

            }
            Log.i("TAG", "onLeft: ${unfilledCells}")

            CoroutineScope(Dispatchers.Main).launch {
                delay(300L)
                placeRandomCell()
            }
        }

    }


}

