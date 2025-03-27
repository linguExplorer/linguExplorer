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
import com.badlogic.gdx.utils.Align
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.masterVolume
import com.github.linguExplorer.minigames.FamilieMinigame
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.musicVolume
import com.github.linguExplorer.soundEffectVolume
import ktx.app.KtxScreen
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

class MinigameFamilieScreen(private val game: linguExplorer) : KtxScreen {

    private val batch = SpriteBatch()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(1920f, 1080f)
    private val shapeRenderer = ShapeRenderer()
    private val executor: ExecutorService = Executors.newFixedThreadPool(1)

    // Texturen
    private val tagTexture = Texture(Gdx.files.internal("Minigames/Kleidung/tag.png"))
    private val reversedTagTexture = Texture(Gdx.files.internal("Minigames/Kleidung/tag_reversed.png"))
    private val textFieldTexture = Texture(Gdx.files.internal("Minigames/time.png"))
    private var pauseTexture = Texture(Gdx.files.internal("Minigames/pausebutton.png"))
    private var playTexture = Texture(Gdx.files.internal("Minigames/playbutton.png"))
    private val continueTexture = Texture(Gdx.files.internal("Minigames/btn_continue.png"))
    private val quitButtonTexture = Texture(Gdx.files.internal("Minigames/btn_quitMinigame.png"))

    // Audio
    private var backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Hintergrundmusik/Hintergrundmusik_Familie.mp3"))
    private var correctSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Soundeffekte/richtig.mp3"))
    private var wrongSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Soundeffekte/falsch.mp3"))

    // Skalierung
    private val scaleFactor = 1080f / 600f
    private val tagSize = Vector2(200f * scaleFactor, 90f * scaleFactor)

    // Positionen
    private val pausePosition: Vector2
        get() = Vector2(310f, viewport.worldHeight - 120f)
    private val pauseSize = Vector2(80f, 80f)
    private val continueButtonPosition: Vector2
        get() = Vector2(
            (viewport.worldWidth / 2) - (buttonSize.x / 2),
            (viewport.worldHeight - buttonSize.y) / 2 - 50f
        )
    private val buttonSize = Vector2(375f, 105f)
    private val timePosition: Vector2
        get() = Vector2(30f, viewport.worldHeight - 125f)
    private val timeSize = Vector2(260f, 90f)

    // Spielzustand
    private var phraseCountMax = 0
    private var phraseCorrectCounter = 0
    private var timeLeft = 45
    private var elapsedTime = 0f
    private var isPaused = false
    private var gameStarted = false
    private var gameEnded = false
    private var isCompleted = false
    private var incorrectSelectionTimer = 0f

    // Minigame Logik
    private val minigame = FamilieMinigame()
    private var objects: List<Pair<TagObject, TagObject>> = listOf()
    private var shownPhrases: List<Pair<PhraseEntity, String>>? = null
    private var firstSelected: TagObject? = null

