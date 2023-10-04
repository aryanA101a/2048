package com.example.a2048.model

import com.example.a2048.util.MoveOutcome

data class Move(val successful: Boolean, val outcome: MoveOutcome)
