package com.example.tictactoe_compose.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.tictactoe_compose.model.GameState
import com.example.tictactoe_compose.model.Player

class TicTacToeViewModel : ViewModel() {
    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    fun onCellClicked(index: Int) {
        val currentState = _state.value


        if (currentState.board[index] != null || currentState.winner != null) return


        val newBoard = currentState.board.toMutableList().apply {
            this[index] = currentState.currentPlayer
        }


        val winner = calculateWinner(newBoard)
        val isDraw = winner == null && newBoard.all { it != null }


        _state.value = currentState.copy(
            board = newBoard,
            currentPlayer = currentState.currentPlayer.next(),
            winner = winner,
            isDraw = isDraw
        )
    }


    fun resetGame() {
        _state.value = GameState()
    }


    private fun calculateWinner(board: List<Player?>): Player? {
        val winPatterns = listOf(
            listOf(0,1,2), listOf(3,4,5), listOf(6,7,8),
            listOf(0,3,6), listOf(1,4,7), listOf(2,5,8),
            listOf(0,4,8), listOf(2,4,6)
        )


        return winPatterns
            .map { pattern -> pattern.map { board[it] } }
            .firstOrNull { it.all { cell -> cell != null && cell == it[0] } }
            ?.first()
    }
}