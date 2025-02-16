package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.linguExplorer.linguExplorer
import ktx.app.KtxScreen

class GameMenuScreen(val game: linguExplorer) : KtxScreen {

    private lateinit var batch: SpriteBatch
    private lateinit var boxTexture: Texture
    private var boxX: Float = 0f
    private var boxY: Float = 0f

    override fun show() {
        batch = SpriteBatch()
        boxTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/box.png"))

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()

        val boxWidth = screenWidth * 0.33f  // % von Bildschirmbreite
        val boxHeight = screenHeight * 0.6f // % von Bildschirmhöhe

        val boxX = (screenWidth - boxWidth) / 2f
        var boxY = (screenHeight - boxHeight) / 2f

        val yOffset = screenHeight * 0.10f // 10% der Bildschirmhöhe als Offset

        boxY -= yOffset // Verschiebe die Box nach unten

        batch.begin()
        batch.draw(boxTexture, boxX, boxY, boxWidth, boxHeight)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        boxTexture.dispose()
    }
}
