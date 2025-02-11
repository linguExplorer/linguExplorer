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
import com.github.linguExplorer.minigames.FamilieMinigame
import com.github.linguExplorer.models.PhraseEntity
import ktx.app.KtxScreen
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

class MinigameFamilieScreen(private val game: linguExplorer) : KtxScreen {

    private val batch = SpriteBatch()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(800f, 600f)
    private val shapeRenderer = ShapeRenderer()
    private val executor: ExecutorService = Executors.newFixedThreadPool(1)

    private val tagTexture = Texture(Gdx.files.internal("Minigames/Kleidung/tag.png"))
    private val reversedTagTexture = Texture(Gdx.files.internal("Minigames/Kleidung/tag_reversed.png"))
    private val textFieldTexture = Texture(Gdx.files.internal("Minigames/time.png"))
    private var pauseTexture = Texture(Gdx.files.internal("Minigames/pausebutton.png"))
    private var playTexture = Texture(Gdx.files.internal("Minigames/playbutton.png"))
    private val continueTexture = Texture(Gdx.files.internal("Minigames/btn_continue.png"))
    private val quitButtonTexture = Texture(Gdx.files.internal("Minigames/btn_quitMinigame.png"))

    private val tagSize = Vector2(200f, 90f)
    private val pauseBasePosition = Vector2(180f, 530f)
    private val pauseSize = Vector2(50f, 50f)
    private val timeBasePosition = Vector2(20f, 530f)
    private val timeSize = Vector2(150f, 50f)
    private val quitButtonBasePosition = Vector2(430f, 130f)
    private val continueButtonBasePosition = Vector2(0f, 175f)
    private val buttonSize = Vector2(250f, 70f)

    private val pausePosition: Vector2
        get() = Vector2(
            pauseBasePosition.x,
            pauseBasePosition.y * (viewport.worldHeight / 600f)
        )

    private val timePosition: Vector2
        get() = Vector2(
            timeBasePosition.x,
            timeBasePosition.y * (viewport.worldHeight / 600f)
        )

