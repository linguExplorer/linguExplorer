package com.github.linguExplorer.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.math.Vector2
import com.github.linguExplorer.minigames.EssenMinigame
import com.github.linguExplorer.models.PhraseEntity

class GameScreen : Screen {

    private val batch = SpriteBatch()
    private val font = BitmapFont()
    private val viewport: Viewport = ExtendViewport(800f, 600f)
    private val shapeRenderer = ShapeRenderer()

    // Texturen
    private val basketTexture = Texture(Gdx.files.internal("Minigames/basket.png"))
    private val listTexture = Texture(Gdx.files.internal("Minigames/list.png"))
    private val timeTexture = Texture(Gdx.files.internal("Minigames/time.png"))
    private var pauseTexture = Texture(Gdx.files.internal("Minigames/pausebutton.png"))
    private val shelfTexture = Texture(Gdx.files.internal("Minigames/shelf.png"))
    private val tryAgainButtonTexture = Texture(Gdx.files.internal("Minigames/btn_tryAgain.png"))
    private val quitButtonTexture = Texture(Gdx.files.internal("Minigames/btn_quitMinigame.png"))

    // Positionen und Größen
    private val basketBasePosition = Vector2(60f, 0f)
    private val basketSize = Vector2(350f, 260f)

    private val listBasePosition = Vector2(550f, 0f)
    private val listSize = Vector2(240f, 250f)

    private val pauseBasePosition = Vector2(180f, 530f)
    private val pauseSize = Vector2(50f, 50f)

    private val shelfBasePosition1 = Vector2(350f, 450f)
    private val shelfBasePosition2 = Vector2(350f, 300f)
    private val shelfSize = Vector2(650f, 25f)

    private val tryAgainButtonBasePosition = Vector2(430f, 150f)
    private val quitButtonBasePosition = Vector2(430f, 80f)
    private val buttonSize = Vector2(180f, 150f)

    private val timeBasePosition = Vector2(20f, 530f)
    private val timeSize = Vector2(150f, 50f)

    // Getter für die dynamischen Positionen
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

    private val pausePosition: Vector2
        get() = Vector2(
            pauseBasePosition.x,
            pauseBasePosition.y * (viewport.worldHeight / 600f)
        )

    private val shelfPosition1: Vector2
        get() = Vector2(
            shelfBasePosition1.x * (viewport.worldWidth / 800f),
            shelfBasePosition1.y * (viewport.worldHeight / 600f)
        )

    private val shelfPosition2: Vector2
        get() = Vector2(
            shelfBasePosition2.x * (viewport.worldWidth / 800f),
            shelfBasePosition2.y * (viewport.worldHeight / 600f)
        )

    private val tryAgainButtonPosition: Vector2
        get() = Vector2(
            tryAgainButtonBasePosition.x * (viewport.worldWidth / 800f),
            tryAgainButtonBasePosition.y * (viewport.worldHeight / 600f)
        )

    private val quitButtonPosition: Vector2
        get() = Vector2(
            quitButtonBasePosition.x * (viewport.worldWidth / 800f),
            quitButtonBasePosition.y * (viewport.worldHeight / 600f)
        )

    private val timePosition: Vector2
        get() = Vector2(
            timeBasePosition.x,
            timeBasePosition.y * (viewport.worldHeight / 600f)
        )


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
        minigame.loadMinigamePhrases()
        minigame.loadAllPhrases()

