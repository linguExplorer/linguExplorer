package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport

class LoadingScreenRenderer {

    // Liste von Sätzen, die nacheinander angezeigt werden
    private val loadingMessages = listOf(
        "Lade Ressourcen...",
        "Wusstest du schon: Dein Spiel wird von uns automatisch gespeichert!",
        "Noch ein bisschen Geduld...",
        "Tipp: Achte immer auf die Zeit, wenn du ein Minispiel spielst!",
        "Fast fertig..."
    )

    // Variable für die vergangene Zeit
    private var loadingTime = 0f
    private var loadingMessage = ""
    private var dotAnimationTime = 0f
    private var dotCount = 0

    fun renderAnimatedText(batch: SpriteBatch, font: BitmapFont, glyphLayout: GlyphLayout, viewport: Viewport, text: String, delta: Float, alpha: Float, fadingIn: Boolean
    ) {
        var newAlpha = alpha
        var newFadingIn = fadingIn

        // Alpha-Animation
        if (newFadingIn) {
            newAlpha += delta * 0.1f
            if (newAlpha >= 1) {
                newAlpha = 1f
            }
        } else {
            newAlpha -= delta * 0.1f
            if (newAlpha <= 0) {
                newAlpha = 0f
            }
        }

        val shapeRenderer = ShapeRenderer()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.BLACK
        shapeRenderer.rect(0f, 0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        font.color.set(1f, 1f, 1f, newAlpha)
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined

        dotAnimationTime += delta
        if (dotAnimationTime >= 0.5f) {
            dotAnimationTime = 0f
            dotCount = (dotCount + 1) % 4
        }

        val animatedDots = ".".repeat(dotCount)

        batch.begin()
        font.data.setScale(0.4f)
        glyphLayout.setText(font, text + animatedDots)
        var x = (viewport.worldWidth - glyphLayout.width) / 2
        var y = (viewport.worldHeight + glyphLayout.height) / 2 + 30f
        font.draw(batch, text + animatedDots, x, y)

        loadingTime += delta

        val messageIndex = (loadingTime / 5).toInt()
        if (loadingTime >= 2f) {
            loadingMessage = if (messageIndex < loadingMessages.size) {
                loadingMessages[messageIndex]
            } else {
                loadingMessages.last()
            }
        }

        val originalScaleX = font.data.scaleX
        val originalScaleY = font.data.scaleY
        font.data.setScale(0.2f)

        glyphLayout.setText(font, loadingMessage)
        x = (viewport.worldWidth - glyphLayout.width) / 2
        y -= glyphLayout.height + 50f
        font.draw(batch, loadingMessage, x, y)

        // Schriftgröße zurücksetzen
        font.data.setScale(originalScaleX, originalScaleY)

       /* batch.draw(
            currentFrame,
            popUpPosition.x + popUpSize.x - 250f,
            viewport.worldHeight / 2 - 250f,
            200f,
            200f
        )*/
        batch.end()
    }
}
