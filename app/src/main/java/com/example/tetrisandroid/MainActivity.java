package com.example.tetrisandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  {

    static {
        System.loadLibrary("tetrisandroid");
    }
    private MySurfaceWindow glSurfaceView;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurfaceView = (MySurfaceWindow) findViewById(R.id.MySurfaceWindow);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        glSurfaceView.setSizes(screenWidth, screenHeight);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AssetManager assetManager = getAssets();
        assetManagerInit(assetManager);

        for (int i = 1; i <= 7; i++){ // handle Buttons
            addButton(i);
        }

        dataManager = new DataManager(this);
        dataManager.getStartHighScore();
    }

    @Override
    protected void onResume(){
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause(){
        glSurfaceView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        dataManager.setNewHighScore();
        super.onStop();
    }

    private native void assetManagerInit(AssetManager assetManager);
    private native void handleTouch(int code);

    void addButton(int code){
        Button button;
        switch (code){
            case 1:
                button = findViewById(R.id.buttonLeft);
                break;
            case 2:
                button = findViewById(R.id.buttonDown);
                break;
            case 3:
                button = findViewById(R.id.buttonRight);
                break;
            case 4:
                button = findViewById(R.id.buttonUp);
                break;
            case 5:
                button = findViewById(R.id.buttonRotate);
                break;
            case 6:
                button = findViewById(R.id.buttonReset);
                break;
            default:
                button = findViewById(R.id.buttonPause);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTouch(code);
            }
        });
    }
}