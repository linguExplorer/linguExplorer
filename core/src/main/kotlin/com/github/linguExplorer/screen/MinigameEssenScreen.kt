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
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.github.linguExplorer.event.GameEndEvent
import com.github.linguExplorer.event.fire
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.minigames.EssenMinigame
import com.github.linguExplorer.models.PhraseEntity
import ktx.app.KtxScreen
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MinigameEssenScreen(private val game: linguExplorer,
private val stage: Stage

) : KtxScreen {

    private val batch = SpriteBatch()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(800f, 600f)
    private val shapeRenderer = ShapeRenderer()
    private val executor: ExecutorService = Executors.newFixedThreadPool(1)

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

    private val tryAgainButtonBasePosition = Vector2(430f, 200f)
    private val quitButtonBasePosition = Vector2(430f, 130f)
    private val continueButtonBasePosition = Vector2(0f, 175f)
    private val buttonSize = Vector2(250f, 70f)

    private val timeBasePosition = Vector2(20f, 530f)
    private val timeSize = Vector2(150f, 50f)

    //Error Text
    private var showErrorText = false
    private var errorTextTimer = 0f
    private val errorTextDuration = 2f // Dauer
    private var errorTextPositionX = 0f
    private var errorTextPositionY = 0f

    // roter Strich
    private var errorLine = false

    //Positionen der Objekte im Korb
    private val collectedObjectPositions = mutableListOf<Vector2>()
    private val collectedObjectSpacing = 55f // Abstand zwischen den Objekten im Korb
    private var currentBasketRow = 0

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


    private var continueButtonScale = 1f
    private var pauseButtonScale = 1f
    private var continueButtonTargetScale = 1f
    private var pauseButtonTargetScale = 1f

    private val scaleSpeed = 5f


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

        if(showErrorText){
            errorTextTimer += delta
            if(errorTextTimer >= errorTextDuration){
                showErrorText = false
                errorLine = false
            }
        }

        Gdx.gl.glClearColor(0.611f, 0.761f, 0.827f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        font.color = Color.BLACK

        continueButtonScale += (continueButtonTargetScale - continueButtonScale) * scaleSpeed * delta
        pauseButtonScale += (pauseButtonTargetScale - pauseButtonScale) * scaleSpeed * delta


        batch.begin()

        // Zeichne Regale und andere Spielfunktionen
        batch.draw(shelfTexture, shelfPosition1.x, shelfPosition1.y, shelfSize.x, shelfSize.y)
        batch.draw(shelfTexture, shelfPosition2.x, shelfPosition2.y, shelfSize.x, shelfSize.y)
        batch.draw(listTexture, listPosition.x, listPosition.y, listSize.x, listSize.y)


        // Fehlertext wird hier gezeichnet
        if (showErrorText) {
            font.color = Color.RED
            font.data.setScale(0.3f, 0.3f)
            val glyphLayout = GlyphLayout()
            glyphLayout.setText(font, "False!")
            //font.draw(batch, "False!", errorTextPositionX, errorTextPositionY)
        }


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
                }

                batch.draw(obj.texture, obj.positionX, obj.positionY, obj.sizeX, obj.sizeY)
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
        val texture: Texture = if (isPaused || gameEnded) playTexture else pauseTexture
        pauseButtonScale = if (isPaused || gameEnded) 1f else pauseButtonScale

        batch.draw(
            texture,
            pausePosition.x - (pauseSize.x * (pauseButtonScale - 1f) / 2),
            pausePosition.y - (pauseSize.y * (pauseButtonScale - 1f) / 2),
            pauseSize.x * pauseButtonScale,
            pauseSize.y * pauseButtonScale
        )

        // Zeit
        batch.draw(timeTexture, timePosition.x, timePosition.y, timeSize.x, timeSize.y)
        font.data.setScale(0.3f, 0.3f)
        font.draw(batch, formatTime(timeLeft), timePosition.x + 20f, timePosition.y + timeSize.y / 1.4f)

        if (isPaused) {
            batch.end()
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.65f)
            shapeRenderer.rect(0f, 0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
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
            batch.draw(
                continueTexture,
                continueButtonPosition.x - (buttonSize.x * (continueButtonScale - 1f) / 2),
                continueButtonPosition.y - (buttonSize.y * (continueButtonScale - 1f) / 2),
                buttonSize.x * continueButtonScale,
                buttonSize.y * continueButtonScale
            )
        }

        if (!gameStarted) {
            batch.end()
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.65f)
            shapeRenderer.rect(0f, 0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
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
            batch.draw(
                continueTexture,
                continueButtonPosition.x - (buttonSize.x * (continueButtonScale - 1f) / 2),
                continueButtonPosition.y - (buttonSize.y * (continueButtonScale - 1f) / 2),
                buttonSize.x * continueButtonScale,
                buttonSize.y * continueButtonScale
            )

            //batch.draw(continueTexture, continueButtonPosition.x, continueButtonPosition.y, buttonSize.x, buttonSize.y)
        }

        if (gameEnded) {
            batch.end()
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.5f)
            shapeRenderer.rect(0f, 0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
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
                batch.draw(
                    continueTexture,
                    continueButtonPosition.x - (buttonSize.x * (continueButtonScale - 1f) / 2),
                    continueButtonPosition.y - (buttonSize.y * (continueButtonScale - 1f) / 2),
                    buttonSize.x * continueButtonScale,
                    buttonSize.y * continueButtonScale
                )
            } else {
                glyphLayout.setText(font, "GAME OVER")
                val gameOverX = (viewport.worldWidth - glyphLayout.width) / 2
                val gameOverY = (viewport.worldHeight) / 2 + glyphLayout.height + 10f
                font.draw(batch, "GAME OVER", gameOverX, gameOverY)
                batch.draw(
                    quitButtonTexture,
                    continueButtonPosition.x - (buttonSize.x * (continueButtonScale - 1f) / 2),
                    continueButtonPosition.y - (buttonSize.y * (continueButtonScale - 1f) / 2),
                    buttonSize.x * continueButtonScale,
                    buttonSize.y * continueButtonScale
                )

                /*val extraSpacing = 120f // Zusätzlicher Abstand zwischen "GAME OVER" und "Try Again"
                val buttonYSpacing = -70f // Abstand zwischen "Try Again" und "Quit"
                val tryAgainButtonY = gameOverY - glyphLayout.height - extraSpacing
                val quitButtonY = tryAgainButtonY - buttonSize.y - buttonYSpacing
                val buttonX = (viewport.worldWidth - buttonSize.x) / 2
                batch.draw(tryAgainButtonTexture, buttonX, tryAgainButtonY, buttonSize.x, buttonSize.y)
                batch.draw(quitButtonTexture, buttonX, quitButtonY, buttonSize.x, buttonSize.y)*/
            }
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
        collectedObjectPositions.clear()
        currentBasketRow = 0
    }

    private fun handleInput() {
        val mouseX = Gdx.input.x.toFloat() * viewport.worldWidth / Gdx.graphics.width
        val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.worldHeight / Gdx.graphics.height

        continueButtonTargetScale = if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
            mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)) {
            1.1f
        } else {
            1f
        }


        if (!gameEnded && gameStarted) {
            pauseButtonTargetScale = if (mouseX in pausePosition.x..(pausePosition.x + pauseSize.x) &&
                mouseY in pausePosition.y..(pausePosition.y + pauseSize.y)) {
                1.1f
            } else {
                1f
            }

            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (!isPaused) {
                    if (mouseX in pausePosition.x..(pausePosition.x + pauseSize.x) && mouseY in pausePosition.y..(pausePosition.y + pauseSize.y)
                        && objects.find { it.isBeingDragged } == null
                    ) {
                        // Pause/Play umschalten
                        isPaused = !isPaused
                        return
                    }

                    if (!showErrorText) {

                        objects.forEach { obj ->
                            if (!isDragging && !obj.isCollected && isMouseInsideImage(mouseX, mouseY, obj)) {
                                isDragging = true
                                obj.isBeingDragged = true
                                offsetX = mouseX - obj.positionX
                                offsetY = mouseY - obj.positionY
                            }

                            if (obj.isBeingDragged) {
                                obj.basePositionX =
                                    (mouseX - offsetX - obj.positionOffsetX) / (viewport.worldWidth / 800f)
                                obj.basePositionY =
                                    (mouseY - offsetY - obj.positionOffsetY) / (viewport.worldHeight / 600f)
                            }
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
                                //ob das Objekt in der Liste
                                val isCorrect = minigame.phraseList.any { it.id == obj.phrase.id }
                                if (isCorrect) {
                                    //Objekt als eingesammelt markieren
                                    obj.isCollected = true
                                    val initialXOffset = 50f //weiter rechts zeichnen
                                    // Position des Objekts im Korb berechnen
                                    // Startposition Korb + Abstand Rand + Position in Reihe % 5 * Abstand zwischen Objekten
                                    val basketX = basketPosition.x + initialXOffset + (collectedObjectPositions.size % 5) * collectedObjectSpacing
                                    // Startposition Korbs + Abstand + Reihennummer * Abstand zwischen Objekten
                                    val basketY = basketPosition.y + 10f + (currentBasketRow * collectedObjectSpacing)

                                    obj.positionX = basketX
                                    obj.positionY = basketY

                                    // Position speichern
                                    collectedObjectPositions.add(Vector2(basketX, basketY))
                                    // neue Reihe? weil mehr als 5
                                    if(collectedObjectPositions.size % 5 == 0)
                                        currentBasketRow++
                                } else {
                                    // Text mit "Fehler!" anzeigen
                                    showErrorText = true
                                    errorTextTimer = 0f

                                    //Fehlerzustand für Linie
                                    errorLine = true

                                    val glyphLayout = GlyphLayout()
                                    font.data.setScale(0.3f, 0.3f)
                                    glyphLayout.setText(font, "Fehler!")

                                    errorTextPositionX = (viewport.worldWidth - glyphLayout.width) / 2
                                    errorTextPositionY = viewport.worldHeight / 2
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
                    storePhraseDataAsync()

                    stage.fire(GameEndEvent("SM"))


                    if (game!!.containsScreen<MapScreen>()) {
                        game.removeScreen<MapScreen>()
                    }
                    game.addScreen(MapScreen(game))
                    game.setScreen<MapScreen>()
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
            // HIER wird der Text gezeichnet
            font.draw(batch, phrase.phrase, startX, currentY)

            val currentLineHeight = textHeight * 1.5f
            //ob Object schon eingesammelt wurde
            if (phraseObject!!.isCollected) {
                batch.end()
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                if(errorLine){
                    shapeRenderer.color = Color.RED
                } else {
                    shapeRenderer.color = Color.BLACK
                }

                shapeRenderer.rect(listBasePosition.x * (viewport.screenWidth / 800f) + 30f,
                    (currentY - textHeight/2 - 1.75f) * (viewport.screenHeight / 600f),
                    textWidth * (viewport.screenWidth / 800f),
                    3.5f * (viewport.screenHeight / 600f))
                shapeRenderer.end()
                batch.begin()
            }
            currentY -= currentLineHeight
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

    private fun storePhraseDataAsync() {
        executor.submit {
            minigame.storePhraseData()
        }
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
