package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.Input

class GameMenuScreen {

    private lateinit var batch: SpriteBatch
    private lateinit var boxTexture: Texture
    private var boxX: Float = 0f
    private var boxY: Float = 0f
    private lateinit var font: BitmapFont
    private lateinit var resumeTexture: Texture
    private lateinit var soundSettingsTexture: Texture
    private lateinit var quitGameTexture: Texture
    private val buttonSize = Vector2(300f, 90f)
    private val buttonSpacing = 40f
    private lateinit var wordmarkTexture: Texture
    private val wordmarkScale = 0.2f
    private lateinit var applyTexture: Texture
    private val applyButtonWidth = 230f
    private var showSoundSettings = false
    private lateinit var redXTexture: Texture
    private val redXSize = Vector2(60f, 60f)
    private lateinit var barnoneTexture: Texture
    private lateinit var barfullTexture: Texture
    private val barnoneWidth = 480f
    private val barnoneHeight = 25f
    private val headingLeftPadding = 50f
    private lateinit var circleTexture: Texture
    private val circleSize = Vector2(50f, 50f)
    private var masterCircleX: Float = 0f
    private var soundeffectsCircleX: Float = 0f
    private var musicCircleX: Float = 0f
    private var isDraggingMaster: Boolean = false
    private var isDraggingSoundeffects: Boolean = false
    private var isDraggingMusic: Boolean = false
    private var masterCircleXOffset: Float = 0f
    private var soundeffectsCircleXOffset: Float = 0f
    private var musicCircleXOffset: Float = 0f
    private var masterBarnoneY: Float = 0f
    private var soundeffectsBarnoneY: Float = 0f
    private var musicBarnoneY: Float = 0f

    var isMenuVisible: Boolean = false

    init {
        batch = SpriteBatch()
        boxTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/box.png"))
        font = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
        resumeTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/resume.png"))
        soundSettingsTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/soundsettings.png"))
        quitGameTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/quitgame.png"))
        wordmarkTexture = Texture(Gdx.files.internal("xx_Images/wordmark/wordmark_scaled.png"))
        applyTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/apply.png"))
        redXTexture = Texture(Gdx.files.internal("xx_Images/Buttons/red_X.png"))
        barnoneTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/barnone.png"))
        barfullTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/barfull.png"))
        circleTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/circle.png"))
    }

