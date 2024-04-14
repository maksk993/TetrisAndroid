package com.example.tetrisandroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.tetrisandroid.R;
import com.example.tetrisandroid.data.DataManager;

import java.util.Objects;

public class OptionsActivity extends AppCompatActivity {

    private enum Buttons {
        SAVE_AND_RETURN, DEFAULT
    }

    private float startSpeed;
    private int speedLevel;
    private int speedIncreaseCoef;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        dataManager = new DataManager(this);

        startSpeed = dataManager.getStartSpeed();
        speedLevel = dataManager.getSpeedLevel();
        speedIncreaseCoef = dataManager.getSpeedIncreaseCoef();

        for (Buttons button : Buttons.values()) {
            addButtonLogic(button);
        }

        createStartSpeedSpinner();
        createIncreaseSpeedSpinner();
    }

    void addButtonLogic(Buttons code){
        Button button;
        if (Objects.requireNonNull(code) == Buttons.SAVE_AND_RETURN) {
            button = findViewById(R.id.buttonSaveAndReturn);
        }
        else {
            button = findViewById(R.id.buttonDefault);
        }

        button.setOnClickListener(view -> {
            if (code == Buttons.SAVE_AND_RETURN) {
                dataManager.setStartSpeed(startSpeed);
                dataManager.setSpeedIncreaseCoef(speedIncreaseCoef);
                dataManager.setSpeedLevel(speedLevel);

                overridePendingTransition(0, 0);
                Intent intent = new Intent(OptionsActivity.this, StartMenuActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
            else {
                startSpeed = 1.f;
                speedLevel = 1;
                speedIncreaseCoef = 40;
                setSelectionForSpinner(findViewById(R.id.spinnerStartSpeed),
                        getResources().getStringArray(R.array.startSpeedSpinner), speedLevel);
                setSelectionForSpinner(findViewById(R.id.spinnerIncreaseSpeed),
                        getResources().getStringArray(R.array.increaseSpeedSpinner), speedIncreaseCoef);
            }
        });
    }

    void createStartSpeedSpinner(){
        Spinner spinner;
        spinner = findViewById(R.id.spinnerStartSpeed);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.startSpeedSpinner,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setSelectionForSpinner(findViewById(R.id.spinnerStartSpeed),
                getResources().getStringArray(R.array.startSpeedSpinner), speedLevel);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected,
                                       int selectedItemPosition, long selectedId) {
                String[] choose = getResources().getStringArray(R.array.startSpeedSpinner);
                startSpeed = Float.parseFloat(choose[selectedItemPosition].substring
                        (
                            choose[selectedItemPosition].indexOf('(') + 1,
                            choose[selectedItemPosition].indexOf('s')
                        )
                );
                speedLevel = Integer.parseInt(choose[selectedItemPosition].substring
                        (
                                0,
                                choose[selectedItemPosition].indexOf(' ')
                        )
                );
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void createIncreaseSpeedSpinner(){
        Spinner spinner;
        spinner = findViewById(R.id.spinnerIncreaseSpeed);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.increaseSpeedSpinner,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setSelectionForSpinner(findViewById(R.id.spinnerIncreaseSpeed),
                getResources().getStringArray(R.array.increaseSpeedSpinner), speedIncreaseCoef);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected,
                                       int selectedItemPosition, long selectedId) {
                String[] choose = getResources().getStringArray(R.array.increaseSpeedSpinner);
                try {
                    speedIncreaseCoef = Integer.parseInt(choose[selectedItemPosition].substring
                            (
                                    0,
                                    choose[selectedItemPosition].indexOf(' ')
                            )
                    );
                }
                catch (NumberFormatException exception){
                    speedIncreaseCoef = 0;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void setSelectionForSpinner(Spinner spinner, String[] string_array, int value){
        int position = 0;
        for (int i = 0; i < string_array.length; i++){
            try {
                if (value == Integer.parseInt(string_array[i].substring
                        (
                                0,
                                string_array[i].indexOf(' ')
                        )
                )) {
                    position = i;
                    break;
                }
            }
            catch (NumberFormatException exception){
                position = i;
                break;
            }
        }
        spinner.setSelection(position);
    }
}