    private val quitButtonPosition: Vector2
        get() = Vector2(
            quitButtonBasePosition.x * (viewport.worldWidth / 800f),
            quitButtonBasePosition.y * (viewport.worldHeight / 600f)
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
    private var phraseCountMax = 0
    private var phraseCorrectCounter = 0

    private var timeLeft = 45
    private var elapsedTime = 0f
    private var isPaused = false
    private var gameStarted = false
    private var gameEnded = false
    private var isCompleted = false

    private val minigame = FamilieMinigame()
    private var objects: List<Pair<TagObject, TagObject>> = listOf()
    private var shownPhrases: List<Pair<PhraseEntity, String>>? = null
    private var firstSelected: TagObject? = null
    private var incorrectSelectionTimer = 0f // Timer für die rote Umrandung

    override fun show() {
        Gdx.input.inputProcessor = null
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))
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
                isOverlappingWithMargin(obj, usedObj, 30f)
            }

        } while (isOverlapping)
    }

    override fun render(delta: Float) {
        handleInput()
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

        continueButtonScale += (continueButtonTargetScale - continueButtonScale) * scaleSpeed * delta
        pauseButtonScale += (pauseButtonTargetScale - pauseButtonScale) * scaleSpeed * delta

        batch.begin()

        if (gameStarted) {
            objects.forEach { (obj1, obj2) ->
                drawTagObject(obj1)
                drawTagObject(obj2, true)
            }
        }

        val texture: Texture = if (isPaused || gameEnded) playTexture else pauseTexture
        pauseButtonScale = if (isPaused || gameEnded) 1f else pauseButtonScale

        batch.draw(
            texture,
            pausePosition.x - (pauseSize.x * (pauseButtonScale - 1f) / 2),
            pausePosition.y - (pauseSize.y * (pauseButtonScale - 1f) / 2),
            pauseSize.x * pauseButtonScale,
            pauseSize.y * pauseButtonScale
        )

        batch.draw(textFieldTexture, timePosition.x, timePosition.y, timeSize.x, timeSize.y)
        font.data.setScale(0.3f, 0.3f)
        font.draw(batch, formatTime(timeLeft), timePosition.x + 20f, timePosition.y + timeSize.y / 1.4f)

        batch.draw(textFieldTexture, viewport.worldWidth - timePosition.x - timeSize.x, timePosition.y, timeSize.x, timeSize.y)
        font.draw(batch, "$phraseCorrectCounter/$phraseCountMax", viewport.worldWidth - timePosition.x - timeSize.x + 20f, timePosition.y + timeSize.y / 1.4f)

        if (incorrectSelectionTimer > 0f) {
            incorrectSelectionTimer -= Gdx.graphics.deltaTime
        }

        if (incorrectSelectionTimer <= 0f) {
            objects.forEach { it.first.isWrong = false
                it.second.isWrong = false }
        }

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
            glyphLayout.setText(font, "Match the correct pairs")
            val gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
            val gamePausedY = (viewport.worldHeight / 2) + glyphLayout.height
            font.draw(batch, "Match the correct pairs", gamePausedX, gamePausedY)

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
            }
        }

        batch.end()
    }

    private fun setObjects() {
        val tempObjects = mutableListOf<Pair<TagObject, TagObject>>()
        val usedPositions = mutableListOf<TagObject>()

        if(minigame.isGameComplete()) {
            gameEnded = true
            isCompleted = true
        }

        shownPhrases = minigame.loadPhrasesWithAssets().take(4)
        shownPhrases!!.forEach { println(minigame.phraseList.indexOf(it.first)) }
        minigame.phraseList = minigame.phraseList.drop(4)

        shownPhrases!!.forEach { (phrase, assetPath) ->
            val texture = Texture(Gdx.files.internal(assetPath))
            val sizeX = 40f
            val sizeY = 40f

            val objectEnglish = TagObject(
                phrase = phrase,
                texture = texture,
                positionX = 0f,
                positionY = 0f,
                sizeX = sizeX,
                sizeY = sizeY,
                isEnglishPhrase = true
            )

            val objectGerman = TagObject(
                phrase = phrase,
                texture = texture,
                positionX = 0f,
                positionY = 0f,
                sizeX = sizeX,
                sizeY = sizeY
            )

            // Positioniere das englische Objekt
            positionObjectWithoutOverlap(objectEnglish, usedPositions)
            usedPositions.add(objectEnglish)

            // Positioniere das deutsche Objekt
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
        if (!isTranslation) {
            batch.draw(obj.texture, obj.positionX + 10f, obj.positionY + obj.sizeX / 2 + 5f, obj.sizeX, obj.sizeY)
        }


        font.data.setScale(0.15f, 0.15f)
        val glyphLayout = GlyphLayout()
        if (!isTranslation) {
            if (obj.phrase.phrase.length >= 10) {
                font.data.setScale(0.11f, 0.11f)
            }
            glyphLayout.setText(font, obj.phrase.phrase)
            font.draw(batch, obj.phrase.phrase, obj.positionX + (tagSize.x / 3), obj.positionY + obj.sizeY + glyphLayout.height / 2 + 5f)
        } else {
            glyphLayout.setText(font, obj.phrase.translation)
            font.draw(batch, obj.phrase.translation, obj.positionX + (tagSize.x / 3), obj.positionY + obj.sizeY + glyphLayout.height / 2 + 5f)
        }
    }

    private fun handleInput() {
        val mouseX = Gdx.input.x.toFloat() * viewport.worldWidth / Gdx.graphics.width
        val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.worldHeight / Gdx.graphics.height

        continueButtonTargetScale = if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
            mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)) 1.1f else 1f

        when {
            !gameEnded && gameStarted -> handleGameInput(mouseX, mouseY)
            !gameStarted && Gdx.input.isButtonPressed(Input.Buttons.LEFT) -> handleGameStart(mouseX, mouseY)
            Gdx.input.isButtonPressed(Input.Buttons.LEFT) -> handleGameEnd(mouseX, mouseY)
        }
    }



    private fun handleGameInput(mouseX: Float, mouseY: Float) {
        if (Gdx.input.justTouched()) {
            when {
                !isPaused && isPauseButtonClicked(mouseX, mouseY) -> togglePause()
                !isPaused -> handleObjectSelection(mouseX, mouseY)
                isContinueButtonClicked(mouseX, mouseY) -> togglePause()
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            isPaused = true
        }
    }

    private fun handleGameStart(mouseX: Float, mouseY: Float) {
        if (isContinueButtonClicked(mouseX, mouseY)) {
            gameStarted = true
        }
    }

    private fun handleGameEnd(mouseX: Float, mouseY: Float) {
        if (isContinueButtonClicked(mouseX, mouseY)) {
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

        if (!isCorrectMatch) {
            firstSelected!!.isWrong = true
            obj.isWrong = true
            incorrectSelectionTimer = 1f
        } else {
            phraseCorrectCounter++
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
        firstSelected!!.isSelected = false
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
        game.addScreen(MapScreen(game))
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
        objects.forEach { (obj1, obj2) ->
            obj1.texture.dispose()
            obj2.texture.dispose()
        }
    }

    private fun isOverlappingWithMargin(obj1: TagObject, obj2: TagObject, margin: Float): Boolean {
        return obj1.positionX < obj2.positionX + tagSize.x + margin &&
            obj1.positionX + tagSize.x + margin > obj2.positionX &&
            obj1.positionY < obj2.positionY + tagSize.y + margin &&
            obj1.positionY + tagSize.y + margin > obj2.positionY
    }

    private data class TagObject(
        val phrase: PhraseEntity,
        val texture: Texture,
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
