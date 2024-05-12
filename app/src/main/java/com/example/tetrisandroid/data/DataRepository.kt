package com.example.tetrisandroid.data

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

const val PREF_NAME = "TetrisPrefs"

class DataRepository(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: Editor = sharedPreferences.edit()

    private val DEFAULT_START_HIGHSCORE = 0
    private val DEFAULT_START_SPEED = 1f
    private val DEFAULT_SPEED_LEVEL = 1
    private val DEFAULT_SPEED_INCREASE_COEF = 40

    private fun setHighScore(highScore: Int) {
        editor.putInt("HIGHSCORE", highScore)
        editor.apply()
    }

    fun getStartHighScore() {
        cppSetStartHighScore(sharedPreferences.getInt("HIGHSCORE", DEFAULT_START_HIGHSCORE))
    }

    fun setNewHighScore() {
        setHighScore(cppGetCurrentHighScore())
    }

    fun setStartSpeed(startSpeed: Float) {
        editor.putFloat("START_SPEED", startSpeed)
        editor.apply()
    }

    fun getStartSpeed(): Float {
        return sharedPreferences.getFloat("START_SPEED", DEFAULT_START_SPEED)
    }

    fun setSpeedLevel(lvl: Int) {
        editor.putInt("SPEED_LEVEL", lvl)
        editor.apply()
    }

    fun getSpeedLevel(): Int {
        return sharedPreferences.getInt("SPEED_LEVEL", DEFAULT_SPEED_LEVEL)
    }

    fun setSpeedIncreaseCoef(coef: Int) {
        editor.putInt("SPEED_INCREASE_COEF", coef)
        editor.apply()
    }

    fun getSpeedIncreaseCoef(): Int {
        return sharedPreferences.getInt("SPEED_INCREASE_COEF", DEFAULT_SPEED_INCREASE_COEF)
    }

    private external fun cppSetStartHighScore(highScore: Int)
    private external fun cppGetCurrentHighScore(): Int
}