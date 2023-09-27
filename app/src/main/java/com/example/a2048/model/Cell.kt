package com.example.a2048.model

data class Cell(val value: Int, val id: Int) {
    override fun toString(): String {
        return "(value=$value id=$id)"
    }


}