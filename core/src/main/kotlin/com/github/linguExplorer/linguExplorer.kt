package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.screen.GameScreen

class linguExplorer : Game() {
    override fun create() {
        DatabaseManager()
        this.screen = GameScreen() // Setzt den GameScreen
    }
}

var userId = 1
