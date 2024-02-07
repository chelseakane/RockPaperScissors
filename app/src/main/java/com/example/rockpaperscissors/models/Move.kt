package com.example.rockpaperscissors.models

import com.example.rockpaperscissors.R

enum class Move(val icon: Int) {
    ROCK(R.drawable.rock_24),
    PAPER(R.drawable.paper_24),
    SCISSORS(R.drawable.scissors_24);

    // Rock beats scissors, paper beats rock, scissors beats paper
    val beats: Move
        get() = when (this) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }
}

fun Move.toTitleCase() : String {
    return this.name.lowercase().replaceFirstChar { it.titlecase() }
}