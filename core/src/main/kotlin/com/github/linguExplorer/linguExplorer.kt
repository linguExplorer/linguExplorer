package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.database.DatabaseManager
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.github.linguExplorer.screen.*
import ktx.app.KtxGame
import ktx.app.KtxScreen

class linguExplorer : KtxGame<KtxScreen>() {
    private val userId: String? = ConfigManager.readUserId()

    override fun create() {
        println("Benutzer-ID: $userId")

        DatabaseManager()
        Gdx.app.logLevel = Application.LOG_DEBUG // Debug mode aktivieren
        //einkommentieren
       //addScreen(MinigameFamilieScreen(this)) // Setzt den GameScreen
       //setScreen<MinigameFamilieScreen>()

        addScreen(MainMenuScreen(this))
         setScreen<MainMenuScreen>()

      //  addScreen(CharacterDialog(this))
        //setScreen<CharacterDialog>()

        //addScreen(BlobDialog(this))
        //setScreen<BlobDialog>()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}

var name ="Blob"
var userId = 1
var saveNumber = 0
var masterVolume = 1f
var soundEffectVolume = 1f
var musicVolume = 1f

