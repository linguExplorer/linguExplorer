package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport

class GameMenuRenderer {

    private var menuSet = true
    private var showSoundSettings = false

    private val boxTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/box.png"))
    private val resumeTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/resume.png"))
    private val soundSettingsTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/soundsettings.png"))
    private val quitGameTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/quitgame.png"))
    private val wordmarkTexture = Texture(Gdx.files.internal("xx_Images/wordmark/wordmark_scaled.png"))
    private val applyTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/apply.png"))
    private val redXTexture = Texture(Gdx.files.internal("xx_Images/Buttons/red_X.png"))
    private val barnoneTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/barnone.png"))
    private val barfullTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/barfull.png"))
    private val circleTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/circle.png"))

    private val buttonSize = Vector2(300f, 90f)
    private val buttonSpacing = 40f
    private val wordmarkScale = 0.2f
    private val applyButtonWidth = 230f
    private val redXSize = Vector2(60f, 60f)
    private val barnoneWidth = 480f
    private val barnoneHeight = 25f
    private val headingLeftPadding = 50f
    private val circleSize = Vector2(50f, 50f)

    private var masterCircleXOffset = 0f
    private var soundeffectsCircleXOffset = 0f
    private var musicCircleXOffset = 0f

    private var masterBarnoneY = 0f
    private var soundeffectsBarnoneY = 0f
    private var musicBarnoneY = 0f

    private var isDraggingMaster = false
    private var isDraggingSoundeffects = false
    private var isDraggingMusic = false

    private var boxX = 0f
    private var boxY = 0f

    fun renderGameMenu(batch: SpriteBatch, font: BitmapFont, glyphLayout: GlyphLayout, viewport: Viewport) {
        val screenWidth = viewport.worldWidth
        val screenHeight = viewport.worldHeight

        boxX = (screenWidth - screenWidth * 0.30f) / 2f
        boxY = (screenHeight - screenHeight * 0.6f) / 2f - screenHeight * 0.10f

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined

        batch.begin()
        batch.draw(boxTexture, boxX, boxY, screenWidth * 0.30f, screenHeight * 0.6f)

        if (showSoundSettings) {
            drawSliders(batch, font, glyphLayout)
        }

        batch.end()

        handleInput(viewport)
    }

    private fun drawSliders(batch: SpriteBatch, font: BitmapFont, glyphLayout: GlyphLayout) {
        var currentY = boxY + 250f

        fun drawSlider(name: String, offsetX: Float, barY: Float, isDragging: Boolean): Float {
            glyphLayout.setText(font, name)
            batch.draw(barnoneTexture, boxX + headingLeftPadding, barY, barnoneWidth, barnoneHeight)

            val newOffset = if (isDragging) {
                val mousePos = Gdx.input.x.toFloat()
                MathUtils.clamp(mousePos - (boxX + headingLeftPadding), 0f, barnoneWidth - circleSize.x)
            } else offsetX

            batch.draw(
                barfullTexture,
                boxX + headingLeftPadding,
                barY,
                newOffset + circleSize.x / 3,
                barnoneHeight
            )

            batch.draw(
                circleTexture,
                boxX + headingLeftPadding + newOffset,
                barY + barnoneHeight / 2f - circleSize.y / 2f,
                circleSize.x,
                circleSize.y
            )

            return newOffset
        }

        masterBarnoneY = currentY
        masterCircleXOffset = drawSlider("MASTER", masterCircleXOffset, masterBarnoneY, isDraggingMaster)
        currentY -= 80f

        soundeffectsBarnoneY = currentY
        soundeffectsCircleXOffset = drawSlider("SOUNDEFFECTS", soundeffectsCircleXOffset, soundeffectsBarnoneY, isDraggingSoundeffects)
        currentY -= 80f

        musicBarnoneY = currentY
        musicCircleXOffset = drawSlider("MUSIC", musicCircleXOffset, musicBarnoneY, isDraggingMusic)
    }

    private fun handleInput(viewport: Viewport) {
        if (!menuSet) return

        val mousePos = viewport.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()))
        val mouseX = mousePos.x
        val mouseY = mousePos.y

        val circleTouchArea = 20f

        fun isCircleTouched(circleX: Float, circleY: Float) =
            mouseX in (circleX - circleTouchArea)..(circleX + circleSize.x + circleTouchArea) &&
                mouseY in (circleY - circleTouchArea)..(circleY + circleSize.y + circleTouchArea)

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            isDraggingMaster = isCircleTouched(boxX + headingLeftPadding + masterCircleXOffset, masterBarnoneY)
            isDraggingSoundeffects = isCircleTouched(boxX + headingLeftPadding + soundeffectsCircleXOffset, soundeffectsBarnoneY)
            isDraggingMusic = isCircleTouched(boxX + headingLeftPadding + musicCircleXOffset, musicBarnoneY)
        }

        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            isDraggingMaster = false
            isDraggingSoundeffects = false
            isDraggingMusic = false
        }
    }

    fun getMasterVolume(): Float = masterCircleXOffset / (barnoneWidth - circleSize.x)
    fun getSoundEffectsVolume(): Float = soundeffectsCircleXOffset / (barnoneWidth - circleSize.x)
    fun getMusicVolume(): Float = musicCircleXOffset / (barnoneWidth - circleSize.x)

    fun setMasterVolume(volume: Float) {
        masterCircleXOffset = volume * (barnoneWidth - circleSize.x)
    }
}
