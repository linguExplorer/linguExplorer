package com.github.linguExplorer.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.math.Vector2
import com.github.linguExplorer.minigames.EssenMinigame
import com.github.linguExplorer.models.PhraseEntity

class GameScreen : Screen {

    private val batch = SpriteBatch()
    private val basketTexture = Texture(Gdx.files.internal("Minigames/Essen/minispielEssen/basket.png"))
    private val listTexture = Texture(Gdx.files.internal("Minigames/Essen/minispielEssen/list.png"))
    private val timeTexture = Texture(Gdx.files.internal("Minigames/Essen/minispielEssen/time.png"))
    private val pauseTexture = Texture(Gdx.files.internal("Minigames/Essen/minispielEssen/pause.png"))
    private val shelfTexture = Texture(Gdx.files.internal("Minigames/Essen/minispielEssen/shelf.png"))
    private val tryAgainButtonTexture = Texture(Gdx.files.internal("Minigames/Essen/minispielEssen/btn_tryAgain.png"))
    private val quitButtonTexture = Texture(Gdx.files.internal("Minigames/Essen/minispielEssen/btn_quitMinigame.png"))
    private val minigame = EssenMinigame()

    // overlay Buttons
    private val tryAgainButtonPosition = Vector2(430f, 150f)
    private val quitButtonPosition = Vector2(430f, 80f)
    private val tryAgainButtonSize = Vector2(180f, 150f)
    private val quitButtonSize = Vector2(180f, 150f)

    // Time
    private var timeLeft = 20 // Startzeit in Sekunden
    private val font = BitmapFont()
    // font Problem
    //private val font = BitmapFont(Gdx.files.internal("Minigames/Essen/fonts/726a9c58b5a74175b5665675d666eea2-12.fnt"))

    // Viewport für responsives Layout
    private val viewport: Viewport = ExtendViewport(800f, 600f)

    //Korb
    private val basketBasePosition = Vector2(40f, 20f) // Basiskorbposition
    private val basketSize = Vector2(900f, 500f) // Korbgröße (falls notwendig skalierbar)

    //Liste
    private val listPosition = Vector2(480f, 20f)
    private val listSize = Vector2(850f, 500f)

    //Time
    private val timePosition = Vector2(10f,200f)
    private val timeSize = Vector2(720f, 400f)

    // Pause-Button
    private val pausePosition = Vector2(30f,200f)
    private val pauseSize = Vector2(700f, 400f)

