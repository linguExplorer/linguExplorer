package com.github.linguExplorer.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.repositories.PhraseProgressRepository
import com.github.linguExplorer.repositories.PhraseRepository
import ktx.app.KtxScreen
import ktx.assets.disposeSafely

class PhrasenheftScreen : KtxScreen {
    private val font = BitmapFont()
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()


    private val phraseProgresses = PhraseProgressRepository().getAllPhraseProgressForUser(1)
    private val phrases = phraseProgresses.map {
        PhraseRepository().getPhrase(it.phraseId)!!.phrase to PhraseRepository().getPhrase(it.phraseId)!!.translation

    }

    private var scrollPosition = 0f // Scroll-Position (startY wird angepasst)
    private val lineHeight = 55f // Höhe jeder Zeile
    private val spacing = 280f // Abstand zwischen Phrase und Übersetzung
    private val padding = 20f // Abstand vom Rand zum Inhalt
    private var currentY = -177f // Wir definieren currentY hier und aktualisieren es in der render-Methode





    // Texturen
    private val heftTexture = Texture(Gdx.files.internal("Phrasenheft/heft_design.png"))
    private val sortTexture = Texture(Gdx.files.internal("Phrasenheft/Sort.png"))
    private val nextTexture = Texture(Gdx.files.internal("Phrasenheft/weiter.png"))
    private val backTexture = Texture(Gdx.files.internal("Phrasenheft/zurueck.png"))

    private val heftSize = Vector2(heftTexture.width.toFloat()*8, heftTexture.height.toFloat()*8)
    private val nextSize = Vector2(backTexture.width.toFloat()*8, backTexture.height.toFloat()*8)
    private val backSize = Vector2(backTexture.width.toFloat()*8, backTexture.height.toFloat()*8)
    private val sortSize = Vector2(sortTexture.width.toFloat()*6, sortTexture.height.toFloat()*6)


    private val viewport: Viewport = ExtendViewport(800f, 600f)






    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        viewport.camera.position.set(viewport.worldWidth / 2, viewport.worldHeight / 2, 0f)
        viewport.camera.update()
    }

    override fun render(delta: Float) {
        // Update the viewport
        viewport.apply()

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) // Bildschirm löschen

        batch.begin() // Beginnt das Zeichnen
        font.color = Color.BLACK

        // Bildschirmgröße holen
        val screenHeight = viewport.screenHeight
        val screenWidth = viewport.screenWidth

        // Hintergrund zeichnen: abgerundetes Rechteck
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(156 / 255f, 194 / 255f, 211 / 255f, 1f) // Leicht grünliche Farbe
        shapeRenderer.rect(padding, padding, screenWidth - padding * 2, screenHeight - padding * 2) // abgerundete Ecken
        shapeRenderer.end()


        batch.end()

        // Anzeige der Phrasen innerhalb des Hintergrunds
        batch.begin()

        batch.draw(heftTexture, (screenWidth/2f - heftSize.x/2f) , (screenHeight/2f - heftSize.y/2f), heftSize.x, heftSize.y)

        batch.draw(backTexture, screenWidth/2 - 690f , (screenHeight  - heftSize.y) - 200f, backSize.x, backSize.y)
        batch.draw(nextTexture, screenWidth/2 + 600f , (screenHeight  - heftSize.y) - 200f, backSize.x, backSize.y)
        batch.draw(sortTexture,screenWidth/2 - 690f, (screenHeight/2f + heftSize.y/2f) - sortSize.y , sortSize.x, sortSize.y)

        val startX = screenWidth/4f
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
        heftTexture.disposeSafely()
        nextTexture.disposeSafely()
        backTexture.disposeSafely()
        sortTexture.disposeSafely()
        shapeRenderer.dispose()
    }
}