    fun render(batch: SpriteBatch, font: BitmapFont, glyphLayout: GlyphLayout, viewport: Viewport, shapeRenderer: ShapeRenderer, onMenuClose: () -> Unit) {
        if (!isMenuVisible) return

        //Gdx.gl.glClearColor(0f, 0f, 0f, 0.5f)
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()

        val boxWidth = screenWidth * 0.30f
        val boxHeight = screenHeight * 0.6f

        boxX = (screenWidth - boxWidth) / 2f
        boxY = (screenHeight - boxHeight) / 2f - screenHeight * 0.10f

        batch.begin()

        // Wordmark zeichnen
        val wordmarkWidth = wordmarkTexture.width.toFloat() * wordmarkScale
        val wordmarkHeight = wordmarkTexture.height.toFloat() * wordmarkScale
        val wordmarkX = (screenWidth - wordmarkWidth) / 2f
        val wordmarkY = boxY + boxHeight + (screenHeight - (boxY + boxHeight)) / 2 - wordmarkHeight / 2

        batch.draw(wordmarkTexture, wordmarkX, wordmarkY, wordmarkWidth, wordmarkHeight)

        batch.draw(boxTexture, boxX, boxY, boxWidth, boxHeight)

        font.color = Color.BLACK
        font.data.setScale(0.4f)
        val headlineText = if (showSoundSettings) "SOUND" else "OPTIONS"
        val headlineLayout2 = GlyphLayout(font, headlineText)
        val headlineTextX = boxX + (boxWidth - headlineLayout2.width) / 2
        val headlineTextY = boxY + boxHeight - 80
        font.draw(batch, headlineText, headlineTextX, headlineTextY)

        val resumeButtonX = boxX + (boxWidth - buttonSize.x) / 2
        val resumeButtonY = headlineTextY - 120f - buttonSize.y

        val soundSettingsButtonX = boxX + (boxWidth - buttonSize.x) / 2
        val soundSettingsButtonY = resumeButtonY - buttonSize.y - buttonSpacing

        val quitGameButtonX = boxX + (boxWidth - buttonSize.x) / 2
        val quitGameButtonY = soundSettingsButtonY - buttonSize.y - buttonSpacing

        val applyButtonX = boxX + (boxWidth - applyButtonWidth) / 2
        val applyButtonY = boxY - buttonSize.y / 2

        val redXButtonX = boxX + boxWidth - redXSize.x - 20
        val redXButtonY = boxY + boxHeight - redXSize.y - 20

        if (!showSoundSettings) {
            batch.draw(resumeTexture, resumeButtonX, resumeButtonY, buttonSize.x, buttonSize.y)
            batch.draw(soundSettingsTexture, soundSettingsButtonX, soundSettingsButtonY, buttonSize.x, buttonSize.y)
            batch.draw(quitGameTexture, quitGameButtonX, quitGameButtonY, buttonSize.x, buttonSize.y)
        } else {
            batch.draw(redXTexture, redXButtonX, redXButtonY, redXSize.x, redXSize.y)

            var currentY = headlineTextY - 100f
            font.data.setScale(0.2f)

            val headingSpacing = 50f
            val sectionSpacing = 45f

            val masterText = "MASTER"
            val masterLayout = GlyphLayout(font, masterText)
            val masterTextX = boxX + headingLeftPadding
            font.draw(batch, masterText, masterTextX, currentY)
            currentY -= masterLayout.height + headingSpacing
            masterBarnoneY = currentY

            batch.draw(barnoneTexture, boxX + headingLeftPadding, currentY, barnoneWidth, barnoneHeight)

            val minColoredBarWidth = barnoneWidth * 0.1f
            var masterColoredBarWidth = masterCircleX - (boxX + headingLeftPadding) + circleSize.x / 3
            masterColoredBarWidth = Math.max(masterColoredBarWidth, minColoredBarWidth)

            val scaleX = masterColoredBarWidth / barfullTexture.width

            batch.draw(
                barfullTexture,
                boxX + headingLeftPadding,
                currentY,
                masterColoredBarWidth,
                barnoneHeight,
                0,
                0,
                barfullTexture.width,
                barfullTexture.height,
                false,
                false
            )

            masterCircleX = boxX + headingLeftPadding + masterCircleXOffset
            masterCircleX = MathUtils.clamp(masterCircleX, boxX + headingLeftPadding, boxX + headingLeftPadding + barnoneWidth - circleSize.x)
            batch.draw(circleTexture, masterCircleX, masterBarnoneY + barnoneHeight / 2f - circleSize.y / 2f, circleSize.x, circleSize.y)

            currentY -= barnoneHeight + sectionSpacing

            val soundeffectsText = "SOUNDEFFECTS"
            val soundeffectsLayout = GlyphLayout(font, soundeffectsText)
            val soundeffectsTextX = boxX + headingLeftPadding
            font.draw(batch, soundeffectsText, soundeffectsTextX, currentY)
            currentY -= soundeffectsLayout.height + headingSpacing
            soundeffectsBarnoneY = currentY

            batch.draw(barnoneTexture, boxX + headingLeftPadding, currentY, barnoneWidth, barnoneHeight)

            val minSoundeffectsColoredBarWidth = barnoneWidth * 0.1f
            var soundeffectsColoredBarWidth = soundeffectsCircleX - (boxX + headingLeftPadding) + circleSize.x / 3
            soundeffectsColoredBarWidth = Math.max(soundeffectsColoredBarWidth, minSoundeffectsColoredBarWidth)

            val soundeffectsScaleX = soundeffectsColoredBarWidth / barfullTexture.width

            batch.draw(
                barfullTexture,
                boxX + headingLeftPadding,
                currentY,
                soundeffectsColoredBarWidth,
                barnoneHeight,
                0,
                0,
                barfullTexture.width,
                barfullTexture.height,
                false,
                false
            )

            soundeffectsCircleX = boxX + headingLeftPadding + soundeffectsCircleXOffset
            soundeffectsCircleX = MathUtils.clamp(soundeffectsCircleX, boxX + headingLeftPadding, boxX + headingLeftPadding + barnoneWidth - circleSize.x)
            batch.draw(circleTexture, soundeffectsCircleX, soundeffectsBarnoneY + barnoneHeight / 2f - circleSize.y / 2f, circleSize.x, circleSize.y)

            currentY -= barnoneHeight + sectionSpacing

            val musicText = "MUSIC"
            val musicLayout = GlyphLayout(font, musicText)
            val musicTextX = boxX + headingLeftPadding
            font.draw(batch, musicText, musicTextX, currentY)
            currentY -= musicLayout.height + headingSpacing
            musicBarnoneY = currentY

            batch.draw(barnoneTexture, boxX + headingLeftPadding, currentY, barnoneWidth, barnoneHeight)

            val minMusicColoredBarWidth = barnoneWidth * 0.1f
            var musicColoredBarWidth = musicCircleX - (boxX + headingLeftPadding) + circleSize.x / 3
            musicColoredBarWidth = Math.max(musicColoredBarWidth, minMusicColoredBarWidth)

            val musicScaleX = musicColoredBarWidth / barfullTexture.width

            batch.draw(
                barfullTexture,
                boxX + headingLeftPadding,
                currentY,
                musicColoredBarWidth,
                barnoneHeight,
                0,
                0,
                barfullTexture.width,
                barfullTexture.height,
                false,
                false
            )

            musicCircleX = boxX + headingLeftPadding + musicCircleXOffset
            musicCircleX = MathUtils.clamp(musicCircleX, boxX + headingLeftPadding, boxX + headingLeftPadding + barnoneWidth - circleSize.x)
            batch.draw(circleTexture, musicCircleX, musicBarnoneY + barnoneHeight / 2f - circleSize.y / 2f, circleSize.x, circleSize.y)

            currentY -= barnoneHeight + 50f

            batch.draw(applyTexture, applyButtonX, applyButtonY, applyButtonWidth, buttonSize.y)
        }

        batch.end()

        handleInput(resumeButtonX, resumeButtonY, soundSettingsButtonX, soundSettingsButtonY, quitGameButtonX, quitGameButtonY, applyButtonX, applyButtonY, applyButtonWidth, redXButtonX, redXButtonY, onMenuClose)
    }

