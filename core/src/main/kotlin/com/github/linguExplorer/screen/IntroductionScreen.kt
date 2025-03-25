package com.github.linguExplorer.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.linguExplorer.*
import ktx.app.KtxScreen

class IntroductionScreen(private val game: linguExplorer) : KtxScreen {
    private val batch: SpriteBatch
    private val viewport: FitViewport
    private val font: BitmapFont

    // Text and fade properties
    private val texts = listOf(
        "Erste Nachricht",
        "Zweite Überraschung",
        "Letzte Botschaft"
    )
    private var currentTextIndex = 0
    private var textAlpha = 0f
    private var fadeDirection = 1 // 1 = fade in, -1 = fade out
    private var fadeTimer = 0f

    // Timing constants
    private val FADE_DURATION = 2f
    private val TEXT_DISPLAY_DURATION = 3f

    // Music
    private val backgroundMusic: Music

    init {
        viewport = FitViewport(1920f, 1080f)
        batch = SpriteBatch()

        // Load VCR OSD Mono font
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))
        font.data.setScale(0.5f)

      
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Hintergrundmusik/Hintergrundmusik_Introduction.mp3"))
        backgroundMusic.isLooping = true
        backgroundMusic.volume = 0.5f
    }

    override fun show() {
        backgroundMusic.play()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        updateFade(delta)


        batch.projectionMatrix = viewport.camera.combined
        viewport.apply()

        batch.begin()
        renderFadingText()
        batch.end()
    }

    private fun updateFade(delta: Float) {
        fadeTimer += delta

        // Fade in/out logic
        if (fadeDirection == 1) {
            // Fading in
            textAlpha = (fadeTimer / FADE_DURATION).coerceAtMost(1f)
            if (fadeTimer >= FADE_DURATION) {
                fadeDirection = -1
                fadeTimer = 0f
            }
        } else {
            // Fading out
            textAlpha = 1f - (fadeTimer / FADE_DURATION).coerceAtMost(1f)
            if (fadeTimer >= FADE_DURATION) {
                // Move to next text
                currentTextIndex++
                fadeDirection = 1
                fadeTimer = 0f

                // Reset or end
                if (currentTextIndex >= texts.size) {
                    game.addScreen(MapScreen(game, 30f, 30f))  // Fügt den MapScreen hinzu
                    game.setScreen<MapScreen>()  // Setzt den MapScreen als aktuellen Screen
                }
            }
        }
    }

    private fun renderFadingText() {
        // Only render if there are texts left
        if (currentTextIndex < texts.size) {
            font.color.a = textAlpha
            val text = texts[currentTextIndex]
            val textWidth = font.draw(batch, text, 0f, 0f).width
            font.draw(
                batch,
                text,
                (viewport.worldWidth - textWidth) / 2,
                viewport.worldHeight / 2
            )
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun pause() {
        backgroundMusic.pause()
    }

    override fun resume() {
        backgroundMusic.play()
    }

    override fun hide() {
        backgroundMusic.stop()
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        backgroundMusic.dispose()
    }
}
