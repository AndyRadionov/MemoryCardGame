package io.github.andyradionov.memorycardgame.ui.game

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import io.github.andyradionov.memorycardgame.R
import io.github.andyradionov.memorycardgame.game.CardsDataHelper
import kotlinx.android.synthetic.main.activity_game.*
import java.util.concurrent.atomic.AtomicInteger

private const val WAIT_TIME = 5000L
class GameActivity : AppCompatActivity() {

    private var firstCardClicked: CardView? = null
    private var secondCardClicked: CardView? = null

    private var clickCounter = AtomicInteger(2)
    private val introHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        initCards()
    }

    fun onCardClick(v: View) {
        if (clickCounter.get() >= 2) return

        val cardView = v as CardView
        if (firstCardClicked === cardView) return
        if (!cardView.isFlipped) clickCounter.incrementAndGet()
        if (clickCounter.get() > 2) return

        cardView.rotationY = 0f

        cardView.animate().rotationY(90f).setListener(object : CardFlipListener() {

            override fun onAnimationEnd(animation: Animator) {
                if (cardView.isFlipped) {
                    return
                }

                flipCard(cardView, true)

                if (firstCardClicked == null) {
                    firstCardClicked = cardView
                } else {
                    secondCardClicked = cardView
                    checkCardsMatch()
                }
            }
        })
    }

    public fun onStartClick(v: View) {
        flipAllCards()
    }

    private fun initCards() {
        val uniquePairs = CardsDataHelper.generateUniqueCardPairs()

        for (cardResId in uniquePairs) {
            addCardToContainer(cardResId)
        }
    }

    private fun addCardToContainer(cardResId: Int) {
        val card = LayoutInflater.from(this)
                .inflate(R.layout.item_card, cards_container, false) as CardView
        card.resourceId = cardResId
        card.setBackgroundResource(R.drawable.back)
        cards_container.addView(card)
    }

    private fun checkCardsMatch() {
        secondCardClicked?.animate()?.setListener(object : CardFlipListener() {
            override fun onAnimationEnd(animation: Animator) {
                if (firstCardClicked?.resourceId == secondCardClicked?.resourceId) {
                    firstCardClicked?.visibility = View.INVISIBLE
                    secondCardClicked?.visibility = View.INVISIBLE
                } else {
                    flipCard(firstCardClicked!!, false)
                    flipCard(secondCardClicked!!, false)
                }

                clickCounter.set(0)
                firstCardClicked = null
                secondCardClicked = null
            }
        })
    }

    private fun flipCard(cardView: CardView, isFlipped: Boolean) {
        val backResource = if (!isFlipped) R.drawable.back else cardView.resourceId
        cardView.setBackgroundResource(backResource)
        cardView.isFlipped = isFlipped
        cardView.rotationY = 270f
        cardView.animate().rotationY(360f).setListener(null)
    }

    private fun flipAllCards() {
        for (i in 0 until cards_container.childCount) {
            val cardView = cards_container.getChildAt(i) as CardView
            flipCard(cardView, !cardView.isFlipped)
        }

        introHandler.postDelayed({
            for (i in 0 until cards_container.childCount) {
                val cardView = cards_container.getChildAt(i) as CardView
                flipCard(cardView, !cardView.isFlipped)
            }
            clickCounter.set(0)
        }, WAIT_TIME)
    }

    private abstract class CardFlipListener : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {/*NOP*/}
        override fun onAnimationCancel(animation: Animator) {/*NOP*/}
        override fun onAnimationRepeat(animation: Animator) {/*NOP*/}
    }
}
