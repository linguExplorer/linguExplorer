package com.github.linguExplorer.screen;

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport

class LoadingScreenRenderer {

    fun renderAnimatedText(batch: SpriteBatch, font: BitmapFont, viewport: Viewport, text: String, x: Float, y: Float, delta: Float, alpha: Float, fadingIn: Boolean
    ) {
        var newAlpha = alpha
        var newFadingIn = fadingIn

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
        shapeRenderer.rect(0f,0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
        font.color.set(1f, 1f, 1f, newAlpha)
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        font.draw(batch, text, x, y)
        batch.end()
    }
}
