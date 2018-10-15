package io.github.andyradionov.memorycardgame

import android.animation.Animator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    private var firstCardClicked: CardView? = null
    private var secondCardClicked: CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        initCards()
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
        cards_container.addView(card)
    }

    fun onCardClick(v: View) {
        val cardView = v as CardView
        if (firstCardClicked === cardView) {
            return
        }
        cardView.rotationY = 0f

        cardView.animate().rotationY(90f).setListener(object : CardFlipListener() {

            override fun onAnimationEnd(animation: Animator) {
                if (cardView.isFlipped) {
                    return
                }

                flipCard(cardView, cardView.resourceId, true)

                if (firstCardClicked == null) {
                    firstCardClicked = cardView
                } else {
                    secondCardClicked = cardView
                    checkCardsMatch()
                }
            }
        })
    }

    private fun checkCardsMatch() {
        secondCardClicked?.animate()?.setListener(object : CardFlipListener() {
            override fun onAnimationEnd(animation: Animator) {
                if (firstCardClicked?.resourceId == secondCardClicked?.resourceId) {
                    firstCardClicked?.visibility = View.INVISIBLE
                    secondCardClicked?.visibility = View.INVISIBLE
                } else {
                    flipCard(firstCardClicked!!, R.drawable.back, false)
                    flipCard(secondCardClicked!!, R.drawable.back, false)
                }

                firstCardClicked = null
                secondCardClicked = null
            }
        })
    }

    private fun flipCard(cardView: CardView, pictureResourceId: Int, isFlipped: Boolean) {
        cardView.setBackgroundResource(pictureResourceId)
        cardView.isFlipped = isFlipped
        cardView.rotationY = 270f
        cardView.animate().rotationY(360f).setListener(null)
    }

    private abstract class CardFlipListener : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {/*NOP*/}
        override fun onAnimationCancel(animation: Animator) {/*NOP*/}
        override fun onAnimationRepeat(animation: Animator) {/*NOP*/}
    }
}
