package io.github.andyradionov.memorycardgame.ui.scores

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.github.andyradionov.memorycardgame.R
import io.github.andyradionov.memorycardgame.utils.ScoresHelper
import kotlinx.android.synthetic.main.activity_scores.*

class ScoresActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)

        lv_scores.emptyView = findViewById(android.R.id.empty)
        showScoresList()
    }

    fun onBackClick(v: View) {
        onBackPressed()
    }

    private fun showScoresList() {
        val scores = ScoresHelper.loadScores(this)

        val scoreAdapter = ScoreAdapter(this, scores)

        lv_scores.setAdapter(scoreAdapter)
    }
}
