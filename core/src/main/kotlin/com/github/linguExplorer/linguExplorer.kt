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
        //einkommentieren
        //addScreen(MainMenuScreen(this)) // Setzt den GameScreen
        //setScreen<MainMenuScreen>()

        //addScreen(MapScreen(this, 30f, 30f))
        //setScreen<MapScreen>()

        //addScreen(CharacterDialog(this))
        //setScreen<CharacterDialog>()

        addScreen(BlobDialog(this))
        setScreen<BlobDialog>()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}

var userId = 1
var saveNumber = 0
var masterVolume = 1f
var soundEffectVolume = 1f
var musicVolume = 1f

