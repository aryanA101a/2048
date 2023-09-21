package com.example.a2048

class Util {
     companion object{
         fun idxToCoordinates(idx: Int) = Pair(first = idx / 4, second = idx % 4)
         
     }
    data class PositionedInt(var value: Int, val position: Int)

}