        objects = minigame.loadPhrasesWithAssets().map { (phrase, assetPath) ->
            DraggableObject(
                phrase = phrase,
                texture = Texture(Gdx.files.internal(assetPath)),
                resetPositionX = 370f,
                resetPositionY = 480f,
                basePositionX = 370f,
                basePositionY = 480f,
                positionX = 0f,
                positionY = 0f,
                positionOffsetX = 0f,
                positionOffsetY = 0f,
                sizeX = 50f,
                sizeY = 50f
            )
        }
    }

    override fun show() {
        Gdx.input.inputProcessor = null
    }

    override fun render(delta: Float) {
        handleInput()
        updateTime(delta)

        Gdx.gl.glClearColor(0.611f, 0.761f, 0.827f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined


        batch.begin()
        batch.draw(shelfTexture, shelfPosition1.x, shelfPosition1.y, shelfSize.x, shelfSize.y)
        batch.draw(shelfTexture, shelfPosition2.x, shelfPosition2.y, shelfSize.x, shelfSize.y)

        var positionOffsetX = 0f
        var positionOffsetY = 0f
        var index = 0
        objects.forEach { obj ->
            if (!obj.isCollected) {
                if(index > 0 && index % 8 == 0) {
                    positionOffsetX = 0f
                    positionOffsetY -= 150f * (viewport.worldHeight / 600f)
                }
                obj.positionX = obj.basePositionX * (viewport.worldWidth / 800f) + positionOffsetX
                obj.positionY = obj.basePositionY * (viewport.worldHeight / 600f) + positionOffsetY
                obj.positionOffsetX = positionOffsetX
                obj.positionOffsetY = positionOffsetY
                batch.draw(obj.texture, obj.positionX, obj.positionY, obj.sizeX, obj.sizeY)
            }
            positionOffsetX += 50f * (viewport.worldWidth / 800f)
            index++
        }

        batch.draw(basketTexture, basketPosition.x, basketPosition.y, basketSize.x, basketSize.y)
        batch.draw(listTexture, listPosition.x, listPosition.y, listSize.x, listSize.y)

        renderPhrasesOnScreen(batch, font, listPosition.x + 30f, listSize.y - 40f, 30f)

        batch.draw(pauseTexture, pausePosition.x, pausePosition.y, pauseSize.x, pauseSize.y)
        batch.draw(timeTexture, timePosition.x, timePosition.y, timeSize.x, timeSize.y)

        font.color = Color.BLACK
        font.draw(batch, formatTime(timeLeft), 60f, 568f)

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
            if (mouseX in pausePosition.x..(pausePosition.x + pauseSize.x) && mouseY in pausePosition.y..(pausePosition.y + pauseSize.y)) {
                pauseTexture = Texture(Gdx.files.internal("Minigames/playbutton.png"))
            }
            objects.forEach { obj ->
                if (!isDragging && !obj.isCollected && isMouseInsideImage(mouseX, mouseY, obj)) {
                    isDragging = true
                    obj.isBeingDragged = true
                    offsetX = mouseX - obj.positionX
                    offsetY = mouseY - obj.positionY
                }

                if (obj.isBeingDragged) {
                    obj.basePositionX = (mouseX - offsetX - obj.positionOffsetX) / (viewport.worldWidth / 800f)
                    obj.basePositionY = (mouseY - offsetY - obj.positionOffsetY) / (viewport.worldHeight / 600f)
                }
            }
        } else {
            objects.forEach { obj ->
                if (obj.isBeingDragged) {
                    obj.isBeingDragged = false
                    if (isImageInsideBasket(obj)) {
                        val isCorrect = minigame.phraseList.any { it.id == obj.phrase.id }
                        if(isCorrect) {
                            obj.isCollected = true
                            println("JACKPOT")
                        }
                        if (minigame.capturedPhrases.any { it.key.id != obj.phrase.id }) {
                            minigame.phraseCheck(obj.phrase, isCorrect)
                        }
                    }

                    if (!obj.isCollected) {
                        obj.basePositionX = obj.resetPositionX
                        obj.basePositionY = obj.resetPositionY
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

    //TODO
    fun renderPhrasesOnScreen(batch: SpriteBatch, font: BitmapFont, startX: Float, startY: Float, lineHeight: Float) {
        var currentY = startY
        val glyphLayout = GlyphLayout()

        minigame.phraseList.forEach { phrase ->
            val phraseObject = objects.find { it.phrase == phrase }
            glyphLayout.setText(font, phrase.phrase)
            val textWidth = glyphLayout.width
            val textHeight = glyphLayout.height
            font.draw(batch, phrase.phrase, startX, currentY)

            if (phraseObject!!.isCollected) {
                batch.end()
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                shapeRenderer.color = Color.BLACK
                shapeRenderer.rect(startX - 100f, currentY - (textHeight / 2), textWidth, 2f)
                shapeRenderer.end()
                batch.begin()
            }

            currentY -= lineHeight
        }
    }

    private fun isMouseInsideImage(mouseX: Float, mouseY: Float, obj: DraggableObject): Boolean {
        return mouseX in obj.positionX..(obj.positionX + obj.sizeX) && mouseY in obj.positionY..(obj.positionY + obj.sizeY)
    }

    private fun isImageInsideBasket(obj: DraggableObject): Boolean {
        return obj.positionX + obj.texture.width > basketPosition.x &&
            obj.positionX < basketPosition.x + basketSize.x &&
            obj.positionY + obj.texture.height > basketPosition.y &&
            obj.positionY < basketPosition.y + basketSize.y
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
        var resetPositionX: Float,
        var resetPositionY: Float,
        var basePositionX: Float,
        var basePositionY: Float,
        var positionX: Float,
        var positionY: Float,
        var positionOffsetX: Float,
        var positionOffsetY: Float,
        var sizeX: Float,
        var sizeY: Float,
        var isCollected: Boolean = false,
        var isBeingDragged: Boolean = false
    )
}
