package io.github.andyradionov.memorycardgame.game

import io.github.andyradionov.memorycardgame.R
import java.util.*

/**
 * @author Andrey Radionov
 */
private const val ALL_CARDS_NUMBER = 52
private const val FIELD_CARDS_NUMBER = 16
private const val UNIQUE_CARDS_NUMBER = 16 / 2

object CardsDataHelper {

    fun generateUniqueCardPairs(): List<Int> {
        val random = Random()
        val uniqueCards = HashSet<Int>(UNIQUE_CARDS_NUMBER)

        for (i in 0 until UNIQUE_CARDS_NUMBER) {
            var nextCard = random.nextInt(ALL_CARDS_NUMBER)
            while (uniqueCards.contains(ALL_CARD_RES[nextCard])) {
                nextCard = random.nextInt(ALL_CARDS_NUMBER)
            }
            uniqueCards.add(ALL_CARD_RES[nextCard])
        }

        val uniquePairs = ArrayList<Int>(FIELD_CARDS_NUMBER)
        uniquePairs.addAll(uniqueCards)
        uniquePairs.addAll(uniqueCards)
        uniquePairs.shuffle()
        return uniquePairs
    }

    private val ALL_CARD_RES = intArrayOf(
            R.drawable.c0,
            R.drawable.c2,
            R.drawable.c3,
            R.drawable.c4,
            R.drawable.c5,
            R.drawable.c6,
            R.drawable.c7,
            R.drawable.c8,
            R.drawable.c9,
            R.drawable.ca,
            R.drawable.cj,
            R.drawable.ck,
            R.drawable.cq,

            R.drawable.d0,
            R.drawable.d2,
            R.drawable.d3,
            R.drawable.d4,
            R.drawable.d5,
            R.drawable.d6,
            R.drawable.d7,
            R.drawable.d8,
            R.drawable.d9,
            R.drawable.da,
            R.drawable.dj,
            R.drawable.dk,
            R.drawable.dq,

            R.drawable.h0,
            R.drawable.h2,
            R.drawable.h3,
            R.drawable.h4,
            R.drawable.h5,
            R.drawable.h6,
            R.drawable.h7,
            R.drawable.h8,
            R.drawable.h9,
            R.drawable.ha,
            R.drawable.hj,
            R.drawable.hk,
            R.drawable.hq,

            R.drawable.s0,
            R.drawable.s2,
            R.drawable.s3,
            R.drawable.s4,
            R.drawable.s5,
            R.drawable.s6,
            R.drawable.s7,
            R.drawable.s8,
            R.drawable.s9,
            R.drawable.sa,
            R.drawable.sj,
            R.drawable.sk,
            R.drawable.sq
    )
}
