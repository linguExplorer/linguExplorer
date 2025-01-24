package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.linguExplorer.linguExplorer
import ktx.app.KtxScreen

class LoadingScreen(private val game: linguExplorer) : KtxScreen {
    private var batch: SpriteBatch? = null
    private var startTime = 0f
    private var elapsedTime = 1f

    override fun show() {
        batch = SpriteBatch()
        startTime = 0f
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch?.let {
            it.begin()
            // Zeichenvorgänge hier
            it.end()
        }

        if (startTime < elapsedTime) {
            startTime += delta
        } else {
            if (game!!.containsScreen<MinigameEssenScreen>()) {
                game.removeScreen<MinigameEssenScreen>()
            }
            game.addScreen(MinigameEssenScreen(game))
            game.setScreen<MinigameEssenScreen>()
        }
    }

    override fun hide() {
        batch?.dispose()
        batch = null
    }
}