    // Overlay
    private val overlayColor = com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 0.5f) // 50% Schwarz

    // Regale
    private val shelfPosition1 = Vector2(200f,150f) // Oberes Regal
    private val shelfPosition2 = Vector2(200f,20f) // Unteres Regal
    private val shelfSize = Vector2(900f, 400f) // Regalgröße

    private var isDragging = false
    private var offsetX = 0f
    private var offsetY = 0f

    private var gameEnded = false // Flag für das Ende des Spiels

    init {
        // Lade alle Phrasen des Themas "Essen" und die Minigame-Phrasen
        minigame.loadAllPhrases()
        minigame.loadMinigamePhrases()
    }

    // Lade Phrasen mit zugehörigen Assets
    private val phrasesWithAssets = minigame.loadPhrasesWithAssets()

    // Erstelle die Objekte (Phrasen + Texturen)
    private val objects = phrasesWithAssets.map { (phrase, assetPath) ->
        DraggableObject(
            phrase = phrase,
            texture = Texture(Gdx.files.internal(assetPath)),
            x = (-400..-400).random().toFloat(),
            y = (-400..-400).random().toFloat()
        )
    }

    override fun show() {
        Gdx.input.inputProcessor = null // Setze das Input-System (falls nötig)
    }

    private var elapsedTime = 0f

    override fun render(delta: Float) {
        handleInput()

        // Zähle die verstrichene Zeit hinzu
        elapsedTime += delta

        // Wenn eine Sekunde vergangen ist, verringere die verbleibende Zeit
        if (elapsedTime >= 1f) {
            if (timeLeft > 0) {
                timeLeft -= 1 // Zeit verringern
            }
            elapsedTime = 0f // Reset der verstrichenen Zeit
        }


        // Sekunden für die Anzeige (00:00) Berechnen
        val minutes = timeLeft / 60
        val seconds = timeLeft % 60
        val timeText = String.format("%02d:%02d", minutes, seconds)

        //hellblau Hintergrund
        Gdx.gl.glClearColor(0.627f, 0.768f, 0.831f, 1f)
        // Bildschirm bereinigen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Viewport anwenden und Kamera aktualisieren
        viewport.apply()

        // Rendering starten
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()

        // Korb zeichnen
        batch.draw(basketTexture, basketPosition.x, basketPosition.y, basketSize.x, basketSize.y)

        // Liste zeichnen
        batch.draw(listTexture, listPosition.x, listPosition.y, listSize.x, listSize.y)

        // Zeit oben links
        batch.draw(timeTexture, timePosition.x, timePosition.y, timeSize.x, timeSize.y)
        // Zeit Text
        font.color.set(0f, 0f, 0f, 1f)
        font.draw(batch, timeText,  60f, 568f)

        // Pause Button
        batch.draw(pauseTexture, pausePosition.x, pausePosition.y, pauseSize.x, pauseSize.y)

        // Regale rechts
        batch.draw(shelfTexture, shelfPosition1.x, shelfPosition1.y, shelfSize.x, shelfSize.y)
        batch.draw(shelfTexture, shelfPosition2.x, shelfPosition2.y, shelfSize.x, shelfSize.y)

        // Objekte zeichnen
        objects.forEach { obj ->
            if (!obj.isCollected) {
                batch.draw(obj.texture, obj.x, obj.y)
            }
        }

        // Wenn Zeit abgelaufen transparente Overlay
        if (timeLeft <= 0) {
            // Setze die transparente Farbe für das Overlay (50% transparent)
            //batch.setColor(overlayColor)
            //batch.draw(basketTexture, 0f, 0f, 50f, 50f)

            // "Try Again" Text in der Mitte
            font.color.set(1f, 1f, 1f, 1f) // Weißer Text
            font.draw(batch, "GAME OVER", 475f, 350f) // In der Mitte

            // Buttons zeichnen
            batch.draw(tryAgainButtonTexture, tryAgainButtonPosition.x, tryAgainButtonPosition.y, tryAgainButtonSize.x, tryAgainButtonSize.y)
            batch.draw(quitButtonTexture, quitButtonPosition.x, quitButtonPosition.y, quitButtonSize.x, quitButtonSize.y)
        }

        batch.end()
    }

    private fun handleInput() {
        val mouseX = Gdx.input.x.toFloat() * viewport.worldWidth / Gdx.graphics.width
        val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.worldHeight / Gdx.graphics.height

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            objects.forEach { obj ->
                if (!isDragging && !obj.isCollected && isMouseInsideImage(mouseX, mouseY, obj)) {
                    isDragging = true
                    obj.isBeingDragged = true
                    offsetX = mouseX - obj.x
                    offsetY = mouseY - obj.y
                }

                if (obj.isBeingDragged) {
                    obj.x = mouseX - offsetX
                    obj.y = mouseY - offsetY
                }
            }
        } else {
            objects.forEach { obj ->
                if (obj.isBeingDragged) {
                    if (isImageInsideBasket(obj)) {
                        obj.isCollected = true
                        val isCorrect = minigame.phraseList.any { it.id == obj.phrase.id }
                        println(isCorrect)
                    }
                    obj.isBeingDragged = false
                }
            }
            isDragging = false
        }
    }

    private val basketPosition: Vector2
        get() = Vector2(
            basketBasePosition.x * (viewport.worldWidth / 800f),
            basketBasePosition.y * (viewport.worldHeight / 600f)
        )

    private fun isMouseInsideImage(mouseX: Float, mouseY: Float, obj: DraggableObject): Boolean {
        return mouseX in obj.x..(obj.x + obj.texture.width) && mouseY in obj.y..(obj.y + obj.texture.height)
    }

    private fun isImageInsideBasket(obj: DraggableObject): Boolean {
        val imageLeft = obj.x
        val imageRight = obj.x + obj.texture.width
        val imageBottom = obj.y
        val imageTop = obj.y + obj.texture.height

        val basketLeft = basketPosition.x
        val basketRight = basketPosition.x + basketSize.x
        val basketBottom = basketPosition.y
        val basketTop = basketPosition.y + basketSize.y

        return imageRight > basketLeft && imageLeft < basketRight && imageTop > basketBottom && imageBottom < basketTop
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun hide() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        batch.dispose()
        basketTexture.dispose()
        objects.forEach { it.texture.dispose() }
        timeTexture.dispose()
        font.dispose()
    }

    private data class DraggableObject(
        val phrase: PhraseEntity,
        val texture: Texture,
        var x: Float,
        var y: Float,
        var isCollected: Boolean = false,
        var isBeingDragged: Boolean = false
    )
}
