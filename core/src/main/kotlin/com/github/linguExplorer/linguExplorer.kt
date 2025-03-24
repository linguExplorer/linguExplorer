package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.database.DatabaseManager
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.github.linguExplorer.models.TopicEntity
import com.github.linguExplorer.screen.*
import ktx.app.KtxGame
import ktx.app.KtxScreen

class linguExplorer : KtxGame<KtxScreen>() {
    private val userId: Int = ConfigManager.readUserId()

    override fun create() {
        println("Benutzer-ID: $userId")

        DatabaseManager()
        Gdx.app.logLevel = Application.LOG_DEBUG // Debug mode aktivieren
        //einkommentieren

        addScreen(MainMenuScreen(this)) // Setzt den GameScreen
        setScreen<MainMenuScreen>()

       //addScreen(MinigameFamilieScreen(this)) // Setzt den GameScreen
       //setScreen<MinigameFamilieScreen>()




      //  addScreen(CharacterDialog(this))
        //setScreen<CharacterDialog>()


       // addScreen(BlobDialog(this))
       //setScreen<BlobDialog>()

        //addScreen(BlobDialog(this))
        //setScreen<BlobDialog>()

    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}

var name = "Blob"
lateinit var currentTopic: TopicEntity
var topicProgress = 0.0
val userId: Int = ConfigManager.readUserId()
var saveNumber = 0
var masterVolume = 1f
var soundEffectVolume = 1f
var musicVolume = 1f

