package io.github.andyradionov.memorycardgame.ui.game

import android.animation.Animator
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import io.github.andyradionov.memorycardgame.R
import io.github.andyradionov.memorycardgame.ui.scores.ScoresActivity
import io.github.andyradionov.memorycardgame.utils.ScoresHelper
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    private var firstCardClicked: CardView? = null
    private var secondCardClicked: CardView? = null

    private lateinit var gameViewModel: GameViewModel
    private val introHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        initCards()
        initViews()
    }

    override fun onPause() {
        super.onPause()
        introHandler.removeCallbacks(null)
    }

    fun onCardClick(v: View) {
        if (gameViewModel.clicksCount >= 2) return

        val cardView = v as CardView
        if (firstCardClicked === cardView) return
        if (!cardView.isFlipped) gameViewModel.clicksCount++
        if (gameViewModel.clicksCount > 2) return

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
                    gameViewModel.moves++
                    updateScoresView()
                    checkCardsMatch()
                }
            }
        })
    }

    fun onStartClick(v: View) {
        if (btn_start.text == getString(R.string.btn_start_text)) {
            btn_start.text = getString(R.string.btn_reset_text)
            updateScoresView()
        } else {
            gameViewModel.reset()
            initCards()
            updateScoresView()
        }
        flipAllCards()
    }

    fun onScoresClick(v: View) {
        startActivity(Intent(this, ScoresActivity::class.java))
    }

    private fun initCards() {
        cards_container.removeAllViews()
        for (cardResId in gameViewModel.pairsRes) {
            addCardToContainer(cardResId)
        }
    }

    private fun initViews() {
        if (gameViewModel.moves > 0) {
            updateScoresView()
            btn_start.text = getString(R.string.btn_reset_text)
        }
    }

    private fun addCardToContainer(cardResId: Int) {
        val card = LayoutInflater.from(this)
                .inflate(R.layout.item_card, cards_container, false) as CardView
        if (cardResId != -1) {
            card.resourceId = cardResId
            card.setBackgroundResource(R.drawable.back)
        } else {
            card.visibility = View.INVISIBLE
        }
        cards_container.addView(card)
    }

    private fun checkCardsMatch() {
        secondCardClicked?.animate()?.setListener(object : CardFlipListener() {
            override fun onAnimationEnd(animation: Animator) {
                if (firstCardClicked?.resourceId == secondCardClicked?.resourceId) {
                    firstCardClicked?.visibility = View.INVISIBLE
                    secondCardClicked?.visibility = View.INVISIBLE

                    removePair()
                    checkWin()
                } else {
                    flipCard(firstCardClicked!!, false)
                    flipCard(secondCardClicked!!, false)
                }

                gameViewModel.clicksCount = 0
                firstCardClicked = null
                secondCardClicked = null
            }
        })
    }

    private fun removePair() {
        gameViewModel.pairsLeft--
        val firstIdx = gameViewModel.pairsRes.indexOf(firstCardClicked?.resourceId)
        val secondIdx = gameViewModel.pairsRes.lastIndexOf(secondCardClicked?.resourceId)
        gameViewModel.pairsRes[firstIdx] = -1
        gameViewModel.pairsRes[secondIdx] = -1
    }

    private fun checkWin() {
        if (gameViewModel.pairsLeft == 0) {
            showScoreDialog()
            ScoresHelper.updateScoresList(this, gameViewModel.moves)
            gameViewModel.reset()
        }
    }

    private fun flipCard(cardView: CardView, isFlipped: Boolean) {
        val backResource = if (!isFlipped) R.drawable.back else cardView.resourceId
        cardView.setBackgroundResource(backResource)
        cardView.isFlipped = isFlipped
        cardView.rotationY = 270f
        cardView.animate().rotationY(360f).setListener(null)
    }

    private fun flipAllCards() {
        btn_start.isEnabled = false
        for (i in 0 until cards_container.childCount) {
            val cardView = cards_container.getChildAt(i) as CardView
            flipCard(cardView, !cardView.isFlipped)
        }

        introHandler.postDelayed({
            for (i in 0 until cards_container.childCount) {
                val cardView = cards_container.getChildAt(i) as CardView
                flipCard(cardView, !cardView.isFlipped)
            }
            gameViewModel.clicksCount = 0
            btn_start.isEnabled = true
        }, WAIT_TIME)
    }

    private fun updateScoresView() {
        tv_info_display.text = getString(R.string.moves_number, gameViewModel.moves)
    }

    private fun showScoreDialog() {
        val message = getString(R.string.result_message, gameViewModel.moves)

        AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle(R.string.result_dialog_title)
                .setCancelable(false)
                .setPositiveButton(R.string.exit) { _, _ -> this.finish() }
                .setNeutralButton(R.string.scores_button) { _, _ ->
                    startActivity(Intent(this, ScoresActivity::class.java))
                }
                .setNegativeButton(R.string.restart_button) { dialog, which ->
                    gameViewModel.reset()
                    updateScoresView()
                }
                .create()
                .show()

    }

    private abstract class CardFlipListener : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {/*NOP*/}
        override fun onAnimationCancel(animation: Animator) {/*NOP*/}
        override fun onAnimationRepeat(animation: Animator) {/*NOP*/}
    }

    companion object {
        private const val WAIT_TIME = 5000L
    }
}
