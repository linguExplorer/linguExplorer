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

class GameMenuScreen(val game: linguExplorer) : KtxScreen {

    private lateinit var batch: SpriteBatch
    private lateinit var boxTexture: Texture
    private var boxX: Float = 0f
    private var boxY: Float = 0f
    private lateinit var font: BitmapFont // Schriftart Variable
    private lateinit var resumeTexture: Texture
    private lateinit var soundSettingsTexture: Texture
    private lateinit var quitGameTexture: Texture
    private val buttonSize = Vector2(300f, 90f) // Größere Buttons
    private val buttonSpacing = 40f // Größerer Abstand zwischen den Buttons
    private lateinit var wordmarkTexture: Texture  // Wordmark Textur
    private val wordmarkScale = 0.2f //  Wordmark Größe
    private lateinit var applyTexture: Texture

    private var showSoundSettings = false // Zustand, um zu verfolgen, ob Soundeinstellungen angezeigt werden


    override fun show() {
        batch = SpriteBatch()
        boxTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/box.png"))
        font = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt")) // Schriftart laden
        resumeTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/resume.png"))
        soundSettingsTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/soundsettings.png"))
        quitGameTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/quitgame.png"))
        wordmarkTexture = Texture(Gdx.files.internal("xx_Images/wordmark/wordmark_scaled.png"))  // Wordmark laden
        applyTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/apply.png"))

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()

        val boxWidth = screenWidth * 0.30f
        val boxHeight = screenHeight * 0.6f

        val boxX = (screenWidth - boxWidth) / 2f
        var boxY = (screenHeight - boxHeight) / 2f

        val yOffset = screenHeight * 0.10f

        boxY -= yOffset

        batch.begin()

        // Wordmark zeichnen
        val wordmarkWidth = wordmarkTexture.width.toFloat() * wordmarkScale // Skalierte Breite
        val wordmarkHeight = wordmarkTexture.height.toFloat() * wordmarkScale // Skalierte Höhe
        val wordmarkX = (screenWidth - wordmarkWidth) / 2f // Horizontal zentrieren
        val wordmarkY = boxY + boxHeight + (screenHeight - (boxY + boxHeight)) / 2 - wordmarkHeight / 2 // Zentriert vertikal zwischen Box und Bildschirmrand

        batch.draw(wordmarkTexture, wordmarkX, wordmarkY, wordmarkWidth, wordmarkHeight)

        batch.draw(boxTexture, boxX, boxY, boxWidth, boxHeight)

        // Überprüfen, ob die Soundeinstellungen angezeigt werden sollen
        if (!showSoundSettings) {
            // Zeichne den Text "OPTIONS"
            font.color = Color.BLACK // Schriftfarbe Schwarz
            font.data.setScale(0.4f) // Schriftgröße anpassen

            val text = "OPTIONS"
            val layout = GlyphLayout(font, text)
            val textX = boxX + (boxWidth - layout.width) / 2 // Zentriert horizontal in der Box
            val textY = boxY + boxHeight - 80 // Text etwas tiefer in der Box

            font.draw(batch, text, textX, textY)

            // Button-Positionierung
            val startButtonY = textY - 120f - buttonSize.y // Startposition unter dem Text (weiter runter)

            val resumeButtonX = boxX + (boxWidth - buttonSize.x) / 2 // Zentriert horizontal
            val resumeButtonY = startButtonY

            val soundSettingsButtonX = boxX + (boxWidth - buttonSize.x) / 2
            val soundSettingsButtonY = resumeButtonY - buttonSize.y - buttonSpacing

            val quitGameButtonX = boxX + (boxWidth - buttonSize.x) / 2
            val quitGameButtonY = soundSettingsButtonY - buttonSize.y - buttonSpacing

            // Buttons zeichnen
            batch.draw(resumeTexture, resumeButtonX, resumeButtonY, buttonSize.x, buttonSize.y)
            batch.draw(soundSettingsTexture, soundSettingsButtonX, soundSettingsButtonY, buttonSize.x, buttonSize.y)
            batch.draw(quitGameTexture, quitGameButtonX, quitGameButtonY, buttonSize.x, buttonSize.y)
        } else {
            // "apply.png" Button zeichnen, wenn Soundeinstellungen angezeigt werden
            val applyButtonX = boxX + (boxWidth - buttonSize.x) / 2
            val applyButtonY = boxY + (boxHeight - buttonSize.y) / 2 // Zentriert in der Box

            batch.draw(applyTexture, applyButtonX, applyButtonY, buttonSize.x, buttonSize.y)
        }


        batch.end()

        // Eingabe verarbeiten
        handleInput()
    }

