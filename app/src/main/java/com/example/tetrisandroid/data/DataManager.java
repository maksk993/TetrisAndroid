package com.example.tetrisandroid.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManager {
    private static final String PREF_NAME = "TetrisPrefs";
    private final SharedPreferences m_sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final List<String> KEYS;
    private static final int DEFAULT_START_HIGHSCORE = 0;
    private static final float DEFAULT_START_SPEED = 1.f;
    private static final int DEFAULT_SPEED_LEVEL = 1;
    private static final int DEFAULT_SPEED_INCREASE_COEF = 40;

    static {
        KEYS = new ArrayList<>(Arrays.asList("HIGHSCORE", "START_SPEED", "SPEED_LEVEL", "SPEED_INCREASE_COEF"));
    }

    public DataManager(Context context) {
        m_sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    };

    private void setHighScore(int highScore) {
        editor = m_sharedPreferences.edit();
        editor.putInt(KEYS.get(KEYS.indexOf("HIGHSCORE")), highScore);
        editor.apply();
    }
    public void getStartHighScore() {
        cppSetStartHighScore(m_sharedPreferences.getInt(KEYS.get(KEYS.indexOf("HIGHSCORE")), DEFAULT_START_HIGHSCORE));
    }

    public void setNewHighScore() {
        setHighScore(cppGetCurrentHighScore());
    }

    public void setStartSpeed(float startSpeed){
        editor = m_sharedPreferences.edit();
        editor.putFloat(KEYS.get(KEYS.indexOf("START_SPEED")), startSpeed);
        editor.apply();
    }

    public float getStartSpeed() {
        return m_sharedPreferences.getFloat(KEYS.get(KEYS.indexOf("START_SPEED")), DEFAULT_START_SPEED);
    }

    public void setSpeedLevel(int lvl){
        editor = m_sharedPreferences.edit();
        editor.putInt(KEYS.get(KEYS.indexOf("SPEED_LEVEL")), lvl);
        editor.apply();
    }

    public int getSpeedLevel() {
        return m_sharedPreferences.getInt(KEYS.get(KEYS.indexOf("SPEED_LEVEL")), DEFAULT_SPEED_LEVEL);
    }

    public void setSpeedIncreaseCoef(int coef){
        editor = m_sharedPreferences.edit();
        editor.putInt(KEYS.get(KEYS.indexOf("SPEED_INCREASE_COEF")), coef);
        editor.apply();
    }

    public int getSpeedIncreaseCoef() {
        return m_sharedPreferences.getInt(KEYS.get(KEYS.indexOf("SPEED_INCREASE_COEF")), DEFAULT_SPEED_INCREASE_COEF);
    }

    native void cppSetStartHighScore(int highScore);
    native int cppGetCurrentHighScore();
}
