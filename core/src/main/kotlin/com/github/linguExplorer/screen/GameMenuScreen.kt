package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.linguExplorer.linguExplorer
import ktx.app.KtxScreen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Rectangle

class GameMenuScreen(val game: linguExplorer) : KtxScreen {

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
    private val applyButtonWidth = 230f  // Breite für den Apply-Button
    private var showSoundSettings = false
    private lateinit var redXTexture: Texture
    private val redXSize = Vector2(60f, 60f) //rotes X-Symbols
    private lateinit var barnoneTexture: Texture
    private val barnoneWidth = 480f  // Breite des Balkens
    private val barnoneHeight = 25f // Höhe des Balkens
    private val headingLeftPadding = 50f // Abstand der Überschriften vom linken Rand
    private lateinit var circleTexture: Texture
    private val circleSize = Vector2(50f, 50f) // Größe des Kreises
    private var masterCircleX: Float = 0f
    private var soundeffectsCircleX: Float = 0f
    private var musicCircleX: Float = 0f
    private var isDraggingMaster: Boolean = false
    private var isDraggingSoundeffects: Boolean = false
    private var isDraggingMusic: Boolean = false

    override fun show() {
        batch = SpriteBatch()
        boxTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/box.png"))
        font = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
        resumeTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/resume.png"))
        soundSettingsTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/soundsettings.png"))
        quitGameTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/quitgame.png"))
        wordmarkTexture = Texture(Gdx.files.internal("xx_Images/wordmark/wordmark_scaled.png"))
        applyTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/apply.png"))
        redXTexture = Texture(Gdx.files.internal("xx_Images/Buttons/red_X.png")) // Lade die Textur für das rote X
        barnoneTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/barnone.png")) // Lade die Textur für den Balken
        circleTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/circle.png")) // Lade die Textur für den Kreis
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

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
        // Überschrift zeichnen
        font.data.setScale(0.4f)
        val headlineText = if (showSoundSettings) "SOUND" else "OPTIONS"
        val headlineLayout = GlyphLayout(font, headlineText)
        val headlineTextX = boxX + (boxWidth - headlineLayout.width) / 2
        val headlineTextY = boxY + boxHeight - 80
        font.draw(batch, headlineText, headlineTextX, headlineTextY)

        // Button-Positionierung (einmalig berechnen)
        val resumeButtonX = boxX + (boxWidth - buttonSize.x) / 2
        val resumeButtonY = headlineTextY - 120f - buttonSize.y

        val soundSettingsButtonX = boxX + (boxWidth - buttonSize.x) / 2
        val soundSettingsButtonY = resumeButtonY - buttonSize.y - buttonSpacing

        val quitGameButtonX = boxX + (boxWidth - buttonSize.x) / 2
        val quitGameButtonY = soundSettingsButtonY - buttonSize.y - buttonSpacing

        // apply button
        val applyButtonX = boxX + (boxWidth - applyButtonWidth) / 2  // Zentriere den schmaleren Button
        val applyButtonY = boxY - buttonSize.y / 2

        // Position des roten X-Symbols
        val redXButtonX = boxX + boxWidth - redXSize.x - 10 // 10 Pixel Abstand vom rechten Rand
        val redXButtonY = boxY + boxHeight - redXSize.y - 10 // 10 Pixel Abstand vom oberen Rand

        if (!showSoundSettings) {
            // Buttons zeichnen
            batch.draw(resumeTexture, resumeButtonX, resumeButtonY, buttonSize.x, buttonSize.y)
            batch.draw(soundSettingsTexture, soundSettingsButtonX, soundSettingsButtonY, buttonSize.x, buttonSize.y)
            batch.draw(quitGameTexture, quitGameButtonX, quitGameButtonY, buttonSize.x, buttonSize.y)
        } else {
            // Zeichne das rote X-Symbol, wenn Soundeinstellungen angezeigt werden
            batch.draw(redXTexture, redXButtonX, redXButtonY, redXSize.x, redXSize.y)

            //Startposition für die Sound-Einstellungen-Inhalte
            var currentY = headlineTextY - 100f // Abstand zur Überschrift

            // Schriftgröße für die Sound-Einstellungen-Überschriften
            font.data.setScale(0.2f)

            val headingSpacing = 50f // Abstand zwischen Überschrift und Balken
            val sectionSpacing = 45f // Abstand zwischen den Abschnitten (Überschrift + Balken)

            //Master Volume
            val masterText = "MASTER"
            val masterLayout = GlyphLayout(font, masterText)
            val masterTextX = boxX + headingLeftPadding // Linksbündig mit Abstand
            font.draw(batch, masterText, masterTextX, currentY)
            currentY -= masterLayout.height + headingSpacing // Abstand zum Balken
            val masterBarnoneY = currentY
            batch.draw(barnoneTexture, boxX + headingLeftPadding, currentY, barnoneWidth, barnoneHeight) //Balken zeichnen
            // Zeichne den Kreis am linken Ende des Balkens
            batch.draw(circleTexture, boxX + headingLeftPadding, masterBarnoneY + barnoneHeight / 2f - circleSize.y / 2f, circleSize.x, circleSize.y)

            currentY -= barnoneHeight + sectionSpacing // Abstand zur nächsten Überschrift

            // Soundeffects Volume
            val soundeffectsText = "SOUNDEFFECTS"
            val soundeffectsLayout = GlyphLayout(font, soundeffectsText)
            val soundeffectsTextX = boxX + headingLeftPadding // Linksbündig mit Abstand
            font.draw(batch, soundeffectsText, soundeffectsTextX, currentY)
            currentY -= soundeffectsLayout.height + headingSpacing // Abstand zum Balken
            val soundeffectsBarnoneY = currentY // Speichere die Y-Position des Soundeffects-Balkens
            batch.draw(barnoneTexture, boxX + headingLeftPadding, currentY, barnoneWidth, barnoneHeight) //Balken zeichnen
            // Zeichne den Kreis am linken Ende des Balkens
            batch.draw(circleTexture, boxX + headingLeftPadding, soundeffectsBarnoneY + barnoneHeight / 2f - circleSize.y / 2f, circleSize.x, circleSize.y)

            currentY -= barnoneHeight + sectionSpacing // Abstand zur nächsten Überschrift

            //Music Volume
            val musicText = "MUSIC"
            val musicLayout = GlyphLayout(font, musicText)
            val musicTextX = boxX + headingLeftPadding // Linksbündig mit Abstand
            font.draw(batch, musicText, musicTextX, currentY)
            currentY -= musicLayout.height + headingSpacing // Abstand zum Balken
            val musicBarnoneY = currentY // Speichere die Y-Position des Music-Balkens
            batch.draw(barnoneTexture, boxX + headingLeftPadding, currentY, barnoneWidth, barnoneHeight) //Balken zeichnen
            // Zeichne den Kreis am linken Ende des Balkens
            batch.draw(circleTexture, boxX + headingLeftPadding, musicBarnoneY + barnoneHeight / 2f - circleSize.y / 2f, circleSize.x, circleSize.y)

            currentY -= barnoneHeight + 50f // Abstand zum Apply Button

            // apply button
            batch.draw(applyTexture, applyButtonX, applyButtonY, applyButtonWidth, buttonSize.y)
        }

        batch.end()

        // Eingabe verarbeiten (Positionen werden hier übergeben)
        handleInput(resumeButtonX, resumeButtonY, soundSettingsButtonX, soundSettingsButtonY, quitGameButtonX, quitGameButtonY, applyButtonX, applyButtonY, applyButtonWidth, redXButtonX, redXButtonY)
    }

