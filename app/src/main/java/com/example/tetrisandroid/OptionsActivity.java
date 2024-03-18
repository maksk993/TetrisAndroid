package com.example.tetrisandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class OptionsActivity extends AppCompatActivity {

    private enum Buttons {
        SAVE_AND_RETURN, DEFAULT
    }

    private float startSpeed;
    private int speedLevel;
    private int speedIncreaseCoef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Intent intent = getIntent();
        if (intent != null){
            startSpeed = intent.getFloatExtra("StartSpeed", 1.f);
            speedLevel = intent.getIntExtra("Speed level", 1);
            speedIncreaseCoef = intent.getIntExtra("Speed increase coef", 40);
        }

        for (Buttons button : Buttons.values()) {
            addButtonLogic(button);
        }

        createStartSpeedSpinner();
        createIncreaseSpeedSpinner();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    void addButtonLogic(Buttons code){
        Button button;
        switch (code){
            case SAVE_AND_RETURN:
                button = findViewById(R.id.buttonSaveAndReturn);
                break;
            default:
                button = findViewById(R.id.buttonDefault);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (code){
                    case SAVE_AND_RETURN:
                        overridePendingTransition(0, 0);
                        Intent intent = new Intent(OptionsActivity.this, StartMenuActivity.class);
                        intent.putExtra("StartSpeed", startSpeed);
                        intent.putExtra("Speed level", speedLevel);
                        intent.putExtra("Speed increase coef", speedIncreaseCoef);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        startSpeed = 1.f;
                        speedLevel = 1;
                        speedIncreaseCoef = 40;
                        setSelectionForSpinner(findViewById(R.id.spinnerStartSpeed),
                                getResources().getStringArray(R.array.startSpeedSpinner), speedLevel);
                        setSelectionForSpinner(findViewById(R.id.spinnerIncreaseSpeed),
                                getResources().getStringArray(R.array.increaseSpeedSpinner), speedIncreaseCoef);
                        break;
                }
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