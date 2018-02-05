package ru.pepelaz.minesweeper

import android.app.Activity
import android.os.Bundle
import android.view.Window

class GameActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameView(this))
    }
}