    override fun show() {
        Gdx.input.inputProcessor = null
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))

        // Musik initialisieren
        backgroundMusic.isLooping = true
        backgroundMusic.volume = 0.5f * musicVolume * masterVolume

        minigame.loadMinigamePhrases()
        minigame.loadAllPhrases()
        phraseCountMax = minigame.phraseList.size
        setObjects()
    }

    private fun positionObjectWithoutOverlap(obj: TagObject, usedPositions: List<TagObject>) {
        var positionX: Float
        var positionY: Float
        var isOverlapping: Boolean

        do {
            positionX = (Random.nextFloat() + 0.1f) * ((viewport.worldWidth * 0.9f) - tagSize.x)
            positionY = (Random.nextFloat() + 0.1f) * ((viewport.worldHeight * 0.82f) - tagSize.y)

            obj.positionX = positionX
            obj.positionY = positionY

            isOverlapping = usedPositions.any { usedObj ->
                isOverlappingWithMargin(obj, usedObj, 30f * scaleFactor)
            }
        } while (isOverlapping)
    }

    override fun render(delta: Float) {
        handleInput()

        // Musiksteuerung
        if (isPaused) {
            backgroundMusic.pause()
        } else if (gameStarted && !backgroundMusic.isPlaying) {
            backgroundMusic.play()
        }

        if (!isPaused && !gameEnded && gameStarted) {
            updateTime(delta)
        }

        if (objects.all { it.first.isMatched && it.second.isMatched }) {
            setObjects()
        }

        Gdx.gl.glClearColor(0.6509804f, 0.5882353f, 0.6431373f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        font.color = Color.BLACK

        batch.begin()

        if (gameStarted) {
            objects.forEach { (obj1, obj2) ->
                drawTagObject(obj1)
                drawTagObject(obj2, true)
            }
        }

        val texture: Texture = if (isPaused || gameEnded) playTexture else pauseTexture
        batch.draw(texture, pausePosition.x, pausePosition.y, pauseSize.x, pauseSize.y)

        batch.draw(textFieldTexture, timePosition.x, timePosition.y, timeSize.x, timeSize.y)
        font.data.setScale(0.3f * scaleFactor, 0.3f * scaleFactor)
        font.draw(batch, formatTime(timeLeft), timePosition.x + 15f * scaleFactor, timePosition.y + timeSize.y / 1.4f)

        batch.draw(textFieldTexture, viewport.worldWidth - timePosition.x - timeSize.x, timePosition.y, timeSize.x, timeSize.y)
        font.draw(batch, "$phraseCorrectCounter/$phraseCountMax", viewport.worldWidth - timePosition.x - timeSize.x + 20f * scaleFactor, timePosition.y + timeSize.y / 1.4f)

        if (incorrectSelectionTimer > 0f) {
            incorrectSelectionTimer -= delta
        }

        if (incorrectSelectionTimer <= 0f) {
            objects.forEach {
                it.first.isWrong = false
                it.second.isWrong = false
            }
        }

        renderPauseMenu()
        renderStartMenu()
        renderEndMenu()

        batch.end()
    }

    private fun renderPauseMenu() {
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

            batch.draw(continueTexture, continueButtonPosition.x, continueButtonPosition.y - (buttonSize.y / 2) + 15f, buttonSize.x, buttonSize.y)
        }
    }

    private fun renderStartMenu() {
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
            val text = "Match the German words with their English translations"
            font.draw(batch, text, 0f, viewport.worldHeight / 2 + glyphLayout.height / 2 + 80f, viewport.worldWidth, Align.center, true)

            batch.draw(continueTexture, continueButtonPosition.x, continueButtonPosition.y - (buttonSize.y / 2) + 15f, buttonSize.x, buttonSize.y)
        }
    }

    private fun renderEndMenu() {
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
                batch.draw(continueTexture, continueButtonPosition.x, continueButtonPosition.y - (buttonSize.y / 2) + 15f, buttonSize.x, buttonSize.y)
            } else {
                glyphLayout.setText(font, "GAME OVER")
                val gameOverX = (viewport.worldWidth - glyphLayout.width) / 2
                val gameOverY = (viewport.worldHeight) / 2 + glyphLayout.height + 30f
                font.draw(batch, "GAME OVER", gameOverX, gameOverY)
                batch.draw(quitButtonTexture, continueButtonPosition.x, continueButtonPosition.y - (buttonSize.y / 2) + 15f, buttonSize.x, buttonSize.y)
            }
        }
    }

    private fun setObjects() {
        val tempObjects = mutableListOf<Pair<TagObject, TagObject>>()
        val usedPositions = mutableListOf<TagObject>()

        if (minigame.isGameComplete()) {
            gameEnded = true
            isCompleted = true
            return
        }

        shownPhrases = minigame.loadPhrasesWithAssets().take(4)
        minigame.phraseList = minigame.phraseList.drop(4)

        shownPhrases!!.forEach { (phrase, _) ->
            val objectEnglish = TagObject(
                phrase = phrase,
                positionX = 0f,
                positionY = 0f,
                sizeX = 40f,
                sizeY = 40f,
                isEnglishPhrase = true
            )

            val objectGerman = TagObject(
                phrase = phrase,
                positionX = 0f,
                positionY = 0f,
                sizeX = 40f,
                sizeY = 40f
            )

            positionObjectWithoutOverlap(objectEnglish, usedPositions)
            usedPositions.add(objectEnglish)

            positionObjectWithoutOverlap(objectGerman, usedPositions)
            usedPositions.add(objectGerman)

            tempObjects.add(objectEnglish to objectGerman)
        }

        objects = tempObjects
    }

    private fun drawTagObject(obj: TagObject, isTranslation: Boolean = false) {
        val texture = if (!isTranslation) reversedTagTexture else tagTexture

        val borderColor = when {
            obj.isMatched -> Color.GREEN
            obj.isSelected && incorrectSelectionTimer <= 0f -> Color.YELLOW
            obj.isWrong -> Color.RED
            else -> null
        }

        borderColor?.let {
            val borderWidth = 3f
            batch.color = it
            batch.draw(texture, obj.positionX - borderWidth, obj.positionY - borderWidth, tagSize.x + 2 * borderWidth, tagSize.y + 2 * borderWidth)
            batch.color = Color.WHITE
        }

        batch.draw(texture, obj.positionX, obj.positionY, tagSize.x, tagSize.y)

        font.data.setScale(0.24f, 0.24f)
        val glyphLayout = GlyphLayout()
        val text = if (!isTranslation) obj.phrase.phrase else obj.phrase.translation

        if (text.length >= 8) {
            font.data.setScale(0.21f, 0.21f)
        }

        glyphLayout.setText(font, text)
        val xPos = if (!isTranslation) {
            obj.positionX + 50f
        } else {
            obj.positionX + tagSize.x - glyphLayout.width - 50f
        }

        font.draw(batch, text, xPos, obj.positionY + obj.sizeY + glyphLayout.height / 2 + 40f)
    }

    private fun handleInput() {
        val mouseX = Gdx.input.x.toFloat() * viewport.worldWidth / Gdx.graphics.width
        val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.worldHeight / Gdx.graphics.height

        when {
            !gameEnded && gameStarted -> handleGameInput(mouseX, mouseY)
            !gameStarted && Gdx.input.isButtonPressed(Input.Buttons.LEFT) -> handleGameStart(mouseX, mouseY)
            gameEnded && Gdx.input.isButtonPressed(Input.Buttons.LEFT) -> handleGameEnd(mouseX, mouseY)
            Gdx.input.isKeyPressed(Input.Keys.ESCAPE) -> isPaused = true
        }
    }

    private fun handleGameInput(mouseX: Float, mouseY: Float) {
        if (Gdx.input.justTouched()) {
            when {
                isPauseButtonClicked(mouseX, mouseY) -> togglePause()
                isContinueButtonClicked(mouseX, mouseY) && isPaused -> togglePause()
                !isPaused -> handleObjectSelection(mouseX, mouseY)
            }
        }
    }

    private fun handleGameStart(mouseX: Float, mouseY: Float) {
        if (isContinueButtonClicked(mouseX, mouseY)) {
            gameStarted = true
            backgroundMusic.play()
        }
    }

    private fun handleGameEnd(mouseX: Float, mouseY: Float) {
        if (isContinueButtonClicked(mouseX, mouseY)) {
            backgroundMusic.pause()
            backgroundMusic.stop()
            storePhraseDataAsync()
            transitionToMapScreen()
        }
    }

    private fun handleObjectSelection(mouseX: Float, mouseY: Float) {
        val clickedObject = objects.flatMap { listOf(it.first, it.second) }
            .firstOrNull { obj -> !obj.isMatched && isMouseOverObject(mouseX, mouseY, obj) }

        clickedObject?.let { obj ->
            when {
                firstSelected == null -> selectFirstObject(obj)
                firstSelected!!.isEnglishPhrase != obj.isEnglishPhrase -> checkMatch(obj)
                else -> reselectObject(obj)
            }
        } ?: deselectFirstObject()
    }

    private fun selectFirstObject(obj: TagObject) {
        firstSelected = obj.apply { isSelected = true }
    }

    private fun checkMatch(obj: TagObject) {
        val partner = getPartner(firstSelected!!)
        val isCorrectMatch = partner == obj

        firstSelected!!.isMatched = isCorrectMatch
        obj.isMatched = isCorrectMatch

        val englishObject = if (firstSelected!!.isEnglishPhrase) firstSelected!! else obj
        minigame.phraseCheck(englishObject.phrase, isCorrectMatch)

        if (isCorrectMatch) {
            correctSound.play(0.9f * masterVolume * soundEffectVolume)
            phraseCorrectCounter++
        } else {
            wrongSound.play(2.3f * masterVolume * soundEffectVolume)
            firstSelected!!.isWrong = true
            obj.isWrong = true
            incorrectSelectionTimer = 1f
        }

        resetSelection()
    }

    private fun reselectObject(obj: TagObject) {
        firstSelected!!.isSelected = false
        firstSelected = obj.apply { isSelected = true }
    }

    private fun deselectFirstObject() {
        firstSelected?.isSelected = false
        firstSelected = null
    }

    private fun resetSelection() {
        firstSelected?.isSelected = false
        firstSelected = null
    }

    private fun isPauseButtonClicked(mouseX: Float, mouseY: Float) =
        mouseX in pausePosition.x..(pausePosition.x + pauseSize.x) &&
            mouseY in pausePosition.y..(pausePosition.y + pauseSize.y)

    private fun isContinueButtonClicked(mouseX: Float, mouseY: Float) =
        mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
            mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)

    private fun togglePause() {
        isPaused = !isPaused
    }

    private fun transitionToMapScreen() {
        if (game.containsScreen<MapScreen>()) {
            game.removeScreen<MapScreen>()
        }
        game.removeScreen<MinigameFamilieScreen>()
        game.addScreen(MapScreen(game, 12.5f, 15f))
        game.setScreen<MapScreen>()
    }

    private fun getPartner(obj: TagObject): TagObject? {
        return objects.firstOrNull { pair -> pair.first == obj || pair.second == obj }
            ?.let { pair -> if (pair.first == obj) pair.second else pair.first }
    }

    private fun isMouseOverObject(mouseX: Float, mouseY: Float, obj: TagObject): Boolean {
        return mouseX >= obj.positionX && mouseX <= obj.positionX + tagSize.x &&
            mouseY >= obj.positionY && mouseY <= obj.positionY + tagSize.y
    }

    private fun updateTime(delta: Float) {
        elapsedTime += delta
        if (elapsedTime >= 1f && timeLeft > 0) {
            timeLeft--
            elapsedTime = 0f
        } else if (timeLeft <= 0) {
            gameEnded = true
            backgroundMusic.stop()
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

    override fun hide() {
        backgroundMusic.stop()
    }

    override fun pause() {}
    override fun resume() {}

    override fun dispose() {
        batch.dispose()
        font.dispose()
        tagTexture.dispose()
        reversedTagTexture.dispose()
        textFieldTexture.dispose()
        pauseTexture.dispose()
        playTexture.dispose()
        continueTexture.dispose()
        quitButtonTexture.dispose()
        backgroundMusic.dispose()
        correctSound.dispose()
        wrongSound.dispose()
    }

    private fun isOverlappingWithMargin(obj1: TagObject, obj2: TagObject, margin: Float): Boolean {
        return obj1.positionX < obj2.positionX + tagSize.x + margin &&
            obj1.positionX + tagSize.x + margin > obj2.positionX &&
            obj1.positionY < obj2.positionY + tagSize.y + margin &&
            obj1.positionY + tagSize.y + margin > obj2.positionY
    }

    private data class TagObject(
        val phrase: PhraseEntity,
        var positionX: Float,
        var positionY: Float,
        var sizeX: Float,
        var sizeY: Float,
        var isEnglishPhrase: Boolean = false,
        var isSelected: Boolean = false,
        var isMatched: Boolean = false,
        var isWrong: Boolean = false
    )
}
