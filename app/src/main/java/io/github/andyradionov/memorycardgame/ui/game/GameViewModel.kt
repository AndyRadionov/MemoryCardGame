package io.github.andyradionov.memorycardgame.ui.game

import android.arch.lifecycle.ViewModel
import io.github.andyradionov.memorycardgame.game.CardsDataHelper
import io.github.andyradionov.memorycardgame.game.UNIQUE_CARDS_NUMBER
import java.util.*

/**
 * @author Andrey Radionov
 */
class GameViewModel : ViewModel() {
    var moves: Int = 0
    var clicksCount: Int = 2
    var pairsLeft: Int = UNIQUE_CARDS_NUMBER
    var pairsRes: MutableList<Int> = CardsDataHelper.generateUniqueCardPairs()

    fun reset() {
        moves = 0
        clicksCount = 2
        pairsLeft = UNIQUE_CARDS_NUMBER
        pairsRes = CardsDataHelper.generateUniqueCardPairs()
    }

    fun removePair(firstRes: Int?, secondRes: Int?) {
        pairsLeft--
        val firstIdx = pairsRes.indexOf(firstRes)
        val secondIdx = pairsRes.lastIndexOf(secondRes)
        pairsRes[firstIdx] = -1
        pairsRes[secondIdx] = -1
    }
}
