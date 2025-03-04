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
import com.github.linguExplorer.masterVolume
import com.github.linguExplorer.musicVolume
import com.github.linguExplorer.soundEffectVolume

class GameMenuRenderer {
    // Menu state
    private var menuSet = true
    private var showSoundSettings = false
    private lateinit var fontHeadliner: BitmapFont

    // Textures
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

    // UI element sizes and positions
    private val buttonSize = Vector2(300f, 90f)
    private val buttonSpacing = 40f
    private val wordmarkScale = 0.2f
    private val applyButtonWidth = 230f
    private val redXSize = Vector2(85f, 85f)
    private val barnoneWidth = 480f
    private val barnoneHeight = 25f
    private val headingLeftPadding = 50f
    private val circleSize = Vector2(50f, 50f)

    // Slider values and positions
    private var masterCircleXOffset = 0f
    private var soundeffectsCircleXOffset = 0f
    private var musicCircleXOffset = 0f

    private var masterBarnoneY = 0f
    private var soundeffectsBarnoneY = 0f
    private var musicBarnoneY = 0f

    // Drag state
    private var isDraggingMaster = false
    private var isDraggingSoundeffects = false
    private var isDraggingMusic = false

    // Menu position
    private var boxX = 0f
    private var boxY = 0f

    private var onResumeClicked: () -> Unit = {}
    private var onQuitClicked: () -> Unit = {}

    private var masterPreSave = 0f
    private var soundeffectPreSave = 0f
    private var musicPreSave = 0f

    // Initialize the class by setting circle positions based on volume parameters
    init {
        // Load font only once
        fontHeadliner = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
        fontHeadliner.color = Color.BLACK
        fontHeadliner.data.setScale(0.5f, 0.5f)

        // Load textures in init block
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

    fun renderGameMenu(batch: SpriteBatch, font: BitmapFont, glyphLayout: GlyphLayout, viewport: Viewport, shapeRenderer: ShapeRenderer) {
        // Reset menuSet to true when rendering the menu
        menuSet = true

        font.color = Color.BLACK
        font.data.setScale(0.3f, 0.3f)
        val screenWidth = viewport.worldWidth
        val screenHeight = viewport.worldHeight

        boxX = screenWidth / 2f - 300f
        boxY = (screenHeight / 2f) - 430f

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined

        // Draw transparent background
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(0f, 0f, 0f, 0.5f)
        shapeRenderer.rect(0f, 0f, screenWidth, screenHeight)
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        batch.begin()

        // Draw box background
        batch.draw(boxTexture, boxX, boxY, 600f, 650f)

        // Draw wordmark
        val wordmarkWidth = wordmarkTexture.width * wordmarkScale
        val wordmarkHeight = wordmarkTexture.height * wordmarkScale
        batch.draw(
            wordmarkTexture, (screenWidth-wordmarkWidth) / 2f,
            (screenHeight / 2f) + 220f,
            wordmarkWidth,
            wordmarkHeight
        )

        // Draw title
        val titleText = if (showSoundSettings) "SOUND" else "OPTIONS"
        val titleLayout = GlyphLayout(fontHeadliner, titleText)
        val titleX = boxX + (600f - titleLayout.width) / 2f
        val titleY = boxY + 580f
        fontHeadliner.draw(batch, titleText, titleX, titleY)

        if (showSoundSettings) {
            drawSoundSettings(batch, font, glyphLayout)

            // Draw apply button
            val applyButtonX = boxX + 180f
            val applyButtonY = boxY - (buttonSize.y / 2f) + 10f
            batch.draw(applyTexture, applyButtonX, applyButtonY, applyButtonWidth, buttonSize.y)

            // Draw close (X) button
            val redXX = boxX + 600f - redXSize.x - 20f
            val redXY = boxY + 650f - redXSize.y - 20f
            batch.draw(redXTexture, redXX, redXY, redXSize.x, redXSize.y)
        } else {
            drawMainMenuButtons(batch)
        }

        batch.end()

        // Handle input and update volumes
        handleInput(viewport)

        if (isDraggingMaster || isDraggingSoundeffects || isDraggingMusic) {
            updateVolumesFromCirclePositions()
        }
    }

    /**
     * Updates circle positions based on current volume parameters
     */
    private fun updateCirclePositionsFromVolumes() {
        masterCircleXOffset = masterVolume * (barnoneWidth - circleSize.x)
        soundeffectsCircleXOffset = soundEffectVolume * (barnoneWidth - circleSize.x)
        musicCircleXOffset = musicVolume * (barnoneWidth - circleSize.x)
    }

    /**
     * Updates volume parameters based on current circle positions
     */
    private fun updateVolumesFromCirclePositions() {
        masterVolume = masterCircleXOffset / (barnoneWidth - circleSize.x)
        soundEffectVolume = soundeffectsCircleXOffset / (barnoneWidth - circleSize.x)
        musicVolume = musicCircleXOffset / (barnoneWidth - circleSize.x)
    }

    /**
     * Draws the main menu buttons (Resume, Sound Settings, Quit Game)
     */
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
        masterCircleXOffset = drawSlider(batch, font, glyphLayout, "MASTER", masterCircleXOffset, masterBarnoneY, isDraggingMaster)
        currentY -= 125f

        soundeffectsBarnoneY = currentY
        soundeffectsCircleXOffset = drawSlider(batch, font, glyphLayout, "SOUNDEFFECTS", soundeffectsCircleXOffset, soundeffectsBarnoneY, isDraggingSoundeffects)
        currentY -= 125f

        musicBarnoneY = currentY
        musicCircleXOffset = drawSlider(batch, font, glyphLayout, "MUSIC", musicCircleXOffset, musicBarnoneY, isDraggingMusic)
    }

