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


    private val phrasesOfProgress = PhraseProgressRepository().getAllPhrasesOfUserProgress(123)
    private val phrases = phrasesOfProgress.map {
        it.phrase to it.translation

    }

    private var scrollPosition = 0f // Scroll-Position (startY wird angepasst)
    private val lineHeight = 55f // Höhe jeder Zeile
    private val spacing = 280f // Abstand zwischen Phrase und Übersetzung
    private val padding = 20f // Abstand vom Rand zum Inhalt
    private var currentY = 775f // Wir definieren currentY hier und aktualisieren es in der render-Methode





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


    private var maxPages= (phrases.size/10f)//wie viele Phrasen max
    private var currentPage = 1

    private val nextPosition: Vector2
        get() = Vector2(
            viewport.screenWidth/2 + 600f,
            (viewport.screenHeight - heftSize.y) - 200f
        )


    private val backPosition: Vector2
        get() = Vector2(
            viewport.screenWidth/2 - 690f,
            (viewport.screenHeight  - heftSize.y) - 200f
        )



    override fun show() {

        Gdx.input.inputProcessor = null

        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        viewport.camera.position.set(viewport.worldWidth / 2, viewport.worldHeight / 2, 0f)
        viewport.camera.update()
    }

    override fun render(delta: Float) {
        // Update the viewport
        handleInput()

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

        if((currentPage-1) > 0) {
            batch.draw(backTexture, screenWidth/2 - 690f , (screenHeight  - heftSize.y) - 200f, backSize.x, backSize.y)

        }
        if((currentPage-1) + 1 <=  maxPages-1f) {
            batch.draw(nextTexture, nextPosition.x , nextPosition.y, backSize.x, backSize.y)

        }
        batch.draw(sortTexture,screenWidth/2 - 690f, (screenHeight/2f + heftSize.y/2f) - sortSize.y , sortSize.x, sortSize.y)

        val startX = screenWidth/4f + 30f
        var adjustedY = currentY // Berücksichtige die Scroll-Position






        val phrasesPerPage = 20
        var multiplikator = 10
        val phrasesPerColumn = 10
        // Anzeige der Phrasen


        for ((index, phrase) in phrases.withIndex()) {


            val pageStartIndex = if(currentPage == 1) {
                currentPage * 0
            } else {
                (currentPage * 10)+1

            }


            val pageEndIndex = pageStartIndex + phrasesPerPage


            if(index >= pageStartIndex && index <= pageEndIndex+1 ) {

                val phraseText = phrase.first
                val translationText = phrase.second



                val columnOffset = if ((index - pageStartIndex) <= phrasesPerColumn) {
                    0f
                } else {
                    550f
                }

                font.draw(batch, phraseText, startX + columnOffset, adjustedY)
                font.draw(batch, translationText, startX + spacing + columnOffset, adjustedY)

                adjustedY -= lineHeight

                if ((index - pageStartIndex) == phrasesPerColumn) {
                    adjustedY = currentY // Setze die Y-Position zurück
                }
            }

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



    private fun handleInput() {
        val mouseX = Gdx.input.x.toFloat() * viewport.screenWidth/ Gdx.graphics.width
        val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.screenHeight / Gdx.graphics.height


        if (Gdx.input.justTouched()) {


                if (mouseX in nextPosition.x..(nextPosition.x + backSize.x) && mouseY in nextPosition.y..(nextPosition.y + backSize.y)
                ) {

                    if((currentPage-1) + 1 <  maxPages-1f) {
                        currentPage++

                    }
                    println("Button Next, $currentPage")

                }

            if (mouseX in backPosition.x..(backPosition.x + backSize.x) && mouseY in backPosition.y..(backPosition.y + backSize.y)
            ) {

                if((currentPage-1) - 1 >=  0) {
                    currentPage--

                }
                println("Button Back, $currentPage")
            }

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
