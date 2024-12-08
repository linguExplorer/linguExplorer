package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.screen.GameScreen

class linguExplorer : Game() {
    override fun create() {
        this.screen = GameScreen() // Setzt den GameScreen
    }
}