    /**
     * Draws a single slider with label, bar, and draggable circle
     */
    private fun drawSlider(batch: SpriteBatch, font: BitmapFont, glyphLayout: GlyphLayout,
                           name: String, offsetX: Float, barY: Float, isDragging: Boolean): Float {
        // Draw label
        glyphLayout.setText(font, name)
        font.draw(batch, name, boxX + headingLeftPadding + 5f, barY + barnoneHeight + glyphLayout.height + 20f)

        // Draw slider background
        batch.draw(barnoneTexture, boxX + headingLeftPadding, barY, barnoneWidth, barnoneHeight)

        // Calculate position based on drag state
        val newOffset = if (isDragging) {
            val mousePos = Gdx.input.x.toFloat()
            MathUtils.clamp(mousePos - (boxX + headingLeftPadding), 0f, barnoneWidth - circleSize.x)
        } else offsetX

        // Draw filled portion of slider
        batch.draw(
            barfullTexture,
            boxX + headingLeftPadding,
            barY,
            newOffset + circleSize.x / 3,
            barnoneHeight
        )

        // Draw slider handle
        batch.draw(
            circleTexture,
            boxX + headingLeftPadding + newOffset,
            barY + barnoneHeight / 2f - circleSize.y / 2f,
            circleSize.x,
            circleSize.y
        )

        return newOffset
    }

    /**
     * Handles mouse input for menu interactions
     */
    private fun handleInput(viewport: Viewport) {
        // menuSet wird jetzt in renderGameMenu auf true gesetzt, daher ist diese Bedingung nicht mehr notwendig
        // if (!menuSet) return

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

        // Resume button
        if (isButtonTouched(mouseX, mouseY, buttonX, currentY, buttonSize.x, buttonSize.y)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                onResumeClicked()
            }
        }
        currentY -= buttonSize.y + buttonSpacing

        // Sound settings button
        if (isButtonTouched(mouseX, mouseY, buttonX, currentY, buttonSize.x, buttonSize.y)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                // Save current values before entering sound settings
                masterPreSave = masterVolume
                soundeffectPreSave = soundEffectVolume
                musicPreSave = musicVolume

                // Make sure circle positions match current volumes when opening settings
                updateCirclePositionsFromVolumes()

                showSoundSettings = true
            }
        }
        currentY -= buttonSize.y + buttonSpacing

        // Quit button
        if (isButtonTouched(mouseX, mouseY, buttonX, currentY, buttonSize.x, buttonSize.y)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                onQuitClicked()
            }
        }
    }

    /**
     * Handles input for sound settings screen
     */
    private fun handleSoundSettingsInput(mouseX: Float, mouseY: Float) {
        val circleTouchArea = 20f

        // Check if any slider circle is being dragged
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            isDraggingMaster = isCircleTouched(mouseX, mouseY,
                boxX + headingLeftPadding + masterCircleXOffset,
                masterBarnoneY + barnoneHeight / 2f - circleSize.y / 2f,
                circleTouchArea)

            isDraggingSoundeffects = isCircleTouched(mouseX, mouseY,
                boxX + headingLeftPadding + soundeffectsCircleXOffset,
                soundeffectsBarnoneY + barnoneHeight / 2f - circleSize.y / 2f,
                circleTouchArea)

            isDraggingMusic = isCircleTouched(mouseX, mouseY,
                boxX + headingLeftPadding + musicCircleXOffset,
                musicBarnoneY + barnoneHeight / 2f - circleSize.y / 2f,
                circleTouchArea)
        }

        // Release drag state if mouse button is no longer pressed
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            isDraggingMaster = false
            isDraggingSoundeffects = false
            isDraggingMusic = false
        }

        // Apply button
        val applyButtonX = boxX + 180f
        val applyButtonY = boxY - (buttonSize.y / 2f) + 10f
        if (isButtonTouched(mouseX, mouseY, applyButtonX, applyButtonY, applyButtonWidth, buttonSize.y)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                updateVolumesFromCirclePositions()
                showSoundSettings = false
            }
        }

        // Close (X) button
        val redXX = boxX + 600f - redXSize.x - 20f
        val redXY = boxY + 650f - redXSize.y - 20f
        if (isButtonTouched(mouseX, mouseY, redXX, redXY, redXSize.x, redXSize.y)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                // When canceling, restore previous values
                masterVolume = masterPreSave
                soundEffectVolume = soundeffectPreSave
                musicVolume = musicPreSave

                // Make sure circle positions match restored volumes
                updateCirclePositionsFromVolumes()

                showSoundSettings = false
            }
        }
    }

    /**
     * Helper method to check if a button is being touched
     */
    private fun isButtonTouched(mouseX: Float, mouseY: Float, buttonX: Float, buttonY: Float, width: Float, height: Float): Boolean {
        return mouseX >= buttonX && mouseX <= buttonX + width && mouseY >= buttonY && mouseY <= buttonY + height
    }

    /**
     * Helper method to check if a slider circle is being touched
     */
    private fun isCircleTouched(mouseX: Float, mouseY: Float, circleX: Float, circleY: Float, touchArea: Float): Boolean {
        return mouseX in (circleX - touchArea)..(circleX + circleSize.x + touchArea) &&
            mouseY in (circleY - touchArea)..(circleY + circleSize.y + touchArea)
    }

    fun setOnResumeClicked(callback: () -> Unit) {
        onResumeClicked = {
            menuSet = false  // Hier wird menuSet auf false gesetzt
            callback()
        }
    }

    fun setOnQuitClicked(callback: () -> Unit) {
        onQuitClicked = callback
    }

    // Methode zum Zurücksetzen des Menüs, falls du sie separat aufrufen möchtest
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
