package com.example.tetrisandroid.ui.menu.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.tetrisandroid.databinding.FragmentStartBinding
import com.example.tetrisandroid.ui.menu.MenuViewModel
import com.example.tetrisandroid.ui.menu.MenuViewModelFactory
import com.example.tetrisandroid.ui.menu.util.Buttons

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding
    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), MenuViewModelFactory(requireContext()))[MenuViewModel::class.java]

        initButtons()

        return binding.root
    }


    private fun initButtons(){
        binding.apply {
            buttonPlay.setOnClickListener {
                viewModel.handleButton(Buttons.PLAY)
            }
            buttonOptions.setOnClickListener {
                viewModel.handleButton(Buttons.OPTIONS)
            }
            buttonExit.setOnClickListener {
                viewModel.handleButton(Buttons.EXIT)
            }
        }
    }
}