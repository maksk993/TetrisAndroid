package com.example.tetrisandroid.ui.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tetrisandroid.data.DataRepository

class MenuViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MenuViewModel(DataRepository(context)) as T
    }
}