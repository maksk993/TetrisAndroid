package com.example.tetrisandroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.tetrisandroid.R;

public class StartMenuActivity extends AppCompatActivity {

    private enum Buttons {
        PLAY, OPTIONS, EXIT
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        for (Buttons button : Buttons.values()){
            addButtonLogic(button);
        }
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

        button.setOnClickListener(view -> {
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
        });
    }

    private void startNewActivity(Class<?> clazz){
        Intent intent = new Intent(StartMenuActivity.this, clazz);
        startActivity(intent);
        finish();
    }
}