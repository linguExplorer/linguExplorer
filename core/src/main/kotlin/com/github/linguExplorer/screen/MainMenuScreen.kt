package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle

class MainMenuScreen : Screen {

    private var batch: SpriteBatch = SpriteBatch()
    private var shapeRenderer: ShapeRenderer = ShapeRenderer()

    // Texturen Hintergrundbild/Buttons
    private var backgroundTexture: Texture = Texture("xx_map_assets/Map/ref.png")
    private var startNewGameTexture: Texture = Texture("xx_Images/Buttons/neuesSpiel_green.png")
    private var loadGameTexture: Texture = Texture("xx_Images/Buttons/spielstandLaden_green.png")
    private var settingsIconTexture: Texture = Texture("xx_Images/SettingsIcon.png")
    private var wordmarkTexture: Texture = Texture("xx_Images/wordmark/wordmark_scaled.png")

    // Rechtecke -> Positionierung von Buttons auf dem Bildschirm
    private val startNewGameButton = Rectangle()
    private val loadGameButton = Rectangle()
    private val settingsButton = Rectangle()

    // Rechteck für Overlay
    private val overlayWidth = 1200f
    private val overlayHeight = 700f
    private val settingsIconSize = 60f

    // horizontalen Offset
    private var backgroundOffsetX = 0f
    private var backgroundOffsetY = 0f

    // Geschwindigkeit von Bewegung
    private var speedX = 100f
    private var speedY = 50f

    // Methode Screen anzeigen
    override fun show() {
        // Bildschirmgröße abrufen
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()

        // Position und Größe von Buttons
        loadGameButton.set(100f, screenHeight / 2 - 100f, 200f, 50f)
        startNewGameButton.set(100f, screenHeight / 2 - 200f, 200f, 50f)
        settingsButton.set(screenWidth - 70f, screenHeight - 70f, 60f, 60f)
    }

    override fun render(delta: Float) {
        // Bildschirmabmessungen
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()

        // Hintergrundgröße berechnen
        val backgroundWidth = backgroundTexture.width.toFloat()
        val backgroundHeight = backgroundTexture.height.toFloat()

        // Skalierungsfaktor für den Hintergrund
        val scale = 1.5f
        val scaleX = screenWidth / backgroundWidth * scale
        val scaleY = screenHeight / backgroundHeight * scale
        val scaleFactor = Math.max(scaleX, scaleY)

        val scaledWidth = backgroundWidth * scaleFactor
        val scaledHeight = backgroundHeight * scaleFactor

        // Offset aktualisieren basierend auf Geschwindigkeit und Zeit
        backgroundOffsetX += speedX * delta
        backgroundOffsetY += speedY * delta

        // Kollisionsprüfung für die Ränder
        if (backgroundOffsetX < -(scaledWidth - screenWidth)) {
            backgroundOffsetX = -(scaledWidth - screenWidth)
            speedX = -speedX // X-Richtung umkehren
        } else if (backgroundOffsetX > 0) {
            backgroundOffsetX = 0f
            speedX = -speedX // X-Richtung umkehren
        }

        if (backgroundOffsetY < -(scaledHeight - screenHeight)) {
            backgroundOffsetY = -(scaledHeight - screenHeight)
            speedY = -speedY // Y-Richtung umkehren
        } else if (backgroundOffsetY > 0) {
            backgroundOffsetY = 0f
            speedY = -speedY // Y-Richtung umkehren
        }

        // Hintergrund zeichnen
        batch.begin()
        batch.draw(backgroundTexture, backgroundOffsetX, backgroundOffsetY, scaledWidth, scaledHeight)
        batch.end()


        //Zeichne Rechteck
        val overlayColor = Color(0f, 0f, 0f, 0.65f)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = overlayColor
        val overlayX = screenWidth / 2 - overlayWidth / 2
        val overlayY = screenHeight / 2 - overlayHeight / 2
        shapeRenderer.rect(overlayX, overlayY, overlayWidth, overlayHeight)
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        batch.begin()
        // Wordmark
        val wordmarkHeight = 220f
        val wordmarkAspectRatio = wordmarkTexture.width.toFloat() / wordmarkTexture.height.toFloat()
        val wordmarkWidth = wordmarkHeight * wordmarkAspectRatio
        val wordmarkX = screenWidth / 2 - 560f
        val wordmarkY = screenHeight / 2 + 20f
        batch.draw(wordmarkTexture, wordmarkX, wordmarkY, wordmarkWidth, wordmarkHeight)

        // Position der Buttons
        val buttonX = screenWidth / 2 - 500f
        val startNewGameButtonY = screenHeight / 2 - 90f
        val loadGameButtonY = screenHeight / 2 - 205f

        // Buttons
        batch.draw(startNewGameTexture, buttonX, startNewGameButtonY)
        batch.draw(loadGameTexture, buttonX, loadGameButtonY)

        // Settings Button
        val settingsX = screenWidth - settingsIconSize - 10f
        val settingsY = screenHeight - settingsIconSize - 10f
        batch.draw(settingsIconTexture, settingsX, settingsY, settingsIconSize, settingsIconSize)

        // Beendet Zeichnen
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        // Viewport-Größe anpassen
    }

    override fun pause() {
        // Pause-Logik hier hinzufügen
    }

    override fun resume() {
        // Resume-Logik hier hinzufügen
    }

    override fun hide() {
        // Ressourcen freigeben
    }

    override fun dispose() {
        // Ressourcen freigeben, wenn Screen nicht mehr benötigt
        batch.dispose()
        shapeRenderer.dispose()
        backgroundTexture.dispose()
        startNewGameTexture.dispose()
        loadGameTexture.dispose()
        settingsIconTexture.dispose()
        wordmarkTexture.dispose()
    }

    private fun startNewGame() {
        println("Neues Spiel starten")
    }

    private fun loadGame() {
        println("Spielstand laden")
    }

    private fun openSettings() {
        println("Einstellungen öffnen")
    }
}
