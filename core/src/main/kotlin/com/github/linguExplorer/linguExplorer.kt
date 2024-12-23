package com.github.linguExplorer

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.github.linguExplorer.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class linguExplorer : KtxGame<KtxScreen>() {

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG // Debug mode aktivieren
        addScreen(GameScreen()) // Setzt den GameScreen
        setScreen<GameScreen>()
    }

    companion object {
        const val UNIT_SCALE = 1/16f
    }
}
