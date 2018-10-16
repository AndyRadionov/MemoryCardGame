package io.github.andyradionov.memorycardgame.ui.scores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.andyradionov.memorycardgame.R;
import io.github.andyradionov.memorycardgame.game.Score;


/**
 * @author Andrey Radionov
 */

public class ScoreAdapter extends ArrayAdapter<Score> {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm, dd.MM.yyyy",
            Locale.getDefault());

    public ScoreAdapter(@NonNull Context context, List<Score> scores) {
        super(context, 0, scores);
    }

    @Override
    public @NonNull View getView(int position, View listItemView, @NonNull ViewGroup parent) {
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_score, parent, false);
        }

        Score currentScore = getItem(position);

        if (currentScore != null ) {
            TextView scorePositionView = listItemView.findViewById(R.id.tv_score_position);
            scorePositionView.setText(String.valueOf(position + 1));

            TextView scoreView = listItemView.findViewById(R.id.tv_score_display);
            scoreView.setText(String.valueOf(currentScore.getScore()));

            TextView scoreDateView = listItemView.findViewById(R.id.tv_score_date);
            String formattedDate = formatDate(currentScore.getDate());
            scoreDateView.setText(formattedDate);
        }
        return listItemView;
    }

    private String formatDate(Date dateObject) {
        return DATE_FORMAT.format(dateObject);
    }
}
