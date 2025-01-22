package com.github.linguExplorer

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.screen.MainMenuScreen
import com.github.linguExplorer.screen.MapScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class linguExplorer : KtxGame<KtxScreen>() {

    override fun create() {
        DatabaseManager()
        Gdx.app.logLevel = Application.LOG_ERROR
        addScreen(MainMenuScreen(this))
        setScreen<MainMenuScreen>()
    }
    companion object {
        const val UNIT_SCALE = 1/16f
    }
}
