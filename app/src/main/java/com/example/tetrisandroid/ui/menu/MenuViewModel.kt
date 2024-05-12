package com.example.tetrisandroid.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tetrisandroid.data.DataRepository
import com.example.tetrisandroid.ui.menu.util.Buttons

class MenuViewModel(
    private val dataRepository: DataRepository
): ViewModel() {
    private val _lastButtonPressed: MutableLiveData<Buttons> = MutableLiveData()
    val lastButtonPressed: LiveData<Buttons> = _lastButtonPressed

    var speedIncreaseCoef = 40
    var startSpeed = 1F
    var speedLevel = 1

    fun handleButton(button: Buttons){
        _lastButtonPressed.value = button
    }

    fun saveSpeed(){
        dataRepository.setStartSpeed(startSpeed)
        dataRepository.setSpeedIncreaseCoef(speedIncreaseCoef)
        dataRepository.setSpeedLevel(speedLevel)
    }

    fun setDefault(){
        startSpeed = 1F
        speedLevel = 1
        speedIncreaseCoef = 40
    }

    fun getSavedSpeed(){
        startSpeed = dataRepository.getStartSpeed()
        speedLevel = dataRepository.getSpeedLevel()
        speedIncreaseCoef = dataRepository.getSpeedIncreaseCoef()
    }
}