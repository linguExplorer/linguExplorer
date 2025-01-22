package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.screen.MinigameEssenScreen

class linguExplorer : Game() {
    override fun create() {
        DatabaseManager()
        this.screen = MinigameEssenScreen() // Setzt den GameScreen
    }
}

var userId = 1
