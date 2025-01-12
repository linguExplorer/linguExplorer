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
    private val font = BitmapFont()
    private val viewport: Viewport = ExtendViewport(800f, 600f)

    // Texturen
    private val basketTexture = Texture(Gdx.files.internal("Minigames/Essen/basket.png"))
    private val listTexture = Texture(Gdx.files.internal("Minigames/Essen/list.png"))
    private val timeTexture = Texture(Gdx.files.internal("Minigames/Essen/time.png"))
    private val pauseTexture = Texture(Gdx.files.internal("Minigames/Essen/pause.png"))
    private val shelfTexture = Texture(Gdx.files.internal("Minigames/Essen/shelf.png"))
    private val tryAgainButtonTexture = Texture(Gdx.files.internal("Minigames/Essen/btn_tryAgain.png"))
    private val quitButtonTexture = Texture(Gdx.files.internal("Minigames/Essen/btn_quitMinigame.png"))

    // Positionen und Größen
    private val basketPosition = Vector2(40f, 20f)
    private val basketSize = Vector2(650f, 450f)
    private val listPosition = Vector2(480f, 20f)
    private val listSize = Vector2(1200f, 600f)
    private val pausePosition = Vector2(30f, 200f)
    private val pauseSize = Vector2(700f, 400f)

    private val shelfPosition1 = Vector2(200f, 150f)
    private val shelfPosition2 = Vector2(200f, 20f)
    private val shelfSize = Vector2(900f, 400f)

    private val tryAgainButtonPosition = Vector2(430f, 150f)
    private val quitButtonPosition = Vector2(430f, 80f)
    private val buttonSize = Vector2(180f, 150f)

    private val timePosition = Vector2(10f, 200f)
    private val timeSize = Vector2(700f, 400f)

    // Zeit
    private var timeLeft = 20
    private var elapsedTime = 0f

    // Spielstatus
    private var isDragging = false
    private var offsetX = 0f
    private var offsetY = 0f
    private var gameEnded = false

    private val minigame = EssenMinigame()
    private val objects: List<DraggableObject>

    init {
        // Initialisierung
        minigame.loadAllPhrases()
        minigame.loadMinigamePhrases()

        objects = minigame.loadPhrasesWithAssets().map { (phrase, assetPath) ->
            DraggableObject(
                phrase = phrase,
                texture = Texture(Gdx.files.internal(assetPath)),
                x = (400..700).random().toFloat(),
                y = (100..500).random().toFloat()
            )
        }
    }

    override fun show() {
        Gdx.input.inputProcessor = null
    }

    override fun render(delta: Float) {
        handleInput()
        updateTime(delta)

        // Bildschirm bereinigen
        Gdx.gl.glClearColor(0.611f, 0.761f, 0.827f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined

        // Rendering starten
        batch.begin()

        // Hintergrundelemente
        batch.draw(basketTexture, basketPosition.x, basketPosition.y, basketSize.x, basketSize.y)
        batch.draw(listTexture, listPosition.x, listPosition.y, listSize.x, listSize.y)
        batch.draw(pauseTexture, pausePosition.x, pausePosition.y, pauseSize.x, pauseSize.y)
        batch.draw(shelfTexture, shelfPosition1.x, shelfPosition1.y, shelfSize.x, shelfSize.y)
        batch.draw(shelfTexture, shelfPosition2.x, shelfPosition2.y, shelfSize.x, shelfSize.y)
        batch.draw(timeTexture, timePosition.x, timePosition.y, timeSize.x, timeSize.y)

        // Objekte zeichnen
        objects.forEach { obj ->
            if (!obj.isCollected) {
                batch.draw(obj.texture, obj.x, obj.y)
            }
        }

        // Zeit zeichnen
        font.draw(batch, formatTime(timeLeft), 60f, 568f)

        // Spielende-Overlay
        if (gameEnded) {
            font.draw(batch, "GAME OVER", 475f, 350f)
            batch.draw(tryAgainButtonTexture, tryAgainButtonPosition.x, tryAgainButtonPosition.y, buttonSize.x, buttonSize.y)
            batch.draw(quitButtonTexture, quitButtonPosition.x, quitButtonPosition.y, buttonSize.x, buttonSize.y)
        }

        batch.end()
    }

    private fun handleInput() {
        if (gameEnded) return

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
                    obj.isBeingDragged = false
                    if (isImageInsideBasket(obj)) {
                        obj.isCollected = true
                        minigame.phraseCheck(obj.phrase, true) // Passe hier an, wenn Falsch-Prüfung nötig
                    }
                }
            }
            isDragging = false
        }
    }

    private fun updateTime(delta: Float) {
        elapsedTime += delta
        if (elapsedTime >= 1f && timeLeft > 0) {
            timeLeft--
            elapsedTime = 0f
        } else if (timeLeft <= 0) {
            gameEnded = true
        }
    }

    private fun formatTime(time: Int): String {
        val minutes = time / 60
        val seconds = time % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun isMouseInsideImage(mouseX: Float, mouseY: Float, obj: DraggableObject): Boolean {
        return mouseX in obj.x..(obj.x + obj.texture.width) && mouseY in obj.y..(obj.y + obj.texture.height)
    }

    private fun isImageInsideBasket(obj: DraggableObject): Boolean {
        return obj.x + obj.texture.width > basketPosition.x &&
            obj.x < basketPosition.x + basketSize.x &&
            obj.y + obj.texture.height > basketPosition.y &&
            obj.y < basketPosition.y + basketSize.y
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun hide() {}
    override fun pause() {}
    override fun resume() {}

    override fun dispose() {
        batch.dispose()
        font.dispose()
        basketTexture.dispose()
        listTexture.dispose()
        timeTexture.dispose()
        pauseTexture.dispose()
        shelfTexture.dispose()
        tryAgainButtonTexture.dispose()
        quitButtonTexture.dispose()
        objects.forEach { it.texture.dispose() }
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
