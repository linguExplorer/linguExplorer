package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
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
    //private val overlayColor = Color(0xf6.toFloat() / 255f, 0xf5.toFloat() / 255f, 0xf1.toFloat() / 255f, 1f)
    //private val overlayWidth = 1200f
    //private val overlayHeight = 700f
    private val settingsIconSize = 60f

    // horizontalen Offset
    private var backgroundOffsetX = 0f

    // Geschwindigkeit von Bewegung
    private val scrollSpeed = 30f // Geschwindigkeit (höher=schneller)

    // Breite speichern
    private var newWidth : Float = 0f

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

    // In jedem Frame aufgerufen -> Anzeige aktualisieren
    override fun render(delta: Float) {
        // Hintergrundbild skalieren und zentrieren
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()

        // Skalierungsfaktor
        val scale = 2.5f
        val backgroundWidth = backgroundTexture.width.toFloat() * scale
        val backgroundHeight = backgroundTexture.height.toFloat() * scale

        // Skalierung von Hintergrundbild berechnen
        val scaleX = screenWidth / backgroundWidth
        val scaleY = screenHeight / backgroundHeight
        val scaleFactor = Math.max(scaleX, scaleY)

        // neuen Position für Bild berechnen
        newWidth = backgroundWidth * scaleFactor //Bildbreite
        val newHeight = backgroundHeight * scaleFactor
        val backgroundY = (screenHeight - newHeight) / 2

        // richtung der Bewegung
        backgroundOffsetX -= scrollSpeed * delta // (bild geht so nach links)

        // ob Bild sich wiederholen muss
        if (backgroundOffsetX < -newWidth) {
            backgroundOffsetX += newWidth
        }

        // Beginnt Zeichnen
        batch.begin()
        // zwei Hintergrundbilder nebeneinander!
        batch.draw(backgroundTexture, backgroundOffsetX, backgroundY, newWidth, newHeight)
        batch.draw(backgroundTexture, backgroundOffsetX + newWidth, backgroundY, newWidth, newHeight)
        batch.end()

        // Zeichne Rechteck
        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = overlayColor
        val overlayX = screenWidth / 2 - overlayWidth / 2
        val overlayY = screenHeight / 2 - overlayHeight / 2
        shapeRenderer.rect(overlayX, overlayY, overlayWidth, overlayHeight)
        shapeRenderer.end()*/

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
