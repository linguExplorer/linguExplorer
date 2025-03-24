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



    fun render(batch: SpriteBatch, viewport: Viewport, delta: Float): Boolean {
        // Wenn bereits fertig, nichts mehr rendern
        if (isFinished) {
            return true
        }

        // Textur laden, wenn noch nicht geschehen
        if (lockTexture == null) {
            try {
                lockTexture = Texture(Gdx.files.internal(texturePath))
            } catch (e: Exception) {
                Gdx.app.error("LockScreenRenderer", "Fehler beim Laden der Textur: $e")
                isFinished = true
                return true
            }
        }

        // Sound abspielen, wenn noch nicht geschehen
        if (!soundPlayed) {
            lockSound.play(0.7f * masterVolume * soundEffectVolume)
            soundPlayed = true
        }

        // Zeit aktualisieren
        displayTime -= delta
        if (displayTime <= 0f) {
            isFinished = true
            return true
        }

        // Hintergrund zeichnen
        val shapeRenderer = ShapeRenderer()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(0f, 0f, 0f, 0.7f)
        shapeRenderer.rect(0f, 0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        // Viewport anwenden
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined

        // Textur zeichnen
        batch.begin()
        lockTexture?.let { texture ->
            val scale = 0.55f
            val scaledWidth = texture.width * scale
            val scaledHeight = texture.height * scale

            val x = (viewport.worldWidth - scaledWidth) / 2
            val y = (viewport.worldHeight - scaledHeight) / 2

            batch.draw(
                texture,        // Die Textur
                x, y,           // Position (x, y)
                scaledWidth,    // Breite
                scaledHeight    // Höhe
            )
        }
        batch.end()

        return isFinished
    }

    fun setOnResumeClicked(callback: () -> Unit) {
        onResumeClicked = {
            showLock = false  // Hier wird menuSet auf false gesetzt
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
