package com.github.linguExplorer.screen

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
import com.github.linguExplorer.masterVolume
import com.github.linguExplorer.minigames.EssenMinigame
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.musicVolume
import com.github.linguExplorer.soundEffectVolume
import ktx.app.KtxScreen
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MinigameEssenScreen(private val game: linguExplorer,
                          private val stage: Stage

) : KtxScreen {

    private val batch = SpriteBatch()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(1920f, 1080f)
    private val shapeRenderer = ShapeRenderer()
    private val glyphLayout = GlyphLayout()
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
    private val basketPosition = Vector2(150f, 0f)
    private val basketSize = Vector2(650f, 500f)

    private val listPosition: Vector2
        get() = Vector2(viewport.worldWidth - 800f, 0f)
    private val listSize = Vector2(450f, 420f)

    private val pausePosition: Vector2
        get() = Vector2(310f, viewport.worldHeight - 120f)
    private val pauseSize = Vector2(80f, 80f)

    private val shelfPosition1: Vector2
        get() = Vector2(viewport.worldWidth - 1200f, 800f)
    private val shelfPosition2: Vector2
        get() = Vector2(viewport.worldWidth - 1200f, 540f)
    private val shelfSize = Vector2(1250f, 40f)

    private val objectBasePositionX: Float
        get() = viewport.worldWidth - 1150f

    private val tryAgainButtonBasePosition = Vector2(430f, 200f)
    private val quitButtonBasePosition = Vector2(430f, 130f)
    private val continueButtonPosition: Vector2
        get() = Vector2(
            (viewport.worldWidth / 2) - (buttonSize.x / 2),
            (viewport.worldHeight - buttonSize.y) / 2 - 50f
        )
    private val buttonSize = Vector2(375f, 105f)

    private val timePosition: Vector2
        get() = Vector2(30f, viewport.worldHeight - 125f)
    private val timeSize = Vector2(260f, 90f)

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

    private val tryAgainButtonPosition: Vector2
        get() = Vector2(
            tryAgainButtonBasePosition.x * (viewport.worldWidth / 800f),
            tryAgainButtonBasePosition.y * (viewport.worldHeight / 600f)
        )

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

    private var loadingScreenRenderer = LoadingScreenRenderer()
    private var threadExecuted = false

    // Transition properties
    private var isTransitioning = false
    private var transitionRadius = 0f
    private val maxRadius = 2500f
    private var loadingTime = 0f
    private var threadWorking = false

    private var backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Hintergrundmusik/minigame_music.mp3"))
    private var correctSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Soundeffekte/correct.mp3"))
    private var wrongSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Soundeffekte/wrong.mp3"))
    private var gameEndSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Soundeffekte/game_end.mp3"))



    private val minigame = EssenMinigame()
    private lateinit var objects: List<DraggableObject>

    override fun show() {
        loadPhraseData()
        Gdx.input.inputProcessor = null

        backgroundMusic.isLooping = true
        backgroundMusic.volume = musicVolume * masterVolume
        backgroundMusic.play()
    }

    private var isPaused = false

    override fun render(delta: Float) {
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))

        // Handle transition to MapScreen
        if (isTransitioning) {
            transitionRadius += 1500 * delta
            if (transitionRadius >= maxRadius) {
                loadingTime += delta

                if (!threadWorking) {
                    storePhraseDataAsync()
                    threadWorking = true
                }

                loadingScreenRenderer.renderAnimatedText(batch, font, glyphLayout, viewport, "Loading", delta, 1f, true)

                if (threadExecuted && loadingTime > 2f) {
                    Gdx.app.postRunnable {
                        isTransitioning = false
                        transitionRadius = 0f
                        loadingTime = 0f

                        if (game.containsScreen<MapScreen>()) {
                            game.removeScreen<MapScreen>()
                        }
                        game.addScreen(MapScreen(game, 31.104187f, 15.677063f))
                        game.setScreen<MapScreen>()
                    }
                }
                return
            }
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color.BLACK
            shapeRenderer.circle(viewport.worldWidth / 2, viewport.worldHeight / 2, transitionRadius)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
        }

        if (!threadExecuted) {
            loadingScreenRenderer.renderAnimatedText(batch, font, glyphLayout, viewport, "Loading", delta, 1f, true)
        } else {
            handleInput()
            if (!isPaused && !gameEnded && gameStarted) {
                updateTime(delta)
            }

            if (showErrorText) {
                errorTextTimer += delta
                if (errorTextTimer >= errorTextDuration) {
                    showErrorText = false
                    errorLine = false
                }
            }

            Gdx.gl.glClearColor(0.611f, 0.761f, 0.827f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

            viewport.apply()
            batch.projectionMatrix = viewport.camera.combined
            shapeRenderer.projectionMatrix = viewport.camera.combined
            font.color = Color.BLACK



            batch.begin()

            // Zeichne Regale und andere Spielfunktionen
            batch.draw(shelfTexture, shelfPosition1.x, shelfPosition1.y, shelfSize.x, shelfSize.y)
            batch.draw(shelfTexture, shelfPosition2.x, shelfPosition2.y, shelfSize.x, shelfSize.y)
            batch.draw(listTexture, listPosition.x, listPosition.y, listSize.x, listSize.y)


            // Fehlertext wird hier gezeichnet
            if (showErrorText) {
                font.color = Color.RED
                font.data.setScale(0.3f, 0.3f)
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
                        positionOffsetY -= 260f
                    }
                    if (!obj.isCollected) {
                        obj.positionX = obj.basePositionX + positionOffsetX
                        obj.positionY = obj.basePositionY + positionOffsetY
                        obj.positionOffsetX = positionOffsetX
                        obj.positionOffsetY = positionOffsetY
                    }

                    batch.draw(obj.texture, obj.positionX, obj.positionY, obj.sizeX, obj.sizeY)
                    index++
                    positionOffsetX += 140f
                }


                renderPhrasesOnScreen(batch, font, listPosition.x + 45f, listSize.y - 70f, 30f)
            }

            batch.draw(basketTexture, basketPosition.x, basketPosition.y, basketSize.x, basketSize.y)

            if (minigame.isGameComplete()) {
                gameEnded = true
                isCompleted = true
            }

            // Pause- oder Play-Button anzeigen
            val texture: Texture = if (isPaused || gameEnded) playTexture else pauseTexture

            batch.draw(
                texture,
                pausePosition.x ,
                pausePosition.y,
                pauseSize.x, pauseSize.y)

            // Zeit
            batch.draw(timeTexture, timePosition.x, timePosition.y, timeSize.x, timeSize.y)
            font.data.setScale(0.5f, 0.5f)
            font.draw(batch, formatTime(timeLeft), timePosition.x + 42.5f, timePosition.y + 62.5f)

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
                font.data.setScale(1f, 1f)
                glyphLayout.setText(font, "GAME PAUSED")
                val gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
                val gamePausedY = (viewport.worldHeight / 2) + glyphLayout.height + 30f
                font.draw(batch, "GAME PAUSED", gamePausedX, gamePausedY)

                // Continue-Button anzeigen
                batch.draw(
                    continueTexture,
                    continueButtonPosition.x,
                    continueButtonPosition.y - (buttonSize.y / 2) + 15f,
                    buttonSize.x,
                    buttonSize.y
                )
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
                font.data.setScale(0.45f, 0.45f)

                val text = "Put the items on the list in the basket"
                font.draw(
                    batch,
                    text,
                    0f,
                    viewport.worldHeight / 2 + glyphLayout.height / 2 + 80f,
                    viewport.worldWidth,
                    Align.center,
                    true
                )

                // Continue-Button anzeigen
                batch.draw(
                    continueTexture,
                    continueButtonPosition.x,
                    continueButtonPosition.y - (buttonSize.y / 2) + 15f,
                    buttonSize.x,
                    buttonSize.y
                )

                //batch.draw(continueTexture, continueButtonPosition.x, continueButtonPosition.y, buttonSize.x, buttonSize.y)
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
                font.data.setScale(1f, 1f)

                if (isCompleted) {
                    glyphLayout.setText(font, "CONGRATULATIONS")
                    val gameOverX = (viewport.worldWidth - glyphLayout.width) / 2
                    val gameOverY = (viewport.worldHeight / 2) + glyphLayout.height + 30f
                    font.draw(batch, "CONGRATULATIONS", gameOverX, gameOverY)
                    batch.draw(
                        continueTexture,
                        continueButtonPosition.x,
                        continueButtonPosition.y - (buttonSize.y / 2) + 15f,
                        buttonSize.x,
                        buttonSize.y
                    )
                } else {
                    glyphLayout.setText(font, "GAME OVER")
                    val gameOverX = (viewport.worldWidth - glyphLayout.width) / 2
                    val gameOverY = (viewport.worldHeight) / 2 + glyphLayout.height + 30f
                    font.draw(batch, "GAME OVER", gameOverX, gameOverY)
                    batch.draw(
                        quitButtonTexture,
                        continueButtonPosition.x,
                        continueButtonPosition.y - (buttonSize.y / 2) + 15f,
                        buttonSize.x,
                        buttonSize.y
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
                                    (mouseX - offsetX - obj.positionOffsetX)
                                obj.basePositionY =
                                    (mouseY - offsetY - obj.positionOffsetY)
                            }
                        }
                    }
                } else {
                    // Update the button hit area to match how it's drawn
                    val buttonY = continueButtonPosition.y - (buttonSize.y / 2) + 15f
                    if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
                        mouseY in buttonY..(buttonY + buttonSize.y)) {
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
                                    correctSound.play(0.7f * masterVolume * soundEffectVolume)
                                    val initialXOffset = 80f //weiter rechts zeichnen
                                    // Position des Objekts im Korb berechnen
                                    // Startposition Korb + Abstand Rand + Position in Reihe % 5 * Abstand zwischen Objekten
                                    val basketX = basketPosition.x + initialXOffset + (collectedObjectPositions.size % 5) * collectedObjectSpacing + 20f
                                    // Startposition Korbs + Abstand + Reihennummer * Abstand zwischen Objekten
                                    val basketY = basketPosition.y + 20f + (currentBasketRow * collectedObjectSpacing)

                                    obj.positionX = basketX
                                    obj.positionY = basketY

                                    // Position speichern
                                    collectedObjectPositions.add(Vector2(basketX, basketY))
                                    // neue Reihe? weil mehr als 5
                                    if(collectedObjectPositions.size % 5 == 0)
                                        currentBasketRow++
                                } else {
                                    // Text mit "Fehler!" anzeigen
                                    wrongSound.play(0.7f * masterVolume * soundEffectVolume)
                                    
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
                // Update the button hit area to match how it's drawn
                val buttonY = continueButtonPosition.y - (buttonSize.y / 2) + 15f
                if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
                    mouseY in buttonY..(buttonY + buttonSize.y)) {
                    gameStarted = true
                }
            }
        } else {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                // Update the button hit area to match how it's drawn
                val buttonY = continueButtonPosition.y - (buttonSize.y / 2) + 15f
                if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
                    mouseY in buttonY..(buttonY + buttonSize.y)) {
                    // Begin transition to MapScreen with loading display
                    isTransitioning = true
                    loadingTime = 0f

                    // Start data storage in background thread
                    storePhraseDataAsync()
                    stage.fire(GameEndEvent("SM"))
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
            font.data.setScale(0.33f, 0.33f)
            glyphLayout.setText(font, phrase.phrase)
            val textWidth = glyphLayout.width
            val textHeight = glyphLayout.height

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

                shapeRenderer.rect(startX - 15f,
                    (currentY - (textHeight / 2) + 2f),
                    textWidth + 30f,
                    6f)
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

    private fun loadPhraseData() {
        threadExecuted = false
        executor.submit {
            minigame.loadMinigamePhrases()
            minigame.phraseList.forEach { println(it.phrase) }
            minigame.loadAllPhrases()
            Gdx.app.postRunnable {
                objects = minigame.loadPhrasesWithAssets().map { (phrase, assetPath) ->
                    DraggableObject(
                        phrase = phrase,
                        texture = Texture(Gdx.files.internal(assetPath)),
                        resetPositionX = objectBasePositionX,
                        resetPositionY = 850f,
                        basePositionX = objectBasePositionX,
                        basePositionY = 850f,
                        positionX = 0f,
                        positionY = 0f,
                        positionOffsetX = 0f,
                        positionOffsetY = 0f,
                        sizeX = 90f,
                        sizeY = 90f
                    )
                }
            }
            threadExecuted = true
        }
    }

    private fun storePhraseDataAsync() {
        executor.submit {
            minigame.storePhraseData()
        }
    }

    private fun recalculatePositions() {
        objects.forEach { obj ->
            if (!obj.isCollected && !obj.isBeingDragged) {
                obj.resetPositionX = objectBasePositionX
                obj.basePositionX = objectBasePositionX
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)

        if (::objects.isInitialized) {
            recalculatePositions()
        }
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
        if (::objects.isInitialized) {
            objects.forEach { it.texture.dispose() }
        }
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
