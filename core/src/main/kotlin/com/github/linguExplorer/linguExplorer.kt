package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.screen.MinigameEssenScreen
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.github.linguExplorer.screen.MainMenuScreen
import com.github.linguExplorer.screen.MapScreen
import com.github.linguExplorer.screen.MinigameKleidungScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class linguExplorer : KtxGame<KtxScreen>() {

    override fun create() {
        DatabaseManager()
        Gdx.app.logLevel = Application.LOG_DEBUG // Debug mode aktivieren
       // addScreen(MainMenuScreen(this)) // Setzt den GameScreen
        //setScreen<MainMenuScreen>()

        addScreen(MinigameKleidungScreen(this))
        setScreen<MinigameKleidungScreen>()
    }


    companion object {
        const val UNIT_SCALE = 1/16f
    }
}

var userId = 123
