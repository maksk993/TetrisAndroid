package com.example.tetrisandroid;

import android.content.Context;
import android.content.SharedPreferences;

public class DataManager {
    private static final String PREF_NAME = "TetrisPrefs";
    private static final String[] KEYS = {"HIGHSCORE", "FIELD_STATE"};
    private SharedPreferences sharedPreferences;

    public DataManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    };

    private void setHighScore(int highScore) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEYS[0], highScore);
        editor.apply();
    }
    public void getStartHighScore() {
        setStartHighScore(sharedPreferences.getInt(KEYS[0], 0));
    }

    public void setNewHighScore() {
        setHighScore(getCurrentHighScore());
    }

    native void setStartHighScore(int highScore);
    native int getCurrentHighScore();
}
