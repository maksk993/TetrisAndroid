package com.example.tetrisandroid.ui.game.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRenderer: GLSurfaceView.Renderer {
    private var width = 0
    private var height = 0
    private var startSpeed = 0f
    private var speedLevel = 0
    private var increaseCoef = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) = cppStartGame(width, height, startSpeed, speedLevel, increaseCoef)
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) = GLES20.glViewport(0, 0, width, height)
    override fun onDrawFrame(gl: GL10?) = cppRun()

    fun setSizes(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun setSpeed(startSpeed: Float, speedLevel: Int, increaseCoef: Int) {
        this.startSpeed = startSpeed
        this.speedLevel = speedLevel
        this.increaseCoef = increaseCoef
    }

    private external fun cppStartGame(screenWidth: Int, screenHeight: Int, startSpeed: Float, speedLevel: Int, increaseCoef: Int)
    private external fun cppRun()
}