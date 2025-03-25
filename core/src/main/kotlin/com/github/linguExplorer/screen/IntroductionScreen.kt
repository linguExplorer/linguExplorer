package com.github.linguExplorer.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
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
        "103650, Galaxis Neolingastia",
        "Im All unterwegs passiert etwas nicht Vorhersehbares.\nDas Raumschiff eines kleinen Blobs stürzt auf die Erde",
        "Es ist nun deine Mission ihm zu helfen"
    )
    private var currentTextIndex = -1  // Start at -1 to trigger initial wait
    private var textAlpha = 0f
    private var fadeState = FadeState.WAITING
    private var stageTimer = 0f

    // Timing constants
    private val INITIAL_WAIT_DURATION = 1.5f
    private val FADE_DURATION = 1.5f
    private val TEXT_DISPLAY_DURATION = 2f

    // Music
    private val backgroundMusic: Music

    // Enum to manage fade states more clearly
    private enum class FadeState {
        WAITING,
        FADING_IN,
        DISPLAYING,
        FADING_OUT
    }

    init {
        viewport = FitViewport(1920f, 1080f)
        batch = SpriteBatch()

        // Load VCR OSD Mono font
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))
        font.data.setScale(0.4f)  // Slightly reduced scale for multi-line text

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Hintergrundmusik/Hintergrundmusik_Introduction.mp3"))
        backgroundMusic.isLooping = true
        backgroundMusic.volume = 0.4f
    }

    override fun show() {
        backgroundMusic.play()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        updateFadeSequence(delta)

        batch.projectionMatrix = viewport.camera.combined
        viewport.apply()

        batch.begin()
        renderFadingText()
        batch.end()
    }

    private fun updateFadeSequence(delta: Float) {
        stageTimer += delta

        when (fadeState) {
            FadeState.WAITING -> {
                // Initial wait or between texts
                if (stageTimer >= INITIAL_WAIT_DURATION) {
                    // Move to next text if possible
                    currentTextIndex++
                    if (currentTextIndex >= texts.size) {
                        // End of texts, move to map screen
                        game.addScreen(MapScreen(game, 30f, 30f))
                        game.setScreen<MapScreen>()
                        return
                    }

                    // Start fading in
                    fadeState = FadeState.FADING_IN
                    stageTimer = 0f
                    textAlpha = 0f
                }
            }
            FadeState.FADING_IN -> {
                // Fading in
                textAlpha = (stageTimer / FADE_DURATION).coerceAtMost(1f)
                if (stageTimer >= FADE_DURATION) {
                    fadeState = FadeState.DISPLAYING
                    stageTimer = 0f
                }
            }
            FadeState.DISPLAYING -> {
                // Hold text for display duration
                if (stageTimer >= TEXT_DISPLAY_DURATION) {
                    fadeState = FadeState.FADING_OUT
                    stageTimer = 0f
                }
            }
            FadeState.FADING_OUT -> {
                // Fading out
                textAlpha = 1f - (stageTimer / FADE_DURATION).coerceAtMost(1f)
                if (stageTimer >= FADE_DURATION) {
                    // Reset for next text
                    fadeState = FadeState.WAITING
                    stageTimer = 0f
                }
            }
        }
    }

    private fun renderFadingText() {
        // Only render if there are texts left
        if (currentTextIndex >= 0 && currentTextIndex < texts.size) {
            font.color.a = textAlpha
            val text = texts[currentTextIndex]

            // Measure text to center it
            val glyphLayout = GlyphLayout()
            glyphLayout.setText(font, text)
            val x = (viewport.worldWidth - glyphLayout.width) / 2
            val y = (viewport.worldHeight + glyphLayout.height) / 2

            font.draw(
                batch,
                text,
                x,
                y
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
