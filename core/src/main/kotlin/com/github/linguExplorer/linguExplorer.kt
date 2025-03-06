package com.github.linguExplorer

import com.badlogic.gdx.Game
import com.github.linguExplorer.database.DatabaseManager
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.linguExplorer.screen.*
import ktx.app.KtxGame
import ktx.app.KtxScreen

class linguExplorer : KtxGame<KtxScreen>() {

    lateinit var stage: Stage  // Stage als Klassenmember

    override fun create() {
        DatabaseManager()
        Gdx.app.logLevel = Application.LOG_DEBUG

        stage = Stage(ScreenViewport())
       // Gdx.input.inputProcessor = stage

        addScreen(MinigameSchuleScreen())
        setScreen<MinigameSchuleScreen>()
    }


    companion object {
        const val UNIT_SCALE = 1/16f
    }
}

var userId = 1
var saveNumber = 1
