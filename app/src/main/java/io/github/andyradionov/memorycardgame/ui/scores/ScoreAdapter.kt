package io.github.andyradionov.memorycardgame.ui.scores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import io.github.andyradionov.memorycardgame.R
import io.github.andyradionov.memorycardgame.game.Score


/**
 * @author Andrey Radionov
 */

class ScoreAdapter(context: Context, scores: List<Score>) : ArrayAdapter<Score>(context, 0, scores) {

    override fun getView(position: Int, listItemView: View?, parent: ViewGroup): View {
        var listItem = listItemView
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_score, parent, false)
        }

        val currentScore = getItem(position)

        if (currentScore != null) {
            val scorePositionView = listItem!!.findViewById<TextView>(R.id.tv_score_position)
            scorePositionView.text = (position + 1).toString()

            val scoreView = listItem.findViewById<TextView>(R.id.tv_score_display)
            scoreView.text = currentScore.score.toString()

            val scoreDateView = listItem.findViewById<TextView>(R.id.tv_score_date)
            val formattedDate = formatDate(currentScore.date)
            scoreDateView.text = formattedDate
        }
        return listItem!!
    }

    private fun formatDate(dateObject: Date): String {
        return DATE_FORMAT.format(dateObject)
    }

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.getDefault())
    }
}
