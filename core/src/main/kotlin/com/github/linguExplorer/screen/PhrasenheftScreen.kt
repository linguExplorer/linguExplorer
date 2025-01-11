package com.github.linguExplorer.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.repositories.PhraseProgressRepository
import com.github.linguExplorer.repositories.PhraseRepository

class PhrasenheftScreen : Screen {
    private val font = BitmapFont()
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()

    private val phraseProgresses = PhraseProgressRepository().getAllPhraseProgressForUser(1)
    private val phrases = phraseProgresses.map {
        PhraseRepository().getPhrase(it.phraseId)!!.phrase to PhraseRepository().getPhrase(it.phraseId)!!.translation
    }

    private var scrollPosition = 0f // Scroll-Position (startY wird angepasst)
    private val lineHeight = 30f // Höhe jeder Zeile
    private val spacing = 300f // Abstand zwischen Phrase und Übersetzung
    private val padding = 20f // Abstand vom Rand zum Inhalt
    private var currentY = 0f // Wir definieren currentY hier und aktualisieren es in der render-Methode

    // Define a viewport that adapts to screen size
    private val viewport: Viewport = ExtendViewport(800f, 600f)

    override fun show() {
        // Set the viewport to match the screen dimensions
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)

        // initialisiere currentY bei Bildschirmhöhe und der aktuellen Scroll-Position
        currentY = viewport.screenHeight - padding * 2 + scrollPosition
    }

    override fun render(delta: Float) {
        // Update the viewport
        viewport.apply()

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) // Bildschirm löschen

        batch.begin() // Beginnt das Zeichnen
        font.color = Color.WHITE

        // Bildschirmgröße holen
        val screenHeight = viewport.screenHeight
        val screenWidth = viewport.screenWidth

        // Hintergrund zeichnen: abgerundetes Rechteck
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(0.6f, 0.8f, 0.6f, 1f) // Leicht grünliche Farbe
        shapeRenderer.rect(padding, padding, screenWidth - padding * 2, screenHeight - padding * 2) // abgerundete Ecken
        shapeRenderer.end()

        batch.end()

        // Anzeige der Phrasen innerhalb des Hintergrunds
        batch.begin()

        val startX = 50f
        var adjustedY = currentY + scrollPosition // Berücksichtige die Scroll-Position

        // Anzeige der Phrasen
        for (phrase in phrases) {
            val phraseText = phrase.first
            val translationText = phrase.second

            // Debugging: Gebe die Position aus, an der der Text gezeichnet wird
            println("Drawing text at Y: $adjustedY")

            font.draw(batch, phraseText, startX, adjustedY)
            font.draw(batch, translationText, startX + spacing, adjustedY)

            adjustedY -= lineHeight // Zeilenhöhe nach unten verschieben
        }

        batch.end()

        // Wenn die Pfeiltaste nach unten gedrückt wird, die Scroll-Position anpassen
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            scrollPosition -= lineHeight // Scrollen nach unten
        }

        // Begrenzung, dass man nicht zu weit nach oben scrollen kann
        if (scrollPosition > 0) {
            scrollPosition = 0f // Nicht weiter nach oben als der Anfang
        }

        // Berechnung des maximalen Scrollwerts:
        val totalContentHeight = phrases.size * lineHeight // Gesamthöhe des Inhalts
        val maxScroll = totalContentHeight - screenHeight // Maximaler Scrollwert

        // Begrenzung, dass man nicht über das Ende der Liste hinaus scrollen kann
        if (scrollPosition < -maxScroll) {
            scrollPosition = -maxScroll // Nicht weiter nach unten als das Ende der Liste
        }
    }

    override fun resize(width: Int, height: Int) {
        // Update the viewport on resize
        viewport.update(width, height, true)
    }

    override fun hide() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        batch.dispose()
        font.dispose()
        shapeRenderer.dispose()
    }
}
