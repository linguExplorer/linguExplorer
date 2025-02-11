package com.github.linguExplorer.screen

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.minigames.KleidungMinigame
import com.github.linguExplorer.models.PhraseEntity
import ktx.app.KtxScreen
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MinigameKleidungScreen(private val game: linguExplorer) : KtxScreen {

    private val batch = SpriteBatch()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(800f, 600f)
    private val shapeRenderer = ShapeRenderer()
    private val executor: ExecutorService = Executors.newFixedThreadPool(1)
    var textgap = 2f

    // Texturen
    private val BagBlueTexture = Texture(Gdx.files.internal("Minigames/Kleidung/bagblue.png"))
    private val PurpleBagTexture = Texture(Gdx.files.internal("Minigames/Kleidung/bagpurple.png"))
    private val timeTexture = Texture(Gdx.files.internal("Minigames/time.png"))
    private var pauseTexture = Texture(Gdx.files.internal("Minigames/pausebutton.png"))
    private var playTexture = Texture(Gdx.files.internal("Minigames/playbutton.png"))
    private val shelfTexture = Texture(Gdx.files.internal("Minigames/shelf.png"))
    private val continueTexture = Texture(Gdx.files.internal("Minigames/btn_continue.png"))
    private val tryAgainButtonTexture = Texture(Gdx.files.internal("Minigames/btn_tryAgain.png"))
    private val quitButtonTexture = Texture(Gdx.files.internal("Minigames/btn_quitMinigame.png"))

    // Positionen und Größen
    private val blueBagBasePosition = Vector2(600f, 0f)
    private val blueBagSize = Vector2(220f, 260f)

    private val purpleBagBasePosition = Vector2(600f, 340f)
    private val purpleBagSize = Vector2(220f, 260f)

    private val pauseBasePosition = Vector2(180f, 530f)
    private val pauseSize = Vector2(50f, 50f)

    private val shelfBasePosition1 = Vector2(0f, 370f)
    private val shelfBasePosition2 = Vector2(-80f, 250f)
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
    private val blueBagPosition: Vector2
        get() = Vector2(
            blueBagBasePosition.x * (viewport.worldWidth / 800f),
            blueBagBasePosition.y * (viewport.worldHeight / 600f)
        )

    private val purpleBagPosition: Vector2
        get() = Vector2(
            purpleBagBasePosition.x * (viewport.worldWidth / 800f),
            purpleBagBasePosition.y * (viewport.worldHeight / 600f)
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

    private val minigame = KleidungMinigame()
    private val objects: List<DraggableObject>

    // Liste der Dateinamen, die nach unten verschoben werden sollen
    private val objectsToMoveDown = listOf("blouse.png", "coat.png", "dress.png", "jacket.png", "jeans.png", "skirt.png", "trousers.png")

    // Positionen unter dem Regal
    private val bottomPositions = mutableListOf<Vector2>()

    init {
        // Initialisierung
        minigame.loadMinigamePhrases()
        minigame.loadAllPhrases()

        // Positionen UNTER dem Regal (Y-Wert angepasst)
        bottomPositions.add(Vector2(50f, 100f))  // Position 1
        bottomPositions.add(Vector2(150f, 100f)) // Position 2
        bottomPositions.add(Vector2(250f, 100f)) // Position 3
        bottomPositions.add(Vector2(350f, 100f)) // Position 4
        bottomPositions.add(Vector2(450f, 100f)) // Position 5

        val horizontalOffset = 100f
        val verticalOffset = 150f

        val phrasesWithAssets = minigame.loadPhrasesWithAssets()

        // Separate Listen für Regal- und Bottom-Objekte
        val bottomObjects = mutableListOf<Pair<PhraseEntity, String>>()
        val shelfObjects = mutableListOf<Pair<PhraseEntity, String>>()

        phrasesWithAssets.forEach { (phrase, assetPath) ->
            if (objectsToMoveDown.any { assetPath.contains(it) }) {
                bottomObjects.add(phrase to assetPath)
            } else {
                shelfObjects.add(phrase to assetPath)
            }
        }

        // Mische die Bottom-Objekte zufällig
        bottomObjects.shuffle()

        // Erstelle die DraggableObjects
        objects = mutableListOf<DraggableObject>()

        // Bottom-Objekte erstellen
        bottomObjects.forEachIndexed { index, (phrase, assetPath) ->
            val texture = Texture(Gdx.files.internal(assetPath))
            val pixmap = Pixmap(Gdx.files.internal(assetPath))
            val originalWidth = pixmap.width.toFloat()
            val originalHeight = pixmap.height.toFloat()
            pixmap.dispose()
            val targetWidth = 50f
            val aspectRatio = originalHeight / originalWidth
            val targetHeight = targetWidth * aspectRatio

            // Position aus der Liste bottomPositions nehmen (index-basiert, nicht entfernen)
            val position = bottomPositions[index % bottomPositions.size] //Modulo Operator
            val resetPositionX = position.x
            val resetPositionY = position.y

            objects.add(
                DraggableObject(
                    phrase = phrase,
                    texture = texture,
                    resetPositionX = resetPositionX,
                    resetPositionY = resetPositionY,
                    basePositionX = resetPositionX,
                    basePositionY = resetPositionY,
                    positionX = 0f,
                    positionY = 0f,
                    positionOffsetX = 0f,
                    positionOffsetY = 0f,
                    sizeX = targetWidth,
                    sizeY = targetHeight
                )
            )
        }

        //Regal-Objekte erstellen
        shelfObjects.forEach { (phrase, assetPath) ->
            val texture = Texture(Gdx.files.internal(assetPath))
            val pixmap = Pixmap(Gdx.files.internal(assetPath))
            val originalWidth = pixmap.width.toFloat()
            val originalHeight = pixmap.height.toFloat()
            pixmap.dispose()
            val targetWidth = 50f
            val aspectRatio = originalHeight / originalWidth
            val targetHeight = targetWidth * aspectRatio

            val resetPositionX = if (assetPath.contains("Shirt") || assetPath.contains("Dress")) shelfBasePosition1.x + horizontalOffset else shelfBasePosition2.x + horizontalOffset
            val resetPositionY = if (assetPath.contains("Shirt") || assetPath.contains("Dress")) shelfBasePosition1.y + verticalOffset else shelfBasePosition2.y + verticalOffset

            objects.add(
                DraggableObject(
                    phrase = phrase,
                    texture = texture,
                    resetPositionX = resetPositionX,
                    resetPositionY = resetPositionY,
                    basePositionX = resetPositionX,
                    basePositionY = resetPositionY,
                    positionX = 0f,
                    positionY = 0f,
                    positionOffsetX = 0f,
                    positionOffsetY = 0f,
                    sizeX = targetWidth,
                    sizeY = targetHeight
                )
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

        if (showErrorText) {
            errorTextTimer += delta
            if (errorTextTimer >= errorTextDuration) {
                showErrorText = false
                errorLine = false
            }
        }

        //Hintergrundfarbe auf #e7d7c7
        Gdx.gl.glClearColor(0.905f, 0.843f, 0.780f, 1f) // RGB-Werte für #e7d7c7
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

        // Fehlertext wird hier gezeichnet
        if (showErrorText) {
            font.color = Color.RED
            font.data.setScale(0.3f, 0.3f)
            val glyphLayout = GlyphLayout()
            glyphLayout.setText(font, "False!")
        }

        batch.draw(PurpleBagTexture, purpleBagPosition.x, purpleBagPosition.y, purpleBagSize.x, purpleBagSize.y)
        batch.draw(BagBlueTexture, blueBagPosition.x, blueBagPosition.y, blueBagSize.x, blueBagSize.y)

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

                if (!obj.isCollected) {
                    batch.draw(obj.texture, obj.positionX, obj.positionY, obj.sizeX, obj.sizeY)
                }
                index++
                positionOffsetX += 50f * (viewport.worldWidth / 800f)

                renderPhrasesOnScreen(batch, font, minigame.bag1, purpleBagPosition.x + 30f, purpleBagPosition.y + purpleBagSize.y - 90f, 30f)
                renderPhrasesOnScreen(batch, font, minigame.bag2, blueBagPosition.x + 30f, blueBagPosition.y + purpleBagSize.y - 90f, 30f)
            }
        }

        if (minigame.isGameComplete()) {
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

            font.color = Color.WHITE
            val glyphLayout = GlyphLayout()
            font.data.setScale(0.4f, 0.4f)
            glyphLayout.setText(font, "Put the items in the correct bags", Color.WHITE, viewport.worldWidth * 0.5f, Align.center, true)
            val gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
            val gamePausedY = (viewport.worldHeight / 2) + glyphLayout.height
            font.draw(batch, "Put the items in the correct bags", gamePausedX, gamePausedY, viewport.worldWidth * 0.5f, Align.center, true)

            // Continue-Button anzeigen
            batch.draw(
                continueTexture,
                continueButtonPosition.x - (buttonSize.x * (continueButtonScale - 1f) / 2),
                continueButtonPosition.y - (buttonSize.y * (continueButtonScale - 1f) / 2),
                buttonSize.x * continueButtonScale,
                buttonSize.y * continueButtonScale
            )
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
            }
        }

        batch.end()
    }

    private fun restartGame() {
        timeLeft = 4
        elapsedTime = 0f

        objects.forEach { obj ->
            obj.isCollected = false
            obj.isBeingDragged = false
            obj.basePositionX = obj.resetPositionX
            obj.basePositionY = obj.resetPositionY
        }

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
                                obj.basePositionX = (mouseX - offsetX - obj.positionOffsetX) / (viewport.worldWidth / 800f)
                                obj.basePositionY = (mouseY - offsetY - obj.positionOffsetY) / (viewport.worldHeight / 600f)
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

                            // Überprüfen, ob das Objekt im Bereich des purpleBag oder blueBag liegt
                            val isInPurpleBag = mouseX in purpleBagPosition.x..(purpleBagPosition.x + purpleBagSize.x) &&
                                mouseY in purpleBagPosition.y..(purpleBagPosition.y + purpleBagSize.y)

                            val isInBlueBag = mouseX in blueBagPosition.x..(blueBagPosition.x + blueBagSize.x) &&
                                mouseY in blueBagPosition.y..(blueBagPosition.y + blueBagSize.y)

                            if (isInPurpleBag || isInBlueBag) {
                                // Überprüfen, ob die Phrase in der richtigen Liste ist
                                val isCorrect = if (isInPurpleBag) {
                                    minigame.bag1.any { it.id == obj.phrase.id }
                                } else {
                                    minigame.bag2.any { it.id == obj.phrase.id }
                                }

                                if (isCorrect) {
                                    // Objekt als eingesammelt markieren und nicht mehr anzeigen
                                    obj.isCollected = true
                                } else {
                                    // Fehlermeldung anzeigen
                                    showErrorText = true
                                    errorTextTimer = 0f
                                    errorLine = true
                                }
                            }

                            if (!obj.isCollected) {
                                // Objekt zurück an die Startposition setzen
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

    fun renderPhrasesOnScreen(batch: SpriteBatch, font: BitmapFont, list: List<PhraseEntity>, startX: Float, startY: Float, lineHeight: Float) {
        var currentY = startY
        val glyphLayout = GlyphLayout()

        list.forEach { phrase ->
            val phraseObject = objects.find { it.phrase == phrase }
            font.data.setScale(0.2f, 0.2f)
            glyphLayout.setText(font, phrase.phrase)
            val textWidth = glyphLayout.width
            val textHeight = glyphLayout.height

            font.data.setScale(0.2f, 0.2f)
            font.draw(batch, phrase.phrase, startX, currentY)

            val currentLineHeight = textHeight * 1.5f
            if (phraseObject!!.isCollected) {
                batch.end()
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                if (errorLine) {
                    shapeRenderer.color = Color.RED
                } else {
                    shapeRenderer.color = Color.BLACK
                }

                shapeRenderer.rect(
                    startX * (viewport.worldWidth/800f) + 320f,
                    (currentY - textHeight / 2 - 1.75f) * (viewport.screenHeight / 600f),
                    textWidth * (viewport.screenWidth / 800f),
                    3.5f * (viewport.screenHeight / 600f)
                )
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
        return obj.positionX + obj.texture.width > blueBagPosition.x &&
            obj.positionX < blueBagPosition.x + blueBagSize.x &&
            obj.positionY + obj.texture.height > blueBagPosition.y &&
            obj.positionY < blueBagPosition.y + blueBagSize.y
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
        BagBlueTexture.dispose()
        PurpleBagTexture.dispose()
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
