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
import com.badlogic.gdx.utils.Align
import com.github.linguExplorer.minigames.EssenMinigame
import com.github.linguExplorer.models.PhraseEntity

class GameScreen : Screen {

    private val batch = SpriteBatch()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(800f, 600f)
    private val shapeRenderer = ShapeRenderer()
    var textgap = 2f

    // Texturen
    private val basketTexture = Texture(Gdx.files.internal("Minigames/basket.png"))
    private val listTexture = Texture(Gdx.files.internal("Minigames/list.png"))
    private val timeTexture = Texture(Gdx.files.internal("Minigames/time.png"))
    private var pauseTexture = Texture(Gdx.files.internal("Minigames/pausebutton.png"))
    private var playTexture = Texture(Gdx.files.internal("Minigames/playbutton.png"))
    private val shelfTexture = Texture(Gdx.files.internal("Minigames/shelf.png"))
    private val continueTexture = Texture(Gdx.files.internal("Minigames/btn_continue.png"))
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
    private val continueButtonBasePosition = Vector2(0f, 125f)
    private val buttonSize = Vector2(250f, 220f)

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

    private val continueButtonPosition: Vector2
        get() = Vector2(
            (viewport.worldWidth / 2) - (buttonSize.x / 2),
            continueButtonBasePosition.y * (viewport.worldHeight / 600f)
        )


    // Zeit
    private var timeLeft = 30
    private var elapsedTime = 0f

    // Spielstatus
    private var isDragging = false
    private var offsetX = 0f
    private var offsetY = 0f
    private var gameStarted = false
    private var gameEnded = false
    private var isCompleted = false

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

    private var isPaused = false

    override fun render(delta: Float) {
        handleInput()
        if (!isPaused && !gameEnded && gameStarted) {
            updateTime(delta)
        }

        Gdx.gl.glClearColor(0.611f, 0.761f, 0.827f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        font.color = Color.BLACK

        batch.begin()

        // Zeichne Regale und andere Spielfunktionen
        batch.draw(shelfTexture, shelfPosition1.x, shelfPosition1.y, shelfSize.x, shelfSize.y)
        batch.draw(shelfTexture, shelfPosition2.x, shelfPosition2.y, shelfSize.x, shelfSize.y)
        batch.draw(listTexture, listPosition.x, listPosition.y, listSize.x, listSize.y)

        var positionOffsetX = 0f
        var positionOffsetY = 0f
        var index = 0

        if (gameStarted) {
            objects.forEach { obj ->
                if (index > 0 && index % 8 == 0) {
                    positionOffsetX = 0f
                    positionOffsetY -= 150f * (viewport.worldHeight / 600f)
                }
                if (!obj.isCollected) {
                    obj.positionX = obj.basePositionX * (viewport.worldWidth / 800f) + positionOffsetX
                    obj.positionY = obj.basePositionY * (viewport.worldHeight / 600f) + positionOffsetY
                    obj.positionOffsetX = positionOffsetX
                    obj.positionOffsetY = positionOffsetY
                    batch.draw(obj.texture, obj.positionX, obj.positionY, obj.sizeX, obj.sizeY)

                }
                index++
                positionOffsetX += 50f * (viewport.worldWidth / 800f)
            }

            renderPhrasesOnScreen(batch, font, listPosition.x + 30f, listSize.y - 40f, 30f)
        }

        batch.draw(basketTexture, basketPosition.x, basketPosition.y, basketSize.x, basketSize.y)

        if(minigame.isGameComplete()) {
            gameEnded = true
            isCompleted = true
        }

        // Pause- oder Play-Button anzeigen
        if (isPaused || gameEnded) {
            batch.draw(playTexture, pausePosition.x, pausePosition.y, pauseSize.x, pauseSize.y)
        } else {
            batch.draw(pauseTexture, pausePosition.x, pausePosition.y, pauseSize.x, pauseSize.y)
        }

        // Zeit
        batch.draw(timeTexture, timePosition.x, timePosition.y, timeSize.x, timeSize.y)
        font.data.setScale(0.3f, 0.3f)
        font.draw(batch, formatTime(timeLeft), timePosition.x + 20f, timePosition.y + timeSize.y / 1.4f)

        if (isPaused) {
            batch.end()
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.65f)
            shapeRenderer.rect(0f, 0f, viewport.worldWidth, viewport.worldHeight)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
            batch.begin()

            font = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
            font.color = Color.WHITE
            val glyphLayout = GlyphLayout()
            font.data.setScale(0.7f, 0.7f)
            glyphLayout.setText(font, "GAME PAUSED")
            val gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
            val gamePausedY = (viewport.worldHeight / 2) + glyphLayout.height + 10f
            font.draw(batch, "GAME PAUSED", gamePausedX, gamePausedY)

            // Continue-Button anzeigen
            batch.draw(continueTexture, continueButtonPosition.x, continueButtonPosition.y, buttonSize.x, buttonSize.y)
        }

        if (!gameStarted) {
            batch.end()
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.65f)
            shapeRenderer.rect(0f, 0f, viewport.worldWidth, viewport.worldHeight)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
            batch.begin()

            //TODO der text ist soooo knapp nicht in der mitte :((
            font.color = Color.WHITE
            val glyphLayout = GlyphLayout()
            font.data.setScale(0.4f, 0.4f)
            glyphLayout.setText(font, "Put the items on the list in the basket", Color.WHITE, viewport.worldWidth * 0.75f, Align.center, true)
            val gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
            val gamePausedY = (viewport.worldHeight / 2) + glyphLayout.height
            font.draw(batch, "Put the items on the list in the basket", gamePausedX, gamePausedY, viewport.worldWidth * 0.75f, Align.center, true)

            // Continue-Button anzeigen
            batch.draw(continueTexture, continueButtonPosition.x, continueButtonPosition.y, buttonSize.x, buttonSize.y)
        }

