package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.masterVolume
import com.github.linguExplorer.repositories.TopicRepository
import com.github.linguExplorer.repositories.UserProgressRepository
import com.github.linguExplorer.saveNumber
import com.github.linguExplorer.soundEffectVolume
import com.github.linguExplorer.userId
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class GameUnlockScreenRenderer {

    private var loadingTime = 5f
    private var unlockSound: Sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Soundeffekte/unlock.mp3"))
    private var soundPlayed = false
    private var unlockTexture: Texture? = null
    private val glyphLayout = GlyphLayout()
    private val executor: ExecutorService = Executors.newFixedThreadPool(1)

    private var isDoneTexture = true
    private var shouldSwitchTexture = false
    private var textureRequested = false
    private var futureTexturePath: String? = null

    // Flag to indicate when the rendering should be finished
    private var isFinished = false
    // Additional time for unlock animation
    private var unlockAnimationTime = 3f

    // Method to check if the renderer is finished
    fun isFinished(): Boolean {
        return isFinished
    }

    fun renderTopicUpdate(batch: SpriteBatch, viewport: Viewport, delta: Float, alpha: Float, fadingIn: Boolean): Boolean {
        // If already finished, don't render anything
        if (isFinished) {
            return true
        }

        // Texture-Loading-Logik
        if (!textureRequested && unlockTexture == null) {
            textureRequested = true

            // Pfad ermitteln und für später speichern (OpenGL-sicher)
            executor.execute {
                try {
                    val topicName = TopicRepository().getTopicById(
                        UserProgressRepository().getLatestUserProgress(userId, saveNumber)?.topicId
                    )?.name

                    if (topicName != null) {
                        futureTexturePath = "graphics/map-objects/(Un)lock/Done_$topicName.png"
                    }
                } catch (e: Exception) {
                    Gdx.app.error("GameUnlockScreenRenderer", "Fehler beim Ermitteln des Topics: $e")
                }
            }
        }

        // Den Pfad verwenden, um im Hauptthread die Textur zu laden
        if (futureTexturePath != null && unlockTexture == null) {
            try {
                unlockTexture = Texture(Gdx.files.internal(futureTexturePath!!))
                futureTexturePath = null
            } catch (e: Exception) {
                Gdx.app.error("GameUnlockScreenRenderer", "Fehler beim Laden der Textur: $e")
                futureTexturePath = null
            }
        }

        // Spiele den Sound ab, wenn Animation beginnt
        if (fadingIn && !soundPlayed && unlockTexture != null) {
            unlockSound.play(0.7f * masterVolume * soundEffectVolume)
            soundPlayed = true
        }

        // Timer aktualisieren wenn Textur vorhanden
        if (unlockTexture != null) {
            loadingTime -= delta

            // Texture-Wechsel vorbereiten nach 5 Sekunden
            if (loadingTime <= 0f && isDoneTexture && !shouldSwitchTexture) {
                shouldSwitchTexture = true

                // Neuen Texturpfad im Thread ermitteln
                executor.execute {
                    try {
                        val upcomingTopicName = TopicRepository().getTopicById(
                            UserProgressRepository().getUpcomingUserProgress(userId, saveNumber)
                        )?.name

                        if (upcomingTopicName != null) {
                            futureTexturePath = "graphics/map-objects/(Un)lock/Unlock_$upcomingTopicName.png"
                        }
                    } catch (e: Exception) {
                        Gdx.app.error("GameUnlockScreenRenderer", "Fehler beim Ermitteln des Upcoming Topics: $e")
                    }
                }
            }

            // Textur im Hauptthread wechseln, wenn bereit
            if (shouldSwitchTexture && futureTexturePath != null) {
                unlockTexture?.dispose()

                try {
                    unlockTexture = Texture(Gdx.files.internal(futureTexturePath!!))
                    futureTexturePath = null
                    isDoneTexture = false
                    shouldSwitchTexture = false
                    unlockSound.play(0.7f * masterVolume * soundEffectVolume)
                    // Reset animation time for unlock texture
                    unlockAnimationTime = 5f
                } catch (e: Exception) {
                    Gdx.app.error("GameUnlockScreenRenderer", "Fehler beim Laden der Unlock-Textur: $e")
                    futureTexturePath = null
                    shouldSwitchTexture = false
                }
            }

            // Update the unlock animation timer if we're showing the unlock texture
            if (!isDoneTexture) {
                unlockAnimationTime -= delta

                // Mark as finished after the unlock animation is done
                if (unlockAnimationTime <= 0f) {
                    isFinished = true
                    return true
                }
            }
        }

        // Hintergrund zeichnen
        val shapeRenderer = ShapeRenderer()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(0f, 0f, 0f, alpha * 0.7f)
        shapeRenderer.rect(0f, 0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined

        batch.begin()

        unlockTexture?.let { texture ->
            val scale = 0.55f
            val scaledWidth = texture.width * scale
            val scaledHeight = texture.height * scale

            val x = (viewport.worldWidth - (scaledWidth)) / 2
            val y = (viewport.worldHeight - (scaledHeight)) / 2

            batch.setColor(1f, 1f, 1f, alpha)

            batch.draw(
                texture,           // Die Textur
                x, y,              // Position (x, y)
                scaledWidth,       // Breite
                scaledHeight       // Höhe
            )
        }

        batch.end()

        return isFinished
    }

    fun resetSound() {
        soundPlayed = false
    }

    fun reset() {
        soundPlayed = false
        loadingTime = 5f
        unlockAnimationTime = 5f
        isDoneTexture = true
        shouldSwitchTexture = false
        textureRequested = false
        futureTexturePath = null
        isFinished = false
    }

    fun dispose() {
        unlockTexture?.dispose()
        unlockSound.dispose()
        executor.shutdown()
    }
}