    private fun handleInput() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            val mouseX = Gdx.input.x.toFloat()
            val mouseY = Gdx.graphics.height - Gdx.input.y.toFloat() // Y-Koordinate korrigieren

            val screenWidth = Gdx.graphics.width.toFloat()
            val screenHeight = Gdx.graphics.height.toFloat()

            val boxWidth = screenWidth * 0.30f
            val boxHeight = screenHeight * 0.6f

            val boxX = (screenWidth - boxWidth) / 2f
            var boxY = (screenHeight - boxHeight) / 2f

            val yOffset = screenHeight * 0.10f

            boxY -= yOffset

            // Button Y berechnung
            val startButtonY = boxY + boxHeight - 120f - buttonSize.y

            // resume
            val resumeButtonX = boxX + (boxWidth - buttonSize.x) / 2
            val resumeButtonY = startButtonY

            // soundsettings
            val soundSettingsButtonX = boxX + (boxWidth - buttonSize.x) / 2
            val soundSettingsButtonY = resumeButtonY - buttonSize.y - buttonSpacing

            // quitgame
            val quitGameButtonX = boxX + (boxWidth - buttonSize.x) / 2
            val quitGameButtonY = soundSettingsButtonY - buttonSize.y - buttonSpacing

            Gdx.app.log("DEBUG", "MouseX: $mouseX, MouseY: $mouseY")
            Gdx.app.log("DEBUG", "soundSettingsButtonX: $soundSettingsButtonX, soundSettingsButtonY: $soundSettingsButtonY")
            Gdx.app.log("DEBUG", "ButtonSize: ${buttonSize.x}, ${buttonSize.y}")

            // Überprüfen, ob der Soundsettings-Button angeklickt wurde
            if (mouseX >= soundSettingsButtonX && mouseX <= soundSettingsButtonX + buttonSize.x &&
                mouseY >= soundSettingsButtonY && mouseY <= soundSettingsButtonY + buttonSize.y) {
                Gdx.app.log("DEBUG", "SoundSettings Button clicked!")
                showSoundSettings = true // Soundeinstellungen anzeigen
            }

            // Überprüfen, ob der Apply-Button angeklickt wurde
            if (showSoundSettings) {
                val applyButtonX = boxX + (boxWidth - buttonSize.x) / 2
                val applyButtonY = boxY + (boxHeight - buttonSize.y) / 2 // Zentriert in der Box

                Gdx.app.log("DEBUG", "applyButtonX: $applyButtonX, applyButtonY: $applyButtonY")

                if (mouseX >= applyButtonX && mouseX <= applyButtonX + buttonSize.x &&
                    mouseY >= applyButtonY && mouseY <= applyButtonY + buttonSize.y) {
                    Gdx.app.log("DEBUG", "Apply Button clicked!")
                    showSoundSettings = false // Soundeinstellungen ausblenden
                }
            }
        }
    }

    override fun dispose() {
        batch.dispose()
        boxTexture.dispose()
        font.dispose() // Schriftart freigeben
        resumeTexture.dispose()
        soundSettingsTexture.dispose()
        quitGameTexture.dispose()
        wordmarkTexture.dispose() // Wordmark Textur freigeben
        applyTexture.dispose()
    }
}
