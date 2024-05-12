package com.example.tetrisandroid.ui.game

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tetrisandroid.R
import com.example.tetrisandroid.databinding.ActivityGameBinding
import com.example.tetrisandroid.ui.game.renderer.MySurfaceView
import com.example.tetrisandroid.ui.game.util.Buttons
import com.example.tetrisandroid.ui.game.util.GameStates

class GameActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var surfaceView: MySurfaceView

    companion object {
        init {
            System.loadLibrary("tetrisandroid")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, GameViewModelFactory(this))[GameViewModel::class.java]
        surfaceView = findViewById(R.id.MySurfaceView)

        viewModel.getHighScore()
        setScreenSizes()
        setGameSpeed()

        viewModel.initAssetManager(assets)
        for (button in Buttons.entries) initButton(button)
        initObservers()
    }

    private fun setScreenSizes(){
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        surfaceView.setSizes(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    private fun setGameSpeed(){
        surfaceView.setSpeed(
            viewModel.getStartSpeed(),
            viewModel.getSpeedLevel(),
            viewModel.getSpeedIncreaseCoef()
        )
    }

    private fun initButton(code: Buttons){
        val button =
            when(code){
                Buttons.LEFT -> binding.buttonLeft
                Buttons.DOWN -> binding.buttonDown
                Buttons.RIGHT -> binding.buttonRight
                Buttons.UP -> binding.buttonUp
                Buttons.ROTATE -> binding.buttonRotate
                Buttons.RESET -> binding.buttonReset
                Buttons.PAUSE -> binding.buttonPause
            }
        viewModel.handleButton(button, code)
    }

    private fun initObservers(){
        viewModel.currentGameState.observe(this){
            if (it == GameStates.PAUSE){
                binding.buttonPause.setBackgroundColor(getResources().getColor(R.color.pauseButtonPressed))
                binding.buttonPause.text = getString(R.string.buttonResume)
            }
            else {
                binding.buttonPause.text = getString(R.string.buttonPause)
                binding.buttonPause.setBackgroundColor(getResources().getColor(R.color.buttonColor))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        surfaceView.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        viewModel.onPause()
        surfaceView.onPause()
        super.onPause()
    }

    override fun onStop() {
        viewModel.onStop()
        super.onStop()
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}