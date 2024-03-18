package com.example.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MySurfaceView extends GLSurfaceView {
    private MyRenderer m_renderer;

    public MySurfaceView(Context context){
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        m_renderer = new MyRenderer();
        setRenderer(m_renderer);
    }

    public void setSizes(int width, int height){
        m_renderer.setSizes(width, height);
    }
    public void setSpeed(float startSpeed, int speedLevel, int increaseCoef){
        m_renderer.setSpeed(startSpeed, speedLevel, increaseCoef);
    }
}