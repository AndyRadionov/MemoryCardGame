package io.github.andyradionov.memorycardgame.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.github.andyradionov.memorycardgame.R;
import io.github.andyradionov.memorycardgame.game.Score;

/**
 * @author Andrey Radionov
 */

public class ScoresHelper {
    private static final int SCORES_LIST_SIZE = 10;
    private static final Gson GSON = new Gson();

    private ScoresHelper() {
    }

    public static List<Score> loadScores(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String scoresJson = sharedPrefs.getString(
                context.getString(R.string.scores_list_key),
                context.getString(R.string.scores_list_default));
        return parseJson(scoresJson);
    }

    public static void updateScoresList(Context context, int currentScore) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        Date date = new Date();
        Score scoreToSave = new Score(currentScore, date);

        List<Score> scores = loadScores(context);
        scores.add(scoreToSave);
        Collections.sort(scores);

        scores = scores.size() > 10 ? scores.subList(0, SCORES_LIST_SIZE) : scores;

        String scoresJson = GSON.toJson(scores);
        sharedPrefs.edit()
                .putString(context.getString(R.string.scores_list_key), scoresJson)
                .apply();
    }

    private static List<Score> parseJson(String scoresJson) {
        Type listType = new TypeToken<ArrayList<Score>>(){}.getType();
        return GSON.fromJson(scoresJson, listType);
    }
}
