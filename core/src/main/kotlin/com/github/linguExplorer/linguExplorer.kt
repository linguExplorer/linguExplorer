package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.database.DatabaseManager
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.github.linguExplorer.screen.*
import ktx.app.KtxGame
import ktx.app.KtxScreen

class linguExplorer : KtxGame<KtxScreen>() {

    override fun create() {
        DatabaseManager()
        Gdx.app.logLevel = Application.LOG_DEBUG // Debug mode aktivieren
       // addScreen(MainMenuScreen(this)) // Setzt den GameScreen
        //setScreen<MainMenuScreen>()

        //addScreen(MinigameKleidungScreen(this))
        //setScreen<MinigameKleidungScreen>()

        // Screens
        addScreen(LoadingScreen(this))
        addScreen(MainMenuScreen(this))
        addScreen(MapScreen(this))
        addScreen(MinigameEssenScreen(this))
        addScreen(MinigameKleidungScreen(this))
        addScreen(MinigameKleidungShoppenScreen(this))

        // Startscreen
        setScreen<MinigameKleidungShoppenScreen>()
    }


    companion object {
        const val UNIT_SCALE = 1/16f
    }
}

var userId = 123
