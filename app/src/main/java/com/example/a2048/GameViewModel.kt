package com.example.a2048

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    val gameState: MutableLiveData<List<Int>> = MutableLiveData<List<Int>>().apply {
        value = List(16) { 0 }
    }

    fun setGameState(index:Int,newValue:Int){
        val tempState= gameState.value!!.toMutableList()
        tempState[index]=newValue
        gameState.value=tempState
    }
}