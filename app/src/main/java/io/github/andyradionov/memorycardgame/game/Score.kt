package io.github.andyradionov.memorycardgame.game

import java.util.*

/**
 * @author Andrey Radionov
 */
class Score(val score: Int, val date: Date) : Comparable<Score> {

    override fun compareTo(other: Score): Int {
        return score - other.score
    }
}
