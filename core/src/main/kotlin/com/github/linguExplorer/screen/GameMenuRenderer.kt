package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.masterVolume
import com.github.linguExplorer.musicVolume
import com.github.linguExplorer.soundEffectVolume

class GameMenuRenderer {
    private var menuSet = true
    private var showSoundSettings = false
    private var fontHeadliner: BitmapFont = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))

    private val boxTexture: Texture
    private val resumeTexture: Texture
    private val soundSettingsTexture: Texture
    private val quitGameTexture: Texture
    private val wordmarkTexture: Texture
    private val applyTexture: Texture
    private val redXTexture: Texture
    private val barnoneTexture: Texture
    private val barfullTexture: Texture
    private val circleTexture: Texture

    private val buttonSize = Vector2(300f, 90f)
    private val buttonSpacing = 40f
    private val wordmarkScale = 0.2f
    private val applyButtonWidth = 230f
    private val redXSize = Vector2(85f, 85f)
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
    private var isQuit= false

    private var boxX = 0f
    private var boxY = 0f

    private var onResumeClicked: () -> Unit = {}
    private var onQuitClicked: () -> Unit = {}

    private var masterPreSave = 0f
    private var soundeffectPreSave = 0f
    private var musicPreSave = 0f

    private var game: linguExplorer = linguExplorer()
    private var isMainMenu = true

    init {
        fontHeadliner.color = Color.BLACK
        fontHeadliner.data.setScale(0.5f, 0.5f)

        boxTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/box.png"))
        resumeTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/resume.png"))
        soundSettingsTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/soundsettings.png"))
        quitGameTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/quitgame.png"))
        wordmarkTexture = Texture(Gdx.files.internal("xx_Images/wordmark/wordmark_scaled.png"))
        applyTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/apply.png"))
        redXTexture = Texture(Gdx.files.internal("xx_Images/Buttons/red_X.png"))
        barnoneTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/barnone.png"))
        barfullTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/barfull.png"))
        circleTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/circle.png"))

        updateCirclePositionsFromVolumes()
    }

    fun renderGameMenu(batch: SpriteBatch, font: BitmapFont, glyphLayout: GlyphLayout, viewport: Viewport, shapeRenderer: ShapeRenderer, mainMenu: Boolean, linguGame: linguExplorer): Boolean {
        if(!mainMenu) {
            isMainMenu = false
        } else {
            isMainMenu = true
            game = linguGame
        }

        if (isQuit) {
            return true
        }

        font.color = Color.BLACK
        font.data.setScale(0.3f, 0.3f)
        val screenWidth = viewport.worldWidth
        val screenHeight = viewport.worldHeight

        boxX = screenWidth / 2f - 300f
        boxY = (screenHeight / 2f) - 430f

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined

        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(0f, 0f, 0f, 0.5f)
        shapeRenderer.rect(0f, 0f, screenWidth, screenHeight)
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        batch.begin()
        batch.draw(boxTexture, boxX, boxY, 600f, 650f)

        val wordmarkWidth = wordmarkTexture.width * wordmarkScale
        val wordmarkHeight = wordmarkTexture.height * wordmarkScale
        batch.draw(
            wordmarkTexture, (screenWidth-wordmarkWidth) / 2f,
            (screenHeight / 2f) + 220f,
            wordmarkWidth,
            wordmarkHeight
        )

        val titleText = if (showSoundSettings) "SOUND" else "OPTIONS"
        val titleLayout = GlyphLayout(fontHeadliner, titleText)
        val titleX = boxX + (600f - titleLayout.width) / 2f
        val titleY = boxY + 580f
        fontHeadliner.draw(batch, titleText, titleX, titleY)

        if (showSoundSettings) {
            drawSoundSettings(batch, font, glyphLayout)

            val applyButtonX = boxX + 180f
            val applyButtonY = boxY - (buttonSize.y / 2f) + 10f
            batch.draw(applyTexture, applyButtonX, applyButtonY, applyButtonWidth, buttonSize.y)

            val redXX = boxX + 600f - redXSize.x - 20f
            val redXY = boxY + 650f - redXSize.y - 20f
            batch.draw(redXTexture, redXX, redXY, redXSize.x, redXSize.y)
        } else {
            drawMainMenuButtons(batch)
        }

        batch.end()

        handleInput(viewport)

        if (isDraggingMaster || isDraggingSoundeffects || isDraggingMusic) {
            updateVolumesFromCirclePositions()
        }

        return false
    }

    private fun updateCirclePositionsFromVolumes() {
        masterCircleXOffset = MathUtils.clamp(masterVolume, 0f, 1f) * (barnoneWidth - circleSize.x)
        soundeffectsCircleXOffset = MathUtils.clamp(soundEffectVolume, 0f, 1f) * (barnoneWidth - circleSize.x)
        musicCircleXOffset = MathUtils.clamp(musicVolume, 0f, 1f) * (barnoneWidth - circleSize.x)
    }

    private fun updateVolumesFromCirclePositions() {
        masterVolume = MathUtils.clamp(
            masterCircleXOffset / (barnoneWidth - circleSize.x),
            0f,
            1f
        )
        println(masterVolume)
        soundEffectVolume = MathUtils.clamp(
            soundeffectsCircleXOffset / (barnoneWidth - circleSize.x),
            0f,
            1f
        )
        musicVolume = MathUtils.clamp(
            musicCircleXOffset / (barnoneWidth - circleSize.x),
            0f,
            1f
        )
    }

    private fun drawMainMenuButtons(batch: SpriteBatch) {
        var currentY = boxY + 360f

        batch.draw(resumeTexture, boxX + (buttonSize.x / 2f), currentY, buttonSize.x, buttonSize.y)
        currentY -= buttonSize.y + buttonSpacing

        batch.draw(soundSettingsTexture, boxX + (buttonSize.x / 2f), currentY, buttonSize.x, buttonSize.y)
        currentY -= buttonSize.y + buttonSpacing

        batch.draw(quitGameTexture, boxX + (buttonSize.x / 2f), currentY, buttonSize.x, buttonSize.y)
    }

    private fun drawSoundSettings(batch: SpriteBatch, font: BitmapFont, glyphLayout: GlyphLayout) {
        var currentY = boxY + 400f
        font.data.setScale(0.23f, 0.23f)

        masterBarnoneY = currentY
        drawSlider(batch, font, glyphLayout, "MASTER", masterCircleXOffset, masterBarnoneY, isDraggingMaster)
        currentY -= 125f

        soundeffectsBarnoneY = currentY
        drawSlider(batch, font, glyphLayout, "SOUNDEFFECTS", soundeffectsCircleXOffset, soundeffectsBarnoneY, isDraggingSoundeffects)
        currentY -= 125f

        musicBarnoneY = currentY
        drawSlider(batch, font, glyphLayout, "MUSIC", musicCircleXOffset, musicBarnoneY, isDraggingMusic)
    }

    private fun drawSlider(batch: SpriteBatch, font: BitmapFont, glyphLayout: GlyphLayout,
                           name: String, offsetX: Float, barY: Float, isDragging: Boolean) {
        glyphLayout.setText(font, name)
        font.draw(batch, name, boxX + headingLeftPadding + 5f, barY + barnoneHeight + glyphLayout.height + 20f)

        batch.draw(barnoneTexture, boxX + headingLeftPadding, barY, barnoneWidth, barnoneHeight)

        batch.draw(
            barfullTexture,
            boxX + headingLeftPadding,
            barY,
            offsetX + circleSize.x / 3,
            barnoneHeight
        )

        batch.draw(
            circleTexture,
            boxX + headingLeftPadding + offsetX,
            barY + barnoneHeight / 2f - circleSize.y / 2f,
            circleSize.x,
            circleSize.y
        )
    }

    private fun handleInput(viewport: Viewport) {
        val mousePos = viewport.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()))
        val mouseX = mousePos.x
        val mouseY = mousePos.y

        if (showSoundSettings) {
            handleSoundSettingsInput(mouseX, mouseY)
        } else {
            handleMainMenuInput(mouseX, mouseY)
        }
    }

    private fun handleMainMenuInput(mouseX: Float, mouseY: Float) {
        val buttonX = boxX + (buttonSize.x / 2f)
        var currentY = boxY + 360f

        if (isButtonTouched(mouseX, mouseY, buttonX, currentY, buttonSize.x, buttonSize.y)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                onResumeClicked()
            }
        }
        currentY -= buttonSize.y + buttonSpacing

        if (isButtonTouched(mouseX, mouseY, buttonX, currentY, buttonSize.x, buttonSize.y)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                masterPreSave = masterVolume
                soundeffectPreSave = soundEffectVolume
                musicPreSave = musicVolume

                updateCirclePositionsFromVolumes()

                showSoundSettings = true
            }
        }
        currentY -= buttonSize.y + buttonSpacing

        if (isButtonTouched(mouseX, mouseY, buttonX, currentY, buttonSize.x, buttonSize.y)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if(isMainMenu) {
                    Gdx.app.exit()

                } else {

                    println("Switch to main")
                    isQuit = true
                }

                onQuitClicked()
            }
        }
    }

    private fun handleSoundSettingsInput(mouseX: Float, mouseY: Float) {
        val circleTouchArea = 50f
        val sliderStartX = boxX + headingLeftPadding
        val sliderEndX = sliderStartX + barnoneWidth - circleSize.x

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            isDraggingMaster = isCircleTouched(mouseX, mouseY,
                sliderStartX + masterCircleXOffset,
                masterBarnoneY + barnoneHeight / 2f - circleSize.y / 2f,
                circleTouchArea)

            isDraggingSoundeffects = isCircleTouched(mouseX, mouseY,
                sliderStartX + soundeffectsCircleXOffset,
                soundeffectsBarnoneY + barnoneHeight / 2f - circleSize.y / 2f,
                circleTouchArea)

            isDraggingMusic = isCircleTouched(mouseX, mouseY,
                sliderStartX + musicCircleXOffset,
                musicBarnoneY + barnoneHeight / 2f - circleSize.y / 2f,
                circleTouchArea)
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            val newXOffset = MathUtils.clamp(mouseX - sliderStartX, 0f, barnoneWidth - circleSize.x)

            when {
                isDraggingMaster -> masterCircleXOffset = newXOffset
                isDraggingSoundeffects -> soundeffectsCircleXOffset = newXOffset
                isDraggingMusic -> musicCircleXOffset = newXOffset
            }
        }

        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            isDraggingMaster = false
            isDraggingSoundeffects = false
            isDraggingMusic = false
        }

        updateVolumesFromCirclePositions()

        val applyButtonX = boxX + 180f
        val applyButtonY = boxY - (buttonSize.y / 2f) + 10f
        if (isButtonTouched(mouseX, mouseY, applyButtonX, applyButtonY, applyButtonWidth, buttonSize.y)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                showSoundSettings = false
            }
        }

        val redXX = boxX + 600f - redXSize.x - 20f
        val redXY = boxY + 650f - redXSize.y - 20f
        if (isButtonTouched(mouseX, mouseY, redXX, redXY, redXSize.x, redXSize.y)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                masterVolume = masterPreSave
                soundEffectVolume = soundeffectPreSave
                musicVolume = musicPreSave

                updateCirclePositionsFromVolumes()
                showSoundSettings = false
            }
        }
    }

    private fun isButtonTouched(mouseX: Float, mouseY: Float, buttonX: Float, buttonY: Float, width: Float, height: Float): Boolean {
        return mouseX >= buttonX && mouseX <= buttonX + width && mouseY >= buttonY && mouseY <= buttonY + height
    }

    private fun isCircleTouched(mouseX: Float, mouseY: Float, circleX: Float, circleY: Float, touchArea: Float): Boolean {
        return mouseX in (circleX - touchArea)..(circleX + circleSize.x + touchArea) &&
            mouseY in (circleY - touchArea)..(circleY + circleSize.y + touchArea)
    }

    fun setOnResumeClicked(callback: () -> Unit) {
        onResumeClicked = {
            menuSet = false
            callback()
        }
    }

    fun setOnQuitClicked(callback: () -> Unit) {
        onQuitClicked = callback
    }

    fun resetMenu() {
        menuSet = true
        showSoundSettings = false
    }

    fun dispose() {
        fontHeadliner.dispose()
        boxTexture.dispose()
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
