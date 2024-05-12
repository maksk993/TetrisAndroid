package com.example.tetrisandroid.ui.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tetrisandroid.R
import com.example.tetrisandroid.ui.game.GameActivity
import com.example.tetrisandroid.ui.menu.fragments.OptionsFragment
import com.example.tetrisandroid.ui.menu.fragments.StartFragment
import com.example.tetrisandroid.ui.menu.util.Buttons

class MenuActivity : AppCompatActivity() {
    private lateinit var viewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        viewModel = ViewModelProvider(this, MenuViewModelFactory(this))[MenuViewModel::class.java]
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, StartFragment()).commit()
        initObservers()
    }

    private fun initObservers(){
        viewModel.lastButtonPressed.observe(this){
            when(it){
                Buttons.PLAY -> {
                    startActivity(Intent(this, GameActivity::class.java))
                    finish()
                }
                Buttons.OPTIONS -> {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, OptionsFragment()).commit()
                }
                Buttons.EXIT -> finish()
                Buttons.SAVE_AND_RETURN -> {
                    viewModel.saveSpeed()
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, StartFragment()).commit()
                }
                Buttons.DEFAULT -> {
                    viewModel.setDefault()
                }
                else -> {}
            }
        }
    }
}