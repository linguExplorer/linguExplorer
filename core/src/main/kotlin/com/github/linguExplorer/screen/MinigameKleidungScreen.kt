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
import java.sql.DriverManager.println
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MinigameKleidungScreen(private val game: linguExplorer) : KtxScreen {

    private val batch = SpriteBatch()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(1920f, 1080f)
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
    private val blueBagPosition = Vector2(1520f, 0f)
    private val blueBagSize = Vector2(400f, 500f)

    private val purpleBagPosition = Vector2(1520f, 580f)
    private val purpleBagSize = Vector2(400f, 500f)

    private val pausePosition: Vector2
        get() = Vector2(310f, viewport.worldHeight - 120f)
    private val pauseSize = Vector2(80f, 80f)


    private val shelfPosition1: Vector2
        get() = Vector2(0f, 700f)
    private val shelfPosition2: Vector2
        get() = Vector2(-150f, 450f)
    private val shelfSize = Vector2(1000f, 40f)

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

    // roter Strich
    private var errorLine = false

    //Positionen der Objekte im Korb
    private val collectedObjectPositions = mutableListOf<Vector2>()
    private val collectedObjectSpacing = 55f // Abstand zwischen den Objekten im Korb
    private var currentBasketRow = 0

    // Getter für die dynamischen Positionen




    private var continueButtonScale = 1f
    private var pauseButtonScale = 1f
    private var continueButtonTargetScale = 1f
    private var pauseButtonTargetScale = 1f

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
        bottomPositions.add(Vector2(50f, 50f))   // Position 1
        bottomPositions.add(Vector2(100f, 50f))  // Position 2
        bottomPositions.add(Vector2(150f, 50f))  // Position 3
        bottomPositions.add(Vector2(200f, 50f))  // Position 4
        bottomPositions.add(Vector2(250f, 50f))  // Position 5
        bottomPositions.add(Vector2(260f, 200f))   // Position 6
        bottomPositions.add(Vector2(260f, 200f))  // Position 7
        bottomPositions.add(Vector2(260f, 200f))  // Position 8
        bottomPositions.add(Vector2(260f, 200f))  // Position 9
        bottomPositions.add(Vector2(260f, 200f))  // Position 10

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
        val bottomTargetWidth = 180f // Größere Breite für Bottom-Objekte

        shelfObjects.forEach { (phrase, assetPath) ->  // Korrekte Signatur für ohne Index
            val texture = Texture(Gdx.files.internal(assetPath))
            val pixmap = Pixmap(Gdx.files.internal(assetPath))
            val originalWidth = pixmap.width.toFloat()
            val originalHeight = pixmap.height.toFloat()
            pixmap.dispose()
            val targetWidth = 100f
            val aspectRatio = originalHeight / originalWidth
            val targetHeight = targetWidth * aspectRatio

            val resetPositionX = 50f
            val resetPositionY = 750f

            // Regalobjekte haben keine Hanger-Textur
            val currentTexture = texture // Initialisiere currentTexture

            objects.add(
                DraggableObject(
                    phrase = phrase,
                    texture = texture,
                    hangerTexture = null, // KEINE Hanger-Textur für Regalobjekte
                    currentTexture = currentTexture, //aktuelle Textur
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

        bottomObjects.forEachIndexed { index, (phrase, assetPath) ->
            val texture = Texture(Gdx.files.internal(assetPath))
            //Pfad der Hänger-Textur erstellen
            val hangerAssetPath = assetPath.replace(".png", "").replace("phraseImages/", "phraseImages/H_") + ".png"
            Gdx.app.log("DEBUG","Asset Path: $assetPath") // ursprünglichen Asset-Pfad
            Gdx.app.log("DEBUG","Hanger Asset Path: $hangerAssetPath") // generierten Hanger-Pfad
            //Sicherstellen dass die Datei existiert
            val hangerTexture = if (Gdx.files.internal(hangerAssetPath).exists()) Texture(Gdx.files.internal(hangerAssetPath)) else null
            val pixmap = Pixmap(Gdx.files.internal(assetPath))
            val originalWidth = pixmap.width.toFloat()
            val originalHeight = pixmap.height.toFloat()
            pixmap.dispose()
            val aspectRatio = originalHeight / originalWidth
            val targetHeight = bottomTargetWidth * aspectRatio

            // Position aus der Liste bottomPositions nehmen
            val position = bottomPositions[index % bottomPositions.size] //Modulo Operator
            val resetPositionX = position.x
            val resetPositionY = position.y

            val currentTexture = hangerTexture ?: texture // Initialisieren von currentTexture mit der HangerTextur, falls vorhanden

            objects.add(
                DraggableObject(
                    phrase = phrase,
                    texture = texture,
                    hangerTexture = hangerTexture,
                    currentTexture = currentTexture, // aktuelle Textur
                    resetPositionX = resetPositionX,
                    resetPositionY = resetPositionY,
                    basePositionX = resetPositionX,
                    basePositionY = resetPositionY,
                    positionX = 0f,
                    positionY = 0f,
                    positionOffsetX = 0f,
                    positionOffsetY = 0f,
                    sizeX = bottomTargetWidth * 1.1f,
                    sizeY = targetHeight * 1.1f
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

        val (objectsWithoutHanger, objectsWithHanger) = objects.partition { it.hangerTexture == null }

        if (gameStarted) {
            objectsWithHanger.forEach { obj ->
                if (index > 0 && index % 8 == 0) {
                    positionOffsetX = 0f
                    positionOffsetY -= 250f
                }
                if (!obj.isCollected) {
                    obj.positionX = obj.basePositionX + positionOffsetX
                    obj.positionY = obj.basePositionY + positionOffsetY
                    obj.positionOffsetX = positionOffsetX
                    obj.positionOffsetY = positionOffsetY
                    batch.draw(obj.currentTexture, obj.positionX, obj.positionY, obj.sizeX, obj.sizeY)
                }

                index++
                positionOffsetX += 90f

                renderPhrasesOnScreen(
                    batch,
                    font,
                    minigame.bag1,
                    purpleBagPosition.x + 30f,
                    purpleBagPosition.y + purpleBagSize.y - 90f,
                    30f
                )
                renderPhrasesOnScreen(
                    batch,
                    font,
                    minigame.bag2,
                    blueBagPosition.x + 30f,
                    blueBagPosition.y + purpleBagSize.y - 90f,
                    30f
                )
            }

            index = 0
            positionOffsetX = 0f
            positionOffsetY = 0f

            objectsWithoutHanger.forEach { obj ->
                if (index > 0 && index % 8 == 0) {
                    positionOffsetX = 0f
                    positionOffsetY -= 150f
                }
                if (!obj.isCollected) {
                    obj.positionX = obj.basePositionX + positionOffsetX
                    obj.positionY = obj.basePositionY + positionOffsetY
                    obj.positionOffsetX = positionOffsetX
                    obj.positionOffsetY = positionOffsetY
                    batch.draw(obj.currentTexture, obj.positionX, obj.positionY, obj.sizeX, obj.sizeY)
                }

                index++
                positionOffsetX += 50f

                renderPhrasesOnScreen(
                    batch,
                    font,
                    minigame.bag1,
                    purpleBagPosition.x + 30f,
                    purpleBagPosition.y + purpleBagSize.y - 90f,
                    30f
                )
                renderPhrasesOnScreen(
                    batch,
                    font,
                    minigame.bag2,
                    blueBagPosition.x + 30f,
                    blueBagPosition.y + purpleBagSize.y - 90f,
                    30f
                )
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
            shapeRenderer.rect(0f, 0f, viewport.worldWidth, viewport.worldHeight)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
            batch.begin()

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

            batch.draw(
                continueTexture,
                continueButtonPosition.x,
                continueButtonPosition.y - (buttonSize.y / 2) + 15f,
                buttonSize.x,
                buttonSize.y
            )
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
                                //Setze Textur auf normal wenn aufgehoben
                                obj.currentTexture = obj.texture
                                offsetX = mouseX - obj.positionX
                                offsetY = mouseY - obj.positionY
                            }

                            if (obj.isBeingDragged) {
                                obj.basePositionX = (mouseX - offsetX - obj.positionOffsetX)
                                obj.basePositionY = (mouseY - offsetY - obj.positionOffsetY)
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

                                minigame.phraseCheck(obj.phrase, isCorrect)
                            }

                            if (!obj.isCollected) {
                                // Objekt zurück an die Startposition setzen
                                obj.basePositionX = obj.resetPositionX
                                obj.basePositionY = obj.resetPositionY

                                //Setze Textur auf HangerTextur wenn vorhanden, andernfalls normale Textur
                                obj.currentTexture = obj.hangerTexture ?: obj.texture
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
                    game.addScreen(MapScreen(game,  26.5f, 4.6f))
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
                    startX + 320f,
                    (currentY - textHeight / 2 - 1.75f),
                    textWidth,
                    3.5f
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
            obj.positionY < blueBagSize.y
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
        val hangerTexture: Texture?,
        var currentTexture: Texture,
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
