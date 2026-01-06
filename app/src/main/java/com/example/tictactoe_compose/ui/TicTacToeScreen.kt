package com.example.tictactoe_compose.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe_compose.viewmodel.TicTacToeViewModel
import com.example.tictactoe_compose.model.Player
import androidx.compose.ui.res.stringResource
import com.example.tictactoe_compose.R

@Composable
fun TicTacToeScreen(viewModel: TicTacToeViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val winner = state.winner

        Text(
            text = when {
                winner != null -> stringResource(R.string.winner_text, winner.name)
                state.isDraw -> stringResource(R.string.draw_text)
                else -> stringResource(R.string.turn_text, state.currentPlayer.name)
            },
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(Modifier.height(16.dp))


        Board(
            board = state.board,
            onCellClick = viewModel::onCellClicked
        )


        Spacer(Modifier.height(16.dp))


        Button(onClick = viewModel::resetGame) {
            Text(stringResource(R.string.reset))
        }
    }
}


@Composable
private fun Board(
    board: List<Player?>,
    onCellClick: (Int) -> Unit
) {
    Column {
        for (row in 0 until 3) {
            Row {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground)
                            .clickable { onCellClick(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = board[index]?.name ?: "",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            }
        }
    }
}