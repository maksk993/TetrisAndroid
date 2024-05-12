package com.example.tetrisandroid.ui.game.renderer

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet


class MySurfaceView : GLSurfaceView {
    private lateinit var renderer: MyRenderer

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        setEGLContextClientVersion(2)
        preserveEGLContextOnPause = true
        renderer = MyRenderer()
        setRenderer(renderer)
    }

    fun setSizes(width: Int, height: Int) = renderer.setSizes(width, height)
    fun setSpeed(startSpeed: Float, speedLevel: Int, increaseCoef: Int) = renderer.setSpeed(startSpeed, speedLevel, increaseCoef)
}