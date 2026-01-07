package com.example.tictactoe_compose.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe_compose.viewmodel.TicTacToeViewModel
import com.example.tictactoe_compose.model.Player
import androidx.compose.ui.res.stringResource
import com.example.tictactoe_compose.R

@Composable
fun TicTacToeScreen(viewModel: TicTacToeViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val winner = state.winner
    val isGameOver = winner != null || state.isDraw

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val stateText = when {
            winner != null -> stringResource(R.string.winner_text, winner.name)
            state.isDraw -> stringResource(R.string.draw_text)
            else -> stringResource(R.string.turn_text, state.currentPlayer.name)
        }
        AnimatedContent(
            targetState = stateText,
            transitionSpec = {
                (fadeIn(animationSpec = tween(200)) + scaleIn(initialScale = 0.5f))
                    .togetherWith(fadeOut(animationSpec = tween(150)))
            },
            label = "SymbolAnimation"
        ) { gameText ->
            Text(
                text = gameText,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(Modifier.height(16.dp))


        Board(
            board = state.board,
            onCellClick = viewModel::onCellClicked
        )


        Spacer(Modifier.height(16.dp))

        val infiniteTransition = rememberInfiniteTransition(label = "PulseTransition")

        val buttonScale by infiniteTransition.animateFloat(
            initialValue = 1.0f,
            targetValue = if (isGameOver) 1.1f else 1.0f, // Only pulse if game is over
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "BtnScale"
        )

        Button(
            onClick = viewModel::resetGame,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.scale(buttonScale)
        ) {
            Text(
                text = stringResource(R.string.reset),
                color = MaterialTheme.colorScheme.onPrimary
            )
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
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable { onCellClick(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = board[index],
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(200)) + scaleIn(initialScale = 0.5f))
                                    .togetherWith(fadeOut(animationSpec = tween(150)))
                            },
                            label = "SymbolAnimation"
                        ) { player ->
                            val symbolColor = when (player) {
                                Player.X -> MaterialTheme.colorScheme.primary
                                Player.O -> MaterialTheme.colorScheme.secondary
                                null -> Color.Unspecified
                            }
                            Text(
                                text = player?.name ?: "",
                                color = symbolColor,
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                    }
                }
            }
        }
    }
}