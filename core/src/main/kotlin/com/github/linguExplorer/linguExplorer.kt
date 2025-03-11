package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.screen.PhrasenheftScreen
import com.github.linguExplorer.screen.MinigameEssenScreen
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.github.linguExplorer.screen.GameMenuScreen
import com.github.linguExplorer.screen.MainMenuScreen
import com.github.linguExplorer.screen.MapScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class linguExplorer() : KtxGame<KtxScreen>() {

    private val userId: String? = ConfigManager.readUserId()

    override fun create() {

        println("Benutzer-ID: $userId")

        DatabaseManager()
        Gdx.app.logLevel = Application.LOG_DEBUG // Debug mode aktivieren
        addScreen(MainMenuScreen(this)) // Setzt den GameScreen
        setScreen<MainMenuScreen>()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}

var userId = 1
