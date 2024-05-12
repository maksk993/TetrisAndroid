package com.example.tetrisandroid.ui.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tetrisandroid.R
import com.example.tetrisandroid.databinding.FragmentOptionsBinding
import com.example.tetrisandroid.ui.menu.MenuViewModel
import com.example.tetrisandroid.ui.menu.MenuViewModelFactory
import com.example.tetrisandroid.ui.menu.util.Buttons

class OptionsFragment : Fragment() {
    private lateinit var binding: FragmentOptionsBinding
    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOptionsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), MenuViewModelFactory(requireContext()))[MenuViewModel::class.java]

        initButtons()
        initSpinners()
        initObservers()

        return binding.root
    }

    private fun initButtons() {
        binding.apply {
            buttonSaveAndReturn.setOnClickListener {
                viewModel.handleButton(Buttons.SAVE_AND_RETURN)
            }
            buttonDefault.setOnClickListener {
                viewModel.handleButton(Buttons.DEFAULT)
            }
        }
    }

    private fun initSpinners() {
        viewModel.getSavedSpeed()
        initStartSpeedSpinner()
        initSpeedIncreaseSpinner()
    }

    private fun initStartSpeedSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.startSpeedSpinner,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStartSpeed.adapter = adapter
        setSelectionForSpinner(
            binding.spinnerStartSpeed,
            resources.getStringArray(R.array.startSpeedSpinner),
            viewModel.speedLevel
        )

        binding.spinnerStartSpeed.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, itemSelected: View,
                    selectedItemPosition: Int, selectedId: Long
                ) {
                    val choose = resources.getStringArray(R.array.startSpeedSpinner)
                    val startSpeed = choose[selectedItemPosition].substring(
                        choose[selectedItemPosition].indexOf('(') + 1,
                        choose[selectedItemPosition].indexOf('s')
                    ).toFloat()
                    val speedLevel = choose[selectedItemPosition].substring(
                        0,
                        choose[selectedItemPosition].indexOf(' ')
                    ).toInt()
                    viewModel.startSpeed = startSpeed
                    viewModel.speedLevel = speedLevel
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun initSpeedIncreaseSpinner(){
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.increaseSpeedSpinner,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerIncreaseSpeed.adapter = adapter
        setSelectionForSpinner(
            binding.spinnerIncreaseSpeed,
            resources.getStringArray(R.array.increaseSpeedSpinner),
            viewModel.speedIncreaseCoef
        )

        binding.spinnerIncreaseSpeed.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, itemSelected: View,
                    selectedItemPosition: Int, selectedId: Long
                ) {
                    val choose = resources.getStringArray(R.array.increaseSpeedSpinner)
                    try {
                        val speedIncreaseCoef = choose[selectedItemPosition].substring(
                            0,
                            choose[selectedItemPosition].indexOf(' ')
                        ).toInt()
                        viewModel.speedIncreaseCoef = speedIncreaseCoef
                    }
                    catch (exception: NumberFormatException) {
                        viewModel.speedIncreaseCoef = 0
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setSelectionForSpinner(spinner: Spinner, stringArray: Array<String>, value: Int) {
        var position = 0
        for (i in stringArray.indices) {
            try {
                if (value == stringArray[i].substring(0, stringArray[i].indexOf(' ')).toInt()) {
                    position = i
                    break
                }
            }
            catch (exception: java.lang.NumberFormatException) {
                position = i
                break
            }
        }
        spinner.setSelection(position)
    }

    private fun initObservers() {
        viewModel.lastButtonPressed.observe(viewLifecycleOwner){
            if (it == Buttons.DEFAULT){
                setSelectionForSpinner(
                    binding.spinnerStartSpeed,
                    resources.getStringArray(R.array.startSpeedSpinner),
                    viewModel.speedLevel
                )
                setSelectionForSpinner(
                    binding.spinnerIncreaseSpeed,
                    resources.getStringArray(R.array.increaseSpeedSpinner),
                    viewModel.speedIncreaseCoef
                )
            }
        }
    }
}