        if (gameEnded) {
            batch.end()
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.5f)
            shapeRenderer.rect(0f, 0f, viewport.worldWidth, viewport.worldHeight)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
            batch.begin()

            font.color = Color.WHITE
            val glyphLayout = GlyphLayout()
            font = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
            font.data.setScale(0.7f, 0.7f)

            if (isCompleted) {
                glyphLayout.setText(font, "CONGRATULATIONS")
                val gameOverX = (viewport.worldWidth - glyphLayout.width) / 2
                val gameOverY = (viewport.worldHeight / 2) + glyphLayout.height + 10f
                font.draw(batch, "CONGRATULATIONS", gameOverX, gameOverY)
            } else {
                glyphLayout.setText(font, "GAME OVER")
                val gameOverX = (viewport.worldWidth - glyphLayout.width) / 2
                val gameOverY = (viewport.worldHeight) / 2 + glyphLayout.height + 10f
                font.draw(batch, "GAME OVER", gameOverX, gameOverY)

                /*val extraSpacing = 120f // Zusätzlicher Abstand zwischen "GAME OVER" und "Try Again"
                val buttonYSpacing = -70f // Abstand zwischen "Try Again" und "Quit"
                val tryAgainButtonY = gameOverY - glyphLayout.height - extraSpacing
                val quitButtonY = tryAgainButtonY - buttonSize.y - buttonYSpacing
                val buttonX = (viewport.worldWidth - buttonSize.x) / 2
                batch.draw(tryAgainButtonTexture, buttonX, tryAgainButtonY, buttonSize.x, buttonSize.y)
                batch.draw(quitButtonTexture, buttonX, quitButtonY, buttonSize.x, buttonSize.y)*/
            }

            batch.draw(continueTexture, continueButtonPosition.x, continueButtonPosition.y, buttonSize.x, buttonSize.y)
        }

        batch.end()
    }

    private fun restartGame() {
        // Zurücksetzen der Zeit
        timeLeft = 4
        elapsedTime = 0f

        // Zurücksetzen des Spielfortschritts
        objects.forEach { obj ->
            obj.isCollected = false
            obj.isBeingDragged = false
            obj.basePositionX = obj.resetPositionX
            obj.basePositionY = obj.resetPositionY
        }

        // Zurücksetzen des Spielstatus
        gameEnded = false
        isPaused = false
    }

    private fun handleInput() {
        val mouseX = Gdx.input.x.toFloat() * viewport.worldWidth / Gdx.graphics.width
        val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.worldHeight / Gdx.graphics.height

        if (!gameEnded && gameStarted) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (!isPaused) {
                    if (mouseX in pausePosition.x..(pausePosition.x + pauseSize.x) && mouseY in pausePosition.y..(pausePosition.y + pauseSize.y)
                        && objects.find { it.isBeingDragged } == null
                    ) {
                        // Pause/Play umschalten
                        isPaused = !isPaused
                        return
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
                    if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) && mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)) {
                        isPaused = !isPaused
                    }
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                isPaused = true
            } else {
                if (!isPaused) {
                    objects.forEach { obj ->
                        if (obj.isBeingDragged) {
                            obj.isBeingDragged = false
                            if (isImageInsideBasket(obj)) {
                                val isCorrect = minigame.phraseList.any { it.id == obj.phrase.id }
                                if (isCorrect) {
                                    obj.isCollected = true
                                }
                                minigame.phraseCheck(obj.phrase, isCorrect)
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
        } else if (!gameStarted) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) && mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)) {
                    gameStarted = true
                }
            }
        } else {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) && mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)) {
                    minigame.storePhraseData()
                    Gdx.app.exit()
                }
            }
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
        val lineOffset = 5f //--- neu Linie näher oder weiter vom Text zu positionieren

        // geht durch Liste der Phrasen
        minigame.phraseList.forEach { phrase ->
            //Object das zur aktuellen Phrase gehört
            val phraseObject = objects.find { it.phrase == phrase }
            //Breite des Textes der Phrase berechnen
            font.data.setScale(0.2f, 0.2f)
            glyphLayout.setText(font, phrase.phrase)
            val textWidth = glyphLayout.width
            val textHeight = glyphLayout.height
            font.data.setScale(0.2f, 0.2f)
            font.draw(batch, phrase.phrase, startX, currentY)

            //ob Object schon eingesammelt wurde
            if (phraseObject!!.isCollected) {
                batch.end()
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                shapeRenderer.color = Color.BLACK
                // x-Position des Textes , y-Position des Textes, Breite vom Text, Dicke der Linie
                shapeRenderer.rect(startX - 228f, currentY - (textHeight / 2) - 43f, textWidth, 3.5f)
                /*shapeRenderer.rect(
                    startX - 228f,
                    currentY - textHeight/2 - 43f, // 2 - textgap, //-43f damit erste linie weiter unten!
                    textWidth,
                    3.5f
                )*/
                shapeRenderer.end()
                batch.begin()
            }
            //y-Position für nächste Phrase um lineHeight verringern
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
