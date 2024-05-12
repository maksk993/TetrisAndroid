package com.example.tetrisandroid.ui.game

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.os.Handler
import android.view.MotionEvent
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tetrisandroid.data.DataRepository
import com.example.tetrisandroid.ui.game.util.Buttons
import com.example.tetrisandroid.ui.game.util.GameStates


class GameViewModel(
    private val dataRepository: DataRepository
): ViewModel() {
    private val _currentGameState: MutableLiveData<GameStates> = MutableLiveData(GameStates.PLAY)
    val currentGameState: LiveData<GameStates> = _currentGameState

    private val handler: Handler = Handler()
    private val runnableBtnDown: Runnable = Runnable {
        run {
            cppHandleTouch(Buttons.DOWN.ordinal)
            handler.postDelayed(runnableBtnDown, 40)
        }
    }

    fun initAssetManager(assets: AssetManager) {
        cppAssetManagerInit(assets)
    }

    fun getHighScore() = dataRepository.getStartHighScore()
    fun getStartSpeed(): Float = dataRepository.getStartSpeed() * 1000
    fun getSpeedLevel(): Int = dataRepository.getSpeedLevel()
    fun getSpeedIncreaseCoef(): Int = dataRepository.getSpeedIncreaseCoef()

    @SuppressLint("ClickableViewAccessibility")
    fun handleButton(button: Button, code: Buttons) {
        button.setOnTouchListener{view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN){
                when(code) {
                    Buttons.DOWN -> handler.post(runnableBtnDown)
                    Buttons.RESET -> {
                        cppHandleTouch(code.ordinal)
                        changePauseButtonToResume(false)
                    }
                    Buttons.PAUSE -> {
                        cppHandleTouch(code.ordinal)
                        if (cppIsGamePaused()) changePauseButtonToResume(true)
                        else changePauseButtonToResume(false)
                    }
                    else -> {
                        cppHandleTouch(code.ordinal)
                    }
                }
                true
            }
            else if (motionEvent.action == MotionEvent.ACTION_UP){
                if (code == Buttons.DOWN) handler.removeCallbacks(runnableBtnDown)
                true
            }
            false
        }
    }

    fun onResume(){
        handler.removeCallbacks(runnableBtnDown)
    }

    fun onPause(){
        cppSetGamePaused()
        changePauseButtonToResume(true)
    }

    fun onStop(){
        dataRepository.setNewHighScore()
    }

    private fun changePauseButtonToResume(yes: Boolean){
        _currentGameState.value =
            if (yes) GameStates.PAUSE
            else GameStates.PLAY
    }

    private external fun cppAssetManagerInit(assetManager: AssetManager)
    private external fun cppHandleTouch(code: Int)
    private external fun cppIsGamePaused(): Boolean
    private external fun cppSetGamePaused()
}