    private fun handleInput(resumeButtonX: Float, resumeButtonY: Float, soundSettingsButtonX: Float, soundSettingsButtonY: Float, quitGameButtonX: Float, quitGameButtonY: Float, applyButtonX: Float, applyButtonY: Float, applyButtonWidth: Float, redXButtonX: Float, redXButtonY: Float) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            val mouseX = Gdx.input.x.toFloat()
            val mouseY = Gdx.graphics.height - Gdx.input.y.toFloat()

            Gdx.app.log("DEBUG", "MouseX: $mouseX, MouseY: $mouseY")

            // Überprüfen, ob der Soundsettings-Button angeklickt wurde
            if (!showSoundSettings && mouseX >= soundSettingsButtonX && mouseX <= soundSettingsButtonX + buttonSize.x &&
                mouseY >= soundSettingsButtonY && mouseY <= soundSettingsButtonY + buttonSize.y) {
                Gdx.app.log("DEBUG", "SoundSettings Button clicked!")
                showSoundSettings = true
            }

            // Überprüfen, ob der Apply-Button angeklickt wurde
            if (showSoundSettings && mouseX >= applyButtonX && mouseX <= applyButtonX + applyButtonWidth && // Verwende applyButtonWidth
                mouseY >= applyButtonY && mouseY <= applyButtonY + buttonSize.y) {
                Gdx.app.log("DEBUG", "Apply Button clicked!")
                showSoundSettings = false
            }

            // Überprüfen, ob das rote X-Symbol angeklickt wurde
            if (showSoundSettings && mouseX >= redXButtonX && mouseX <= redXButtonX + redXSize.x &&
                mouseY >= redXButtonY && mouseY <= redXButtonY + redXSize.y) {
                Gdx.app.log("DEBUG", "Red X Button clicked!")
                showSoundSettings = false // Zurück zum Hauptmenü
            }
        }
    }

    override fun dispose() {
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
        circleTexture.dispose()
    }
}
