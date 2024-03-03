package com.example.tetrisandroid;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {

    private int m_width;
    private int m_height;

    public native void startGame(int screenWidth, int screenHeight);
    public native void loadGame();
    public native void run();
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        startGame(m_width, m_height);
        loadGame();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES20.glViewport(0, 0, i, i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        run();
    }

    public void setSizes(int width, int height){
        m_width = width;
        m_height = height;
    }
}