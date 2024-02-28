package com.example.tetrisandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MySurfaceWindow extends GLSurfaceView {
    private MyRenderer mRenderer;

    public MySurfaceWindow(Context context){
        super(context);
        init();
    }

    public MySurfaceWindow(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        mRenderer = new MyRenderer();
        setRenderer(mRenderer);
    }

    public void setSizes(int width, int height){
        mRenderer.setSizes(width, height);
    }
}