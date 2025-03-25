package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.masterVolume
import com.github.linguExplorer.soundEffectVolume

class LockScreenRenderer {
    private var displayTime = 3f // 3 Sekunden Anzeigedauer
    private var lockTexture: Texture? = null
    private var lockSound: Sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Soundeffekte/lock.mp3"))
    private var soundPlayed = false
    private var isFinished = false
    private var texturePath = "graphics/map-objects/(Un)lock/lock.png"
    private var onResumeClicked: () -> Unit = {}
    private var showLock = true

    /**
     * Spielt den Lock-Sound einmalig ab.
     * Muss explizit aufgerufen werden, bevor render() verwendet wird.
     */
    fun playLockSound() {
        if (!soundPlayed) {
            lockSound.play(0.7f * masterVolume * soundEffectVolume)
            soundPlayed = true
        }
    }

    fun render(batch: SpriteBatch, viewport: Viewport, delta: Float): Boolean {
        if (isFinished) {
            return true
        }

        if (lockTexture == null) {
            try {
                lockTexture = Texture(Gdx.files.internal(texturePath))
            } catch (e: Exception) {
                Gdx.app.error("LockScreenRenderer", "Fehler beim Laden der Textur: $e")
                isFinished = true
                return true
            }
        }

        displayTime -= delta
        if (displayTime <= 0f) {
            isFinished = true
            return true
        }

        val shapeRenderer = ShapeRenderer()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(0f, 0f, 0f, 0.7f)
        shapeRenderer.rect(0f, 0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined

        batch.begin()
        lockTexture?.let { texture ->
            val scale = 0.55f
            val scaledWidth = texture.width * scale
            val scaledHeight = texture.height * scale

            val x = (viewport.worldWidth - scaledWidth) / 2
            val y = (viewport.worldHeight - scaledHeight) / 2

            batch.draw(
                texture,
                x, y,
                scaledWidth,
                scaledHeight
            )
        }
        batch.end()

        return isFinished
    }

    fun setOnResumeClicked(callback: () -> Unit) {
        onResumeClicked = {
            showLock = false
            callback()
        }
    }

    fun isFinished(): Boolean {
        return isFinished
    }

    fun reset() {
        displayTime = 3f
        soundPlayed = false
        isFinished = false
    }

    fun dispose() {
        lockTexture?.dispose()
        lockSound.dispose()
    }
}
