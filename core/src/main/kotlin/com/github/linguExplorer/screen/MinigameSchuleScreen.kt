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
    private val timetableBasePosition = Vector2(1250f, 600f)
    private val timetableSize = Vector2(1200f, 730f)

    private val pausePosition: Vector2
        get() = Vector2(310f, viewport.worldHeight - 120f)
    private val pauseSize = Vector2(80f, 80f)
    private val timePosition: Vector2
        get() = Vector2(30f, viewport.worldHeight - 125f)
    private val timeSize = Vector2(260f, 90f)

    // Error Text
    private var showErrorText = false
    private var errorTextTimer = 0f
    private val errorTextDuration = 2f
    private var errorTextPositionX = 0f
    private var errorTextPositionY = 0f

    // Getter für die dynamischen Positionen
    private val timetablePosition: Vector2
        get() = Vector2(
            timetableBasePosition.x,
            timetableBasePosition.y
        )

    private val tryAgainButtonBasePosition = Vector2(430f, 200f)
    private val quitButtonBasePosition = Vector2(430f, 130f)
    private val continueButtonPosition: Vector2
        get() = Vector2(
            (viewport.worldWidth / 2) - (buttonSize.x / 2),
            (viewport.worldHeight - buttonSize.y) / 2 - 50f
        )
    private val buttonSize = Vector2(375f, 105f)



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
        minigame.loadMinigamePhrases()
        minigame.loadAllPhrases()

        // Lade Assets
        val germanAssets = minigame.loadGermanAssets()
        val englishAssets = minigame.loadEnglishAssets()

        // Timetable Grid erstellen (5x7 Grid)
        setupTimetableGrid(germanAssets)

        // Englische Assets auf der linken Seite
        setupEnglishSubjects(englishAssets)
    }

    private fun setupTimetableGrid(germanAssets: List<Pair<PhraseEntity, String>>) {
        val gridWidth = 5 // Spalten
        val gridHeight = 7 // Zeilen
        val cellWidth = 195f
        val cellHeight = 85f
        val startX = timetablePosition.x - (gridWidth * cellWidth) / 2 + 105f
        val startY = timetablePosition.y + (timetableSize.y / 2) - 190f
        val rowSpacing = 4f

        val availableAssets = mutableMapOf<Pair<PhraseEntity, String>, Int>()
        germanAssets.forEach { asset ->
            val repeatCount = (1..3).random()
            availableAssets[asset] = repeatCount
        }

        val gridCells = Array(gridHeight) { Array<Pair<PhraseEntity, String>?>(gridWidth) { null } }
        createPatterns(gridCells, availableAssets)

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
                        width = cellWidth - 1f,
                        height = cellHeight - 1f,
                        occupied = false,
                        correctSubject = null
                    )

                    timetableGrid.add(cell)

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
        val gridHeight = gridCells.size
        val gridWidth = gridCells[0].size
        val totalCells = gridHeight * gridWidth
        val filledCellsTarget = (totalCells * (0.6 + Math.random() * 0.2)).toInt()

        val assetsToPattern = availableAssets.filter { it.value > 1 }.toMutableMap()

        for ((asset, count) in assetsToPattern) {
            if (count <= 1) continue
            distributeAssetRandomly(gridCells, asset, count)
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

    private fun distributeAssetRandomly(
        gridCells: Array<Array<Pair<PhraseEntity, String>?>>,
        asset: Pair<PhraseEntity, String>,
        count: Int
    ) {
        val gridHeight = gridCells.size
        val gridWidth = gridCells[0].size
        val possibleRows = (0 until gridHeight).toMutableList()
        possibleRows.shuffle()

        var placements = 0
        for (row in possibleRows) {
            val possibleCols = (0 until gridWidth).toMutableList()
            possibleCols.shuffle()
            for (col in possibleCols) {
                if (gridCells[row][col] == null) {
                    gridCells[row][col] = asset
                    placements++
                    break
                }
            }
            if (placements >= count) break
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

    private fun setupEnglishSubjects(englishAssets: List<Pair<PhraseEntity, String>>) {
        val subjectWidth = 189f
        val subjectHeight = 80f
        val startX = 100f
        val startY = 800f
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
        font.data.setScale(0.5f, 0.5f)
        font.draw(batch, formatTime(timeLeft), timePosition.x + 42.5f, timePosition.y + 62.5f)

        // Render pause/play button
        val buttonTexture = if (isPaused || gameEnded) playTexture else pauseTexture

        batch.draw(
            buttonTexture,
            pausePosition.x,
            pausePosition.y,
            pauseSize.x,
            pauseSize.y
        )

        if (gameStarted) {
            // Render timetable cells
            timetableGrid.forEach { cell ->
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

    private fun handleInput() {
        val mouseX = Gdx.input.x.toFloat() * viewport.worldWidth / Gdx.graphics.width
        val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.worldHeight / Gdx.graphics.height

        if (!gameEnded && gameStarted) {

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
