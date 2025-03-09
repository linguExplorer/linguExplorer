package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.event.GameEndEvent
import com.github.linguExplorer.event.fire
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.minigames.SchuleMinigame
import com.github.linguExplorer.models.PhraseEntity
import ktx.app.KtxScreen
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MinigameSchuleScreen() : KtxScreen {

    private val batch = SpriteBatch()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(1920f, 1080f)
    private val shapeRenderer = ShapeRenderer()
    private val executor: ExecutorService = Executors.newFixedThreadPool(1)

    // Texturen
    private val timetableTexture = Texture(Gdx.files.internal("Minigames/Schule/phraseImages/stundenplan_final.png"))
    private val timeTexture = Texture(Gdx.files.internal("Minigames/time.png"))
    private var pauseTexture = Texture(Gdx.files.internal("Minigames/pausebutton.png"))
    private var playTexture = Texture(Gdx.files.internal("Minigames/playbutton.png"))
    private val continueTexture = Texture(Gdx.files.internal("Minigames/btn_continue.png"))
    private val quitButtonTexture = Texture(Gdx.files.internal("Minigames/btn_quitMinigame.png"))

    // Positionen und Größen
    private val timetableBasePosition = Vector2(1250f, 700f)
    private val timetableSize = Vector2(1200f, 730f)

    private val pauseBasePosition = Vector2(180f, 970f)
    private val pauseSize = Vector2(50f, 50f)

    private val buttonSize = Vector2(250f, 70f)
    private val quitButtonBasePosition = Vector2(430f, 130f)
    private val continueButtonBasePosition = Vector2(0f, 175f)

    private val timeBasePosition = Vector2(20f, 970f)
    private val timeSize = Vector2(150f, 50f)

    // Error Text
    private var showErrorText = false
    private var errorTextTimer = 0f
    private val errorTextDuration = 2f
    private var errorTextPositionX = 0f
    private var errorTextPositionY = 0f

    // Getter für die dynamischen Positionen
    private val timetablePosition: Vector2
        get() = Vector2(
            timetableBasePosition.x * (viewport.worldWidth / 1920f),
            timetableBasePosition.y * (viewport.worldHeight / 1080f)
        )

    private val pausePosition: Vector2
        get() = Vector2(
            pauseBasePosition.x,
            pauseBasePosition.y * (viewport.worldHeight / 1080f)
        )

    private val quitButtonPosition: Vector2
        get() = Vector2(
            quitButtonBasePosition.x * (viewport.worldWidth / 1920f),
            quitButtonBasePosition.y * (viewport.worldHeight / 1080f)
        )

    private val timePosition: Vector2
        get() = Vector2(
            timeBasePosition.x,
            timeBasePosition.y * (viewport.worldHeight / 1080f)
        )

    private val continueButtonPosition: Vector2
        get() = Vector2(
            (viewport.worldWidth / 2) - (buttonSize.x / 2),
            continueButtonBasePosition.y * (viewport.worldHeight / 1080f)
        )

    // Button Scaling
    private var continueButtonScale = 1f
    private var pauseButtonScale = 1f
    private var continueButtonTargetScale = 1f
    private var pauseButtonTargetScale = 1f
    private val scaleSpeed = 5f

    // Zeit
    private var timeLeft = 60
    private var elapsedTime = 0f

    // Spielstatus
    private var isDragging = false
    private var offsetX = 0f
    private var offsetY = 0f
    private var gameStarted = false
    private var gameEnded = false
    private var isCompleted = false
    private var isPaused = false

    // Minigame und Assets
    private val minigame = SchuleMinigame()
    private val timetableGrid = mutableListOf<TimetableCell>()
    private val englishSubjects = mutableListOf<DraggableSubject>()

    // Map für Phrase-IDs und ihre zugehörigen Zellen
    private val phraseIdToCells = mutableMapOf<Int, MutableList<TimetableCell>>()

    init {
        // Initialisierung
        minigame.loadMinigamePhrases()
        minigame.loadAllPhrases()

        // Lade Assets
        val germanAssets = minigame.loadGermanAssets()
        println("DONE")
        val englishAssets = minigame.loadEnglishAssets()
        englishAssets.forEach { println(it.second) }

        // Timetable Grid erstellen (5x7 Grid)
        setupTimetableGrid(germanAssets)

        // Englische Assets auf der linken Seite
        setupEnglishSubjects(englishAssets)
    }

    private fun setupTimetableGrid(germanAssets: List<Pair<PhraseEntity, String>>) {
        val gridWidth = 5 //Spalten
        val gridHeight = 7 //Zeilen
        val cellWidth = 195f //width von Zelle im Raster
        val cellHeight = 85f //height von Zelle im Raster
        val startX = timetablePosition.x + timetableSize.x - 300f //x koordinate links oben
        val startY = timetablePosition.y + timetableSize.y + 145f //y koordinate links oben
        val rowSpacing = 4f // Abstand zwischen den Zeilen

        // Erstelle eine Map mit verfügbaren Assets und deren möglicher Anzahl (1-3)
        val availableAssets = mutableMapOf<Pair<PhraseEntity, String>, Int>()
        germanAssets.forEach { asset ->
            val repeatCount = (1..3).random()
            availableAssets[asset] = repeatCount
        }

        // Bereite ein Array für das Grid vor (mit null für leere Zellen)
        val gridCells = Array(gridHeight) { Array<Pair<PhraseEntity, String>?>(gridWidth) { null } }

        // Erstelle Muster für wiederholte Assets
        createPatterns(gridCells, availableAssets)

        // Fülle das Grid mit den Assets
        for (row in 0 until gridHeight) {
            for (col in 0 until gridWidth) {
                val asset = gridCells[row][col]
                if (asset != null) {
                    val (phrase, assetPath) = asset
                    val posX = startX + (col * cellWidth)
                    val posY = startY - (row * (cellHeight + rowSpacing))

                    val cell = TimetableCell(
                        phrase = phrase,
                        texture = Texture(Gdx.files.internal(assetPath)),
                        row = row,
                        col = col,
                        positionX = posX,
                        positionY = posY,
                        width = cellWidth- 1f,
                        height = cellHeight - 1f,
                        occupied = false,
                        correctSubject = null
                    )

                    timetableGrid.add(cell)

                    // Füge Zelle zur Map hinzu für Tracking von mehrfachen Assets
                    if (!phraseIdToCells.containsKey(phrase.id)) {
                        phraseIdToCells[phrase.id] = mutableListOf()
                    }
                    phraseIdToCells[phrase.id]?.add(cell)
                }
            }
        }
    }

    private fun createPatterns(
        gridCells: Array<Array<Pair<PhraseEntity, String>?>>,
        availableAssets: MutableMap<Pair<PhraseEntity, String>, Int>
    ) {
        val gridHeight = gridCells.size + 5f
        val gridWidth = gridCells[0].size + 5f

        // Zufällig entscheiden, wie viele Zellen belegt werden sollen (ca. 60-80%)
        val totalCells = gridHeight * gridWidth
        val filledCellsTarget = (totalCells * (0.6 + Math.random() * 0.2)).toInt()

        // Für jedes Asset mit Anzahl > 1 versuchen, ein Muster zu erstellen
        val assetsToPattern = availableAssets.filter { it.value > 1 }.toMutableMap()

        for ((asset, count) in assetsToPattern) {
            if (count <= 1) continue

            when ((1..3).random()) {
                1 -> createColumnPattern(gridCells, asset, count)
                2 -> createRowPattern(gridCells, asset, count)
                3 -> createRandomPattern(gridCells, asset, count)
            }

            availableAssets.remove(asset)
        }

        var filledCells = gridCells.sumOf { row -> row.count { it != null } }
        val remainingAssets = availableAssets.entries.toMutableList()

        while (filledCells < filledCellsTarget && remainingAssets.isNotEmpty()) {
            val assetEntry = remainingAssets.random()
            val (asset, count) = assetEntry.toPair()

            val emptyCells = getEmptyCells(gridCells)
            if (emptyCells.isEmpty()) break

            val (row, col) = emptyCells.random()
            gridCells[row][col] = asset
            filledCells++

            // Aktualisiere verbleibende Anzahl für dieses Asset
            if (count <= 1) {
                remainingAssets.remove(assetEntry)
            } else {
                remainingAssets.remove(assetEntry)
                remainingAssets.add(object : MutableMap.MutableEntry<Pair<PhraseEntity, String>, Int> {
                    override val key = asset
                    override val value = count - 1
                    override fun setValue(newValue: Int) = count
                })
            }
        }
    }

    private fun createColumnPattern(
        gridCells: Array<Array<Pair<PhraseEntity, String>?>>,
        asset: Pair<PhraseEntity, String>,
        count: Int
    ) {
        val gridHeight = gridCells.size
        val gridWidth = gridCells[0].size

        val colOptions = (0 until gridWidth).toMutableList()
        colOptions.shuffle()

        for (col in colOptions) {
            // Finde freie Positionen in dieser Spalte
            val freePositions = (0 until gridHeight).filter { row -> gridCells[row][col] == null }.toMutableList()

            if (freePositions.size >= min(count, 3)) {
                freePositions.shuffle()
                for (i in 0 until min(count, 3)) {
                    val row = freePositions[i]
                    gridCells[row][col] = asset
                }
                return
            }
        }

        createRandomPattern(gridCells, asset, count)
    }

    private fun createRowPattern(
        gridCells: Array<Array<Pair<PhraseEntity, String>?>>,
        asset: Pair<PhraseEntity, String>,
        count: Int
    ) {
        val gridHeight = gridCells.size
        val gridWidth = gridCells[0].size

        // Wähle eine zufällige Zeile
        val rowOptions = (0 until gridHeight).toMutableList()
        rowOptions.shuffle()

        for (row in rowOptions) {
            // Finde freie Positionen in dieser Zeile
            val freePositions = (0 until gridWidth).filter { col -> gridCells[row][col] == null }.toMutableList()

            if (freePositions.size >= min(count, 3)) {
                freePositions.shuffle()
                // Platziere das Asset in der Zeile
                for (i in 0 until min(count, 3)) {
                    val col = freePositions[i]
                    gridCells[row][col] = asset
                }
                return
            }
        }

        // Fallback: Verwende einen zufälligen Ansatz
        createRandomPattern(gridCells, asset, count)
    }

    private fun createRandomPattern(
        gridCells: Array<Array<Pair<PhraseEntity, String>?>>,
        asset: Pair<PhraseEntity, String>,
        count: Int
    ) {
        val emptyCells = getEmptyCells(gridCells)
        val placementCount = min(count, 3).coerceAtMost(emptyCells.size)

        if (placementCount <= 0) return

        // Platziere das Asset an zufälligen freien Stellen
        for (i in 0 until placementCount) {
            val (row, col) = emptyCells[i]
            gridCells[row][col] = asset
        }
    }

    private fun getEmptyCells(gridCells: Array<Array<Pair<PhraseEntity, String>?>>): List<Pair<Int, Int>> {
        val emptyCells = mutableListOf<Pair<Int, Int>>()

        for (row in gridCells.indices) {
            for (col in gridCells[row].indices) {
                if (gridCells[row][col] == null) {
                    emptyCells.add(Pair(row, col))
                }
            }
        }

        return emptyCells
    }

    private fun min(a: Int, b: Int): Int {
        return if (a < b) a else b
    }

    private fun setupEnglishSubjects(englishAssets: List<Pair<PhraseEntity, String>>) {
        val subjectWidth = 189f
        val subjectHeight = 80f
        val startX = 100f
        val startY = 700f
        val spacing = 220f


        for (i in englishAssets.indices) {
            val row = i / 2
            val col = i % 2
            val (phrase, assetPath) = englishAssets[i]

            val posX = startX + (col * spacing)
            val posY = startY - (row * 100f)

            englishSubjects.add(DraggableSubject(
                phrase = phrase,
                texture = Texture(Gdx.files.internal(assetPath)),
                initialX = posX,
                initialY = posY,
                currentX = posX,
                currentY = posY,
                width = subjectWidth,
                height = subjectHeight,
                isBeingDragged = false,
                isMatched = false
            ))
        }
    }

    override fun show() {
        Gdx.input.inputProcessor = null
    }

    override fun render(delta: Float) {
        handleInput()
        if (!isPaused && !gameEnded && gameStarted) {
            updateTime(delta)
        }

        if (showErrorText) {
            errorTextTimer += delta
            if (errorTextTimer >= errorTextDuration) {
                showErrorText = false
            }
        }

        // Update button scaling
        continueButtonScale += (continueButtonTargetScale - continueButtonScale) * scaleSpeed * delta
        pauseButtonScale += (pauseButtonTargetScale - pauseButtonScale) * scaleSpeed * delta

        // Clear screen
        Gdx.gl.glClearColor(0.611f, 0.761f, 0.827f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        font.color = Color.BLACK

        batch.begin()

        // Render timetable background
        batch.draw(timetableTexture,
            timetablePosition.x - timetableSize.x / 2,
            timetablePosition.y - timetableSize.y / 2,
            timetableSize.x,
            timetableSize.y)

        // Render time
        batch.draw(timeTexture, timePosition.x, timePosition.y, timeSize.x, timeSize.y)
        font.data.setScale(0.3f, 0.3f)
        font.draw(batch, formatTime(timeLeft), timePosition.x + 20f, timePosition.y + timeSize.y / 1.4f)

        // Render pause/play button
        val buttonTexture = if (isPaused || gameEnded) playTexture else pauseTexture
        pauseButtonScale = if (isPaused || gameEnded) 1f else pauseButtonScale
        batch.draw(
            buttonTexture,
            pausePosition.x - (pauseSize.x * (pauseButtonScale - 1f) / 2),
            pausePosition.y - (pauseSize.y * (pauseButtonScale - 1f) / 2),
            pauseSize.x * pauseButtonScale,
            pauseSize.y * pauseButtonScale
        )

        if (gameStarted && !gameEnded) {
            timetableGrid.forEach { cell ->
                // Wenn diese Zelle einen korrekten Subject hat, zeichne den stattdessen
                if (cell.correctSubject != null) {
                    batch.draw(
                        cell.correctSubject!!.texture,
                        cell.positionX,
                        cell.positionY,
                        cell.width,
                        cell.height
                    )
                } else {
                    batch.draw(
                        cell.texture,
                        cell.positionX,
                        cell.positionY,
                        cell.width,
                        cell.height
                    )
                }
            }

            // Render draggable subjects
            englishSubjects.forEach { subject ->
                if (!subject.isMatched) {
                    batch.draw(
                        subject.texture,
                        subject.currentX,
                        subject.currentY,
                        subject.width,
                        subject.height
                    )
                }
            }
        }

        if (isPaused) {
            renderPausedOverlay()
        }

        if (!gameStarted) {
            renderStartScreen()
        }

        if (gameEnded) {
            renderGameEndScreen()
        }

        if (showErrorText) {
            font.color = Color.RED
            font.data.setScale(0.3f, 0.3f)
            font.draw(batch, "Incorrect match!", errorTextPositionX, errorTextPositionY)
        }

        batch.end()
    }

    private fun renderPausedOverlay() {
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

        // Continue button
        batch.draw(
            continueTexture,
            continueButtonPosition.x - (buttonSize.x * (continueButtonScale - 1f) / 2),
            continueButtonPosition.y - (buttonSize.y * (continueButtonScale - 1f) / 2),
            buttonSize.x * continueButtonScale,
            buttonSize.y * continueButtonScale
        )
    }

    private fun renderStartScreen() {
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
        font.data.setScale(0.4f, 0.4f)
        glyphLayout.setText(font, "Match the English subjects with the German ones on the timetable",
            Color.WHITE, viewport.worldWidth * 0.75f, Align.center, true)
        val instructionsX = (viewport.worldWidth - glyphLayout.width) / 2
        val instructionsY = (viewport.worldHeight / 2) + glyphLayout.height
        font.draw(batch, "Match the English subjects with the German ones on the timetable",
            instructionsX, instructionsY, viewport.worldWidth * 0.75f, Align.center, true)

        // Continue button
        batch.draw(
            continueTexture,
            continueButtonPosition.x - (buttonSize.x * (continueButtonScale - 1f) / 2),
            continueButtonPosition.y - (buttonSize.y * (continueButtonScale - 1f) / 2),
            buttonSize.x * continueButtonScale,
            buttonSize.y * continueButtonScale
        )
    }

    private fun renderGameEndScreen() {
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

    private fun handleInput() {
        val mouseX = Gdx.input.x.toFloat() * viewport.worldWidth / Gdx.graphics.width
        val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.worldHeight / Gdx.graphics.height

        // Update button hover effects
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
                    // Handle pause button click
                    if (mouseX in pausePosition.x..(pausePosition.x + pauseSize.x) &&
                        mouseY in pausePosition.y..(pausePosition.y + pauseSize.y) &&
                        !isDragging) {
                        isPaused = !isPaused
                        return
                    }

                    if (!showErrorText) {
                        // Handle dragging subjects
                        englishSubjects.forEach { subject ->
                            if (!isDragging && !subject.isMatched && isPointInsideRect(
                                    mouseX, mouseY,
                                    subject.currentX, subject.currentY,
                                    subject.width, subject.height)) {
                                isDragging = true
                                subject.isBeingDragged = true
                                offsetX = mouseX - subject.currentX
                                offsetY = mouseY - subject.currentY
                            }

                            if (subject.isBeingDragged) {
                                subject.currentX = mouseX - offsetX
                                subject.currentY = mouseY - offsetY
                            }
                        }
                    }
                } else {
                    // Handle continue button click in pause menu
                    if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
                        mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)) {
                        isPaused = !isPaused
                    }
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                isPaused = true
            } else {
                if (!isPaused) {
                    // Handle subject drop
                    englishSubjects.forEach { subject ->
                        if (subject.isBeingDragged) {
                            subject.isBeingDragged = false
                            isDragging = false

                            val cellMatch = findMatchingCell(subject)
                            if (cellMatch != null) {
                                // Check if the subject matches the cell
                                val isCorrect = isCellMatchCorrect(subject, cellMatch)
                                if (isCorrect) {
                                    // Alle Zellen mit der gleichen Phrase aktualisieren
                                    val matchingCells = phraseIdToCells[cellMatch.phrase.id]
                                    matchingCells?.forEach { cell ->
                                        cell.occupied = true
                                        cell.correctSubject = subject
                                    }

                                    // Mark subject as matched
                                    subject.isMatched = true
                                    minigame.phraseCheck(subject.phrase, true)

                                    // Check if game is complete
                                    if (minigame.isGameComplete() || areAllCellsOccupied()) {
                                        gameEnded = true
                                        isCompleted = true
                                    }
                                } else {
                                    // Show error
                                    showErrorText = true
                                    errorTextTimer = 0f

                                    var glyphLayout = GlyphLayout()
                                    font.data.setScale(0.3f, 0.3f)
                                    glyphLayout.setText(font, "Incorrect match!")

                                    errorTextPositionX = (viewport.worldWidth - glyphLayout.width) / 2
                                    errorTextPositionY = viewport.worldHeight / 2

                                    minigame.phraseCheck(subject.phrase, false)

                                    // Reset position
                                    // Show error
                                    showErrorText = true
                                    errorTextTimer = 0f

                                    glyphLayout = GlyphLayout()
                                    font.data.setScale(0.3f, 0.3f)
                                    glyphLayout.setText(font, "Incorrect match!")

                                    errorTextPositionX = (viewport.worldWidth - glyphLayout.width) / 2
                                    errorTextPositionY = viewport.worldHeight / 2

                                    minigame.phraseCheck(subject.phrase, false)

                                    // Reset position
                                    subject.currentX = subject.initialX
                                    subject.currentY = subject.initialY
                                }
                            } else {
                                // Reset position if not dropped on a cell
                                subject.currentX = subject.initialX
                                subject.currentY = subject.initialY
                            }
                        }
                    }
                }
            }
        } else if (!gameStarted) {
            // Handle start game button
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
                    mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)) {
                    gameStarted = true
                }
            }
        } else {
            // Handle end game buttons
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
                    mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)) {
                    storePhraseDataAsync()

                    /*stage.fire(GameEndEvent("SM"))

                    if (game.containsScreen<MapScreen>()) {
                        game.removeScreen<MapScreen>()
                    }
                    game.addScreen(MapScreen(game, 31.104187f, 15.677063f))
                    game.setScreen<MapScreen>()*/
                }
            }
        }
    }

    private fun areAllCellsOccupied(): Boolean {
        // Check if all cells in the timetable grid are occupied
        val uniquePhraseIds = phraseIdToCells.keys

        // For each unique phrase ID, check if all corresponding cells are occupied
        for (phraseId in uniquePhraseIds) {
            val cells = phraseIdToCells[phraseId] ?: continue

            // If any cell for this phrase ID is not occupied, return false
            if (cells.any { !it.occupied }) {
                return false
            }
        }

        // All cells are occupied
        return true
    }


    private fun findMatchingCell(subject: DraggableSubject): TimetableCell? {
        return timetableGrid.find { cell ->
            !cell.occupied && isPointInsideRect(
                subject.currentX + (subject.width / 2),
                subject.currentY + (subject.height / 2),
                cell.positionX, cell.positionY,
                cell.width, cell.height
            )
        }
    }

    private fun isCellMatchCorrect(subject: DraggableSubject, cell: TimetableCell): Boolean {
        // Check if the English subject matches the German cell
        return subject.phrase.id == cell.phrase.id
    }

    private fun isPointInsideRect(x: Float, y: Float, rectX: Float, rectY: Float, rectWidth: Float, rectHeight: Float): Boolean {
        return x >= rectX && x <= rectX + rectWidth && y >= rectY && y <= rectY + rectHeight
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
        shapeRenderer.dispose()
        timetableTexture.dispose()
        timeTexture.dispose()
        pauseTexture.dispose()
        playTexture.dispose()
        continueTexture.dispose()
        quitButtonTexture.dispose()

        // Dispose textures in timetable grid
        timetableGrid.forEach { cell ->
            cell.texture.dispose()
        }

        // Dispose textures in subjects
        englishSubjects.forEach { subject ->
            subject.texture.dispose()
        }

        executor.shutdown()
    }

    // Data classes
    private data class TimetableCell(
        val phrase: PhraseEntity,
        val texture: Texture,
        val row: Int,
        val col: Int,
        val positionX: Float,
        val positionY: Float,
        val width: Float,
        val height: Float,
        var occupied: Boolean,
        var correctSubject: DraggableSubject?
    )

    private data class DraggableSubject(
        val phrase: PhraseEntity,
        val texture: Texture,
        val initialX: Float,
        val initialY: Float,
        var currentX: Float,
        var currentY: Float,
        val width: Float,
        val height: Float,
        var isBeingDragged: Boolean,
        var isMatched: Boolean
    )
}
