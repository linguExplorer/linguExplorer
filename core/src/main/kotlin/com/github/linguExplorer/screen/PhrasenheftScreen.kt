package com.github.linguExplorer.screen

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Gdx
import com.github.linguExplorer.repositories.PhraseProgressRepository
import com.github.linguExplorer.repositories.PhraseRepository
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class PhrasenheftScreen : Screen {
    private val font = BitmapFont()
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer() // ShapeRenderer für abgerundete Rechtecke

    private val phrases = PhraseProgressRepository().getAllPhraseProgressForUser(1)

    private var scrollPosition = 0f // Scroll-Position (startY wird angepasst)
    private val lineHeight = 30f // Höhe jeder Zeile
    private val spacing = 300f // Abstand zwischen Phrase und Übersetzung
    private val padding = 20f // Abstand vom Rand zum Inhalt

    override fun show() {}

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) // Bildschirm löschen
        batch.begin()
        font.color = Color.WHITE

        // Bildschirmgröße holen
        val screenHeight = Gdx.graphics.height
        val screenWidth = Gdx.graphics.width

        // Hintergrund zeichnen: abgerundetes Rechteck
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(0.6f, 0.8f, 0.6f, 1f) // Leicht grünliche Farbe
        shapeRenderer.rect(padding, padding, screenWidth - padding * 2, screenHeight - padding * 2) // abgerundete Ecken mit Radius 20
        shapeRenderer.end()

        batch.end()

        // Anzeige der Phrasen innerhalb des Hintergrunds
        batch.begin()

        var startY = screenHeight - 50f + scrollPosition // StartY basierend auf der Scroll-Position
        val startX = 50f

        // Anzeige der Phrasen
        for (phrase in phrases) {
            val phraseText = PhraseRepository().getPhrase(phrase.phraseId)?.phrase ?: ""
            val translationText = PhraseRepository().getPhrase(phrase.phraseId)?.translation ?: ""

            font.draw(batch, phraseText, startX, startY)
            font.draw(batch, translationText, startX + spacing, startY)

            startY -= lineHeight // Zeilenhöhe nach unten verschieben
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
        val totalContentHeight = phrases.size * lineHeight // Gesamthöhe des Inhalts (alle Phrasen)
        val maxScroll = totalContentHeight - screenHeight // Maximaler Scrollwert

        // Begrenzung, dass man nicht über das Ende der Liste hinaus scrollen kann
        if (scrollPosition < -maxScroll) {
            scrollPosition = -maxScroll // Nicht weiter nach unten als das Ende der Liste
        }
    }

    override fun resize(width: Int, height: Int) {}

    override fun hide() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        batch.dispose()
        font.dispose()
        shapeRenderer.dispose() // ShapeRenderer freigeben
    }
}
