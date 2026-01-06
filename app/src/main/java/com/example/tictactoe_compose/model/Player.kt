package com.example.tictactoe_compose.model

enum class Player {
    X, O;

    fun next(): Player = if (this == X) O else X
}