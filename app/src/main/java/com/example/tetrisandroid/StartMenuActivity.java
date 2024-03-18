package com.example.tetrisandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.datamanager.DataManager;

public class StartMenuActivity extends AppCompatActivity {

    private enum Buttons {
        PLAY, OPTIONS, EXIT
    };
    private DataManager dataManager;
    private float startSpeed;
    private int speedLevel;
    private int speedIncreaseCoef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        dataManager = new DataManager(this);
        Intent intent = getIntent();
        if (intent != null){
            startSpeed = intent.getFloatExtra("StartSpeed", dataManager.getStartSpeed());
            speedLevel = intent.getIntExtra("Speed level", dataManager.getSpeedLevel());
            speedIncreaseCoef = intent.getIntExtra("Speed increase coef", dataManager.getSpeedIncreaseCoef());
            dataManager.setStartSpeed(startSpeed);
            dataManager.setSpeedLevel(speedLevel);
            dataManager.setSpeedIncreaseCoef(speedIncreaseCoef);
        }

        for (Buttons button : Buttons.values()){
            addButtonLogic(button);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void addButtonLogic(Buttons code){
        Button button;
        switch (code){
            case PLAY:
                button = findViewById(R.id.buttonPlay);
                break;
            case OPTIONS:
                button = findViewById(R.id.buttonOptions);
                break;
            default:
                button = findViewById(R.id.buttonExit);
                break;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (code){
                    case PLAY:
                        startNewActivity(GameActivity.class);
                        break;
                    case OPTIONS:
                        overridePendingTransition(0, 0);
                        startNewActivity(OptionsActivity.class);
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        finish();
                        break;
                }
            }
        });
    }

    private void startNewActivity(Class<?> clazz){
        Intent intent;
        intent = new Intent(StartMenuActivity.this, clazz);
        intent.putExtra("StartSpeed", startSpeed);
        intent.putExtra("Speed level", speedLevel);
        intent.putExtra("Speed increase coef", speedIncreaseCoef);
        startActivity(intent);
        finish();
    }
}