package com.example.rockpaperscissors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rockpaperscissors.models.GameResult
import com.example.rockpaperscissors.models.Move
import com.example.rockpaperscissors.models.toTitleCase
import com.example.rockpaperscissors.ui.animations.ConfettiRain
import com.example.rockpaperscissors.ui.theme.RockPaperScissorsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RockPaperScissorsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RockPaperScissors()
                }
            }
        }
    }
}

@Composable
fun RockPaperScissors(modifier: Modifier = Modifier) {
    var userMove by remember { mutableStateOf<Move?>(null) }
    var computerMove by remember { mutableStateOf<Move?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxHeight()) {
        if (showConfetti) {
            ConfettiRain {
                showConfetti = false
            }
        }

        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // game content
            userMove?.let {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f).padding(vertical = 90.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isLoading) {
                        Loading()
                    } else {
                        ComputerMove(computerMove)
                    }

                    if (showResult) {
                        GameResult(
                            userMove!!,
                            computerMove!!,
                            onWin = { showConfetti = true }
                        )
                    }
                    UserMove(userMove)
                }
            } ?: YourMove(modifier = Modifier.weight(1f))

            // button row
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Move.entries.forEach { move ->
                    Button(
                        onClick = {
                            userMove = move
                            isLoading = true
                            showResult = false // reset
                            // Simulate delay for computer's choice
                            coroutineScope.launch {
                                delay(1000)
                                computerMove = Move.entries.random()
                                delay(500)
                                isLoading = false
                                showResult = true
                            }
                        },
                        enabled = !isLoading && !showConfetti,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = move.icon),
                            contentDescription = move.name
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun YourMove(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to\nRock, Paper, Scissors!",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Choose your move from the options below",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "The computer is choosing",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ComputerMove(computerMove: Move?, modifier: Modifier = Modifier) {
    computerMove?.let {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Computer Chose ${it.toTitleCase()}",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
            Image(
                painter = painterResource(id = it.icon),
                contentDescription = it.name,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun UserMove(userMove: Move?, modifier: Modifier = Modifier) {
    userMove?.let {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = userMove.icon),
                contentDescription = userMove.name,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "You Chose ${userMove.toTitleCase()}",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun GameResult(
    userMove: Move,
    computerMove: Move,
    modifier: Modifier = Modifier,
    onWin: () -> Unit) {
    val result = when {
        userMove == computerMove -> GameResult.DRAW
        userMove.beats == computerMove -> GameResult.WIN
        else -> GameResult.LOSE
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (result == GameResult.WIN) {
            onWin()
        }
        Text(
            text = result.message,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RockPaperScissorsPreview() {
    RockPaperScissorsTheme {
        RockPaperScissors()
    }
}