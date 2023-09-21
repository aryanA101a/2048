package com.example.a2048

class Util {
     companion object{
         fun idxToCoordinates(idx: Int) = Pair(first = idx / 4, second = idx % 4)
         
     }
    data class Cell(val value: Int,  val id:Int){
        override fun toString(): String {
            return "(value=$value id=$id)"
        }
    }

}