package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.linguExplorer.linguExplorer
import ktx.app.KtxScreen
import kotlin.reflect.KClass
import com.github.linguExplorer.screen.MinigameEssenScreen



class LoadingScreen(private val game: linguExplorer) : KtxScreen {
    private val batch = SpriteBatch()
    private var startTime: Long = 0

    override fun show() {

        startTime = System.currentTimeMillis()



    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()


        batch.end()


        if (isLoaded()) {


            game.addScreen(MinigameEssenScreen(game))

            game.setScreen<MinigameEssenScreen>()
        }
    }

    private fun isLoaded(): Boolean {

        return true
    }

    override fun hide() {
        batch.dispose()
    }
}
