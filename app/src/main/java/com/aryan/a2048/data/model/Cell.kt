package com.aryan.a2048.data.model

data class Cell(val value: Int, val id: Int) {
    override fun toString(): String {
        return "(value=$value id=$id)"
    }


}