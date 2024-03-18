package com.example.datamanager;

import android.content.Context;
import android.content.SharedPreferences;

public class DataManager {
    private static final String PREF_NAME = "TetrisPrefs";
    private static final String[] KEYS = {"HIGHSCORE", "START_SPEED", "SPEED_LEVEL", "SPEED_INCREASE_COEF"};
    private final SharedPreferences m_sharedPreferences;

    public DataManager(Context context) {
        m_sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    };

    private void setHighScore(int highScore) {
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putInt(KEYS[0], highScore);
        editor.apply();
    }
    public void getStartHighScore() {
        cppSetStartHighScore(m_sharedPreferences.getInt(KEYS[0], 0));
    }

    public void setNewHighScore() {
        setHighScore(cppGetCurrentHighScore());
    }

    public void setStartSpeed(float startSpeed){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putFloat(KEYS[1], startSpeed);
        editor.apply();
    }

    public float getStartSpeed() {
        return m_sharedPreferences.getFloat(KEYS[1], 1.f);
    }

    public void setSpeedLevel(int lvl){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putInt(KEYS[2], lvl);
        editor.apply();
    }

    public int getSpeedLevel() {
        return m_sharedPreferences.getInt(KEYS[2], 1);
    }

    public void setSpeedIncreaseCoef(int coef){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putInt(KEYS[3], coef);
        editor.apply();
    }

    public int getSpeedIncreaseCoef() {
        return m_sharedPreferences.getInt(KEYS[3], 40);
    }

    native void cppSetStartHighScore(int highScore);
    native int cppGetCurrentHighScore();
}