    private fun handleInput(resumeButtonX: Float, resumeButtonY: Float, soundSettingsButtonX: Float, soundSettingsButtonY: Float, quitGameButtonX: Float, quitGameButtonY: Float, applyButtonX: Float, applyButtonY: Float, applyButtonWidth: Float, redXButtonX: Float, redXButtonY: Float, onMenuClose: () -> Unit) {
        val mouseX = Gdx.input.x.toFloat()
        val mouseY = Gdx.graphics.height - Gdx.input.y.toFloat()

        fun isButtonClicked(buttonX: Float, buttonY: Float, buttonWidth: Float, buttonHeight: Float): Boolean {
            return mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            if (!showSoundSettings && isButtonClicked(soundSettingsButtonX, soundSettingsButtonY, buttonSize.x, buttonSize.y)) {
                Gdx.app.log("DEBUG", "SoundSettings Button clicked!")
                showSoundSettings = true
            }

            if (showSoundSettings && isButtonClicked(applyButtonX, applyButtonY, applyButtonWidth, buttonSize.y)) {
                Gdx.app.log("DEBUG", "Apply Button clicked!")
                showSoundSettings = false
            }

            if (showSoundSettings && isButtonClicked(redXButtonX, redXButtonY, redXSize.x, redXSize.y)) {
                Gdx.app.log("DEBUG", "Red X Button clicked!")
                showSoundSettings = false
            }

            if (!showSoundSettings && isButtonClicked(resumeButtonX, resumeButtonY, buttonSize.x, buttonSize.y)) {
                Gdx.app.log("DEBUG", "Resume Button clicked!")
                isMenuVisible = false
                onMenuClose()
            }
        }

        if (showSoundSettings) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                val circleTouchArea = 20f
                val masterCircleY = masterBarnoneY + barnoneHeight / 2f - circleSize.y / 2f
                if (mouseX >= masterCircleX - circleTouchArea && mouseX <= masterCircleX + circleSize.x + circleTouchArea
                    && mouseY >= masterCircleY - circleTouchArea && mouseY <= masterCircleY + circleSize.y + circleTouchArea) {
                    isDraggingMaster = true
                }
                val soundeffectsCircleY = soundeffectsBarnoneY + barnoneHeight / 2f - circleSize.y / 2f
                if (mouseX >= soundeffectsCircleX - circleTouchArea && mouseX <= soundeffectsCircleX + circleSize.x + circleTouchArea
                    && mouseY >= soundeffectsCircleY - circleTouchArea && mouseY <= soundeffectsCircleY + circleSize.y + circleTouchArea) {
                    isDraggingSoundeffects = true
                }
                val musicCircleY = musicBarnoneY + barnoneHeight / 2f - circleSize.y / 2f
                if (mouseX >= musicCircleX - circleTouchArea && mouseX <= musicCircleX + circleSize.x + circleTouchArea
                    && mouseY >= musicCircleY - circleTouchArea && mouseY <= musicCircleY + circleSize.y + circleTouchArea) {
                    isDraggingMusic = true
                }
            }

            if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                isDraggingMaster = false
                isDraggingSoundeffects = false
                isDraggingMusic = false
            }

            if (isDraggingMaster) {
                masterCircleXOffset = mouseX - (boxX + headingLeftPadding) - circleSize.x / 2f
                masterCircleXOffset = MathUtils.clamp(masterCircleXOffset, 0f, barnoneWidth - circleSize.x)
            }
            if (isDraggingSoundeffects) {
                soundeffectsCircleXOffset = mouseX - (boxX + headingLeftPadding) - circleSize.x / 2f
                soundeffectsCircleXOffset = MathUtils.clamp(soundeffectsCircleXOffset, 0f, barnoneWidth - circleSize.x)
            }
            if (isDraggingMusic) {
                musicCircleXOffset = mouseX - (boxX + headingLeftPadding) - circleSize.x / 2f
                musicCircleXOffset = MathUtils.clamp(musicCircleXOffset, 0f, barnoneWidth - circleSize.x)
            }
        }
    }

    fun dispose() {
        batch.dispose()
        boxTexture.dispose()
        font.dispose()
        resumeTexture.dispose()
        soundSettingsTexture.dispose()
        quitGameTexture.dispose()
        wordmarkTexture.dispose()
        applyTexture.dispose()
        redXTexture.dispose()
        barnoneTexture.dispose()
        barfullTexture.dispose()
        circleTexture.dispose()
    }
}
