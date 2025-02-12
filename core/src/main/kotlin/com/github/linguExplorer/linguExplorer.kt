package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.screen.GameScreen
import com.github.linguExplorer.screen.PhrasenheftScreen

class linguExplorer : Game() {
    override fun create() {
        DatabaseManager()
        this.screen = PhrasenheftScreen() // Setzt den GameScreen
    }
}
