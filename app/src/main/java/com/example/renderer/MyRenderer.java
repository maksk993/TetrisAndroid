package com.example.renderer;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {

    private int m_width;
    private int m_height;
    private float m_startSpeed;
    private int m_speedLevel;
    private int m_increaseCoef;

    public native void cppStartGame(int screenWidth, int screenHeight, float startSpeed, int speedLevel, int increaseCoef);
    public native void cppRun();
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        cppStartGame(m_width, m_height, m_startSpeed, m_speedLevel, m_increaseCoef);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES20.glViewport(0, 0, i, i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        cppRun();
    }

    public void setSizes(int width, int height){
        m_width = width;
        m_height = height;
    }

    public void setSpeed(float startSpeed, int speedLevel, int increaseCoef){
        m_startSpeed = startSpeed;
        m_speedLevel = speedLevel;
        m_increaseCoef = increaseCoef;
    }
}