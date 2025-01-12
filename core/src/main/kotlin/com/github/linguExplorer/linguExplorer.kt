package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.screen.MainMenuScreen
import com.github.linguExplorer.screen.GameScreen

class linguExplorer : Game() {

    override fun create() {
        setScreen(MainMenuScreen(this)) // Setze den ersten Screen als MainMenuScreen
    }

    fun startGame() {
        setScreen(GameScreen()) // Wechsle zu GameScreen, wenn der Spieler ein neues Spiel startet
    }
}
