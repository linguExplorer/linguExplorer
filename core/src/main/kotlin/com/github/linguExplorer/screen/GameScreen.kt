package com.github.linguExplorer.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.math.Vector2
import com.github.linguExplorer.minigames.EssenMinigame
import com.github.linguExplorer.models.PhraseEntity

class GameScreen : Screen {

    private val batch = SpriteBatch()
    private val basketTexture = Texture(Gdx.files.internal("Minigames/Essen/basket.png"))
    private val listTexture = Texture(Gdx.files.internal("Minigames/Essen/list.png"))
    private val pauseButtonTexture = Texture(Gdx.files.internal("Minigames/pause.png"))
    private val playButtonTexture = Texture(Gdx.files.internal("Minigames/play.png"))
    private val minigame = EssenMinigame()

    // Viewport für responsive Layout
    private val viewport: Viewport = ExtendViewport(800f, 600f)
    private val basketBasePosition = Vector2(100f, 0f)
    private val basketSize = Vector2(650f, 450f)
    private val listBasePosition = Vector2(100f, 0f)
    private val listSize = Vector2(1200f, 600f)
    private val pauseButtonBasePosition = Vector2(100f, 0f)
    private val pauseButtonSize = Vector2(900f, 500f)

    private var isDragging = false
    private var offsetX = 0f
    private var offsetY = 0f

    init {
        // initialisere das minigame (!!)
        minigame.loadAllPhrases()
        minigame.loadMinigamePhrases()
    }

    // lädt die Phrasen mit zugehörigen Assets (!!)
    private val phrasesWithAssets = minigame.loadPhrasesWithAssets()

    // Erstellt die Objekte (Phrasen + Texturen miteinander verknüpft) (!!)
    private val objects = phrasesWithAssets.map { (phrase, assetPath) ->
        DraggableObject(
            phrase = phrase,
            texture = Texture(Gdx.files.internal(assetPath)),
            x = (400..700).random().toFloat(),
            y = (100..500).random().toFloat()
        )
    }

    override fun show() {
        Gdx.input.inputProcessor = null // Setzt das Input-System (falls nötig)
    }

    override fun render(delta: Float) {
        handleInput()

        Gdx.gl.glClearColor(0.611f, 0.761f, 0.827f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.apply()

        // Rendering starten
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()

        objects.forEach { obj ->
            if (!obj.isCollected) {
                batch.draw(obj.texture, obj.x, obj.y)
            } else if (obj.fadeAlpha > 0f) {
                batch.setColor(1f, 1f, 1f, obj.fadeAlpha)
                batch.draw(obj.texture, obj.x, obj.y)
                batch.setColor(1f, 1f, 1f, 1f)

                obj.fadeAlpha -= delta * 1.5f
                if (obj.fadeAlpha <= 0f) {
                    obj.fadeAlpha = 0f
                }
            }
        }

        batch.draw(basketTexture, basketPosition.x, basketPosition.y, basketSize.x, basketSize.y)
        batch.draw(listTexture, listPosition.x, listPosition.y, listSize.x, listSize.y)
        batch.draw(pauseButtonTexture, pauseButtonPosition.x, pauseButtonPosition.y, pauseButtonSize.x, pauseButtonSize.y)

        batch.end()

        //ist nur zum testen, wie lange das spiel braucht um sich zu schließen
        if (minigame.isGameComplete()) {
            minigame.storePhraseData()
            closeScreen()
        }
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
            // das image befindet sich im basket und die minigame funktioneren werden ausgeführt (!!)
            // von den methoden her am besten nichts mehr ändern, aber du müsstest es trotzdem anpassen, um eben aktionen zu setzen, wenn die phrase falsch ist
            objects.forEach { obj ->
                if (obj.isBeingDragged) {
                    obj.isBeingDragged = false
                    if (isImageInsideBasket(obj)) {
                        obj.isCollected = true
                        obj.fadeAlpha = 1f
                        val isCorrect = minigame.phraseList.any { it.id == obj.phrase.id }
                        minigame.phraseCheck(obj.phrase, isCorrect)
                    }
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

    private val listPosition: Vector2
        get() = Vector2(
            listBasePosition.x * (viewport.worldWidth / 800f),
            listBasePosition.y * (viewport.worldHeight / 600f)
        )

    private val pauseButtonPosition: Vector2
        get() = Vector2(
            pauseButtonBasePosition.x * (viewport.worldWidth / 800f),
            pauseButtonBasePosition.y
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

    private fun closeScreen() {
        Gdx.app.exit()
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
    }

    private data class DraggableObject(
        val phrase: PhraseEntity,
        val texture: Texture,
        var x: Float,
        var y: Float,
        var isCollected: Boolean = false,
        var isBeingDragged: Boolean = false,
        var fadeAlpha: Float = 1f
    )
}
