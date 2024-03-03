package com.example.tetrisandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  {

    static {
        System.loadLibrary("tetrisandroid");
    }
    private MySurfaceWindow glSurfaceView;
    private DataManager dataManager;
    enum Buttons {
        LEFT, DOWN, RIGHT, UP, ROTATE, RESET, PAUSE
    };
    private final Handler handler = new Handler();
    private final Runnable runnableForButtonDown = new Runnable() {
        @Override
        public void run() {
            handleTouch(Buttons.DOWN.ordinal());
            handler.postDelayed(runnableForButtonDown, 40);
        }
    };

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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AssetManager assetManager = getAssets();
        assetManagerInit(assetManager);

        for (Buttons button : Buttons.values()){ // handle Buttons
            addButtonLogic(button);
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

    void addButtonLogic(Buttons code){
        Button button;
        switch (code){
            case LEFT:
                button = findViewById(R.id.buttonLeft);
                break;
            case DOWN:
                button = findViewById(R.id.buttonDown);
                break;
            case RIGHT:
                button = findViewById(R.id.buttonRight);
                break;
            case UP:
                button = findViewById(R.id.buttonUp);
                break;
            case ROTATE:
                button = findViewById(R.id.buttonRotate);
                break;
            case RESET:
                button = findViewById(R.id.buttonReset);
                break;
            default:
                button = findViewById(R.id.buttonPause);
        }

        if (code == Buttons.DOWN) {
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        handler.post(runnableForButtonDown);
                        return true;
                    }
                    else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        handler.removeCallbacks(runnableForButtonDown);
                        return true;
                    }
                    return false;
                }
            });
        }
        else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleTouch(code.ordinal());
                }
            });
        }
    }
}