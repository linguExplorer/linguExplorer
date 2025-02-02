package com.github.linguExplorer.screen

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.math.Vector2
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.minigames.FamilieMinigame
import com.github.linguExplorer.models.PhraseEntity
import ktx.app.KtxScreen
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

class MinigameKleidungScreen(private val game: linguExplorer) : KtxScreen {

    private val batch = SpriteBatch()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(800f, 600f)
    private val executor: ExecutorService = Executors.newFixedThreadPool(1)

    private val tagTexture = Texture(Gdx.files.internal("Minigames/Kleidung/tag.png"))
    private val reversedTagTexture = Texture(Gdx.files.internal("Minigames/Kleidung/tag_reversed.png"))
    private val textFieldTexture = Texture(Gdx.files.internal("Minigames/time.png"))
    private var pauseTexture = Texture(Gdx.files.internal("Minigames/pausebutton.png"))
    private var playTexture = Texture(Gdx.files.internal("Minigames/playbutton.png"))

    private val tagSize = Vector2(200f, 90f)
    private val pauseBasePosition = Vector2(180f, 530f)
    private val pauseSize = Vector2(50f, 50f)
    private val timeBasePosition = Vector2(20f, 530f)
    private val timeSize = Vector2(150f, 50f)

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

    private var continueButtonScale = 1f
    private var pauseButtonScale = 1f
    private var continueButtonTargetScale = 1f
    private var pauseButtonTargetScale = 1f
    private val scaleSpeed = 5f
    private var phraseCountMax = 0
    private var phraseCorrectCounter = 0

    private var timeLeft = 30
    private var elapsedTime = 0f
    private var isPaused = false
    private var gameStarted = false
    private var gameEnded = false
    private var isCompleted = false

    private val minigame = FamilieMinigame()
    private var objects: List<Pair<DraggableObject, DraggableObject>> = listOf()
    private var shownPhrases: List<Pair<PhraseEntity, String>>? = null
    private var firstSelected: DraggableObject? = null
    private var incorrectSelectionTimer = 0f // Timer für die rote Umrandung

    override fun show() {
        Gdx.input.inputProcessor = null
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))
        minigame.loadMinigamePhrases()
        minigame.loadAllPhrases()
        phraseCountMax = minigame.phraseList.size

        setObjects()
    }

    private fun positionObjectWithoutOverlap(obj: DraggableObject, usedPositions: List<DraggableObject>, margin: Float) {
        var positionX: Float
        var positionY: Float
        var isOverlapping: Boolean

        do {
            positionX = (Random.nextFloat() + 0.1f) * ((viewport.worldWidth * 0.9f) - tagSize.x)
            positionY = (Random.nextFloat() + 0.1f) * ((viewport.worldHeight * 0.82f) - tagSize.y)

            obj.positionX = positionX
            obj.positionY = positionY

            isOverlapping = usedPositions.any { usedObj ->
                isOverlappingWithMargin(obj, usedObj, margin)
            }

        } while (isOverlapping)
    }

    override fun render(delta: Float) {
        handleInput()
        if (!isPaused && !gameEnded && gameStarted) {
            updateTime(delta)
        }

        // Überprüfe, ob alle Objekte gematcht sind
        if (objects.all { it.first.isMatched && it.second.isMatched }) {

            // Mische die Positionen
            setObjects()
        }

        Gdx.gl.glClearColor(0.6509804f, 0.5882353f, 0.6431373f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        font.color = Color.BLACK

        continueButtonScale += (continueButtonTargetScale - continueButtonScale) * scaleSpeed * delta
        pauseButtonScale += (pauseButtonTargetScale - pauseButtonScale) * scaleSpeed * delta

        batch.begin()

        objects.forEach { (obj1, obj2) ->
            drawDraggableObject(obj1)
            drawDraggableObject(obj2, true)
        }

        batch.draw(
            pauseTexture,
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

        batch.end()
    }

    private fun setObjects() {
        val tempObjects = mutableListOf<Pair<DraggableObject, DraggableObject>>()
        val usedPositions = mutableListOf<DraggableObject>()
        val margin = 20f // Mindestabstand zwischen den Objekten

        shownPhrases = minigame.loadPhrasesWithAssets().take(4)
        minigame.phraseList = minigame.phraseList.drop(4)

        shownPhrases!!.forEach { (phrase, assetPath) ->
            val texture = Texture(Gdx.files.internal(assetPath))
            val sizeX = 40f
            val sizeY = 40f

            val objectEnglish = DraggableObject(
                phrase = phrase,
                texture = texture,
                positionX = 0f,
                positionY = 0f,
                sizeX = sizeX,
                sizeY = sizeY,
                isEnglishPhrase = true
            )

            val objectGerman = DraggableObject(
                phrase = phrase,
                texture = texture,
                positionX = 0f,
                positionY = 0f,
                sizeX = sizeX,
                sizeY = sizeY
            )

            // Positioniere das englische Objekt
            positionObjectWithoutOverlap(objectEnglish, usedPositions, margin)
            usedPositions.add(objectEnglish)

            // Positioniere das deutsche Objekt
            positionObjectWithoutOverlap(objectGerman, usedPositions, margin)
            usedPositions.add(objectGerman)

            tempObjects.add(objectEnglish to objectGerman)
        }

        objects = tempObjects
    }

    private fun drawDraggableObject(obj: DraggableObject, isTranslation: Boolean = false) {
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
            glyphLayout.setText(font, obj.phrase.phrase)
            font.draw(batch, obj.phrase.phrase, obj.positionX + (tagSize.x / 3), obj.positionY + obj.sizeY + glyphLayout.height / 2 + 5f)
        } else {
            glyphLayout.setText(font, obj.phrase.translation)
            font.draw(batch, obj.phrase.translation, obj.positionX + (tagSize.x / 3), obj.positionY + obj.sizeY + glyphLayout.height / 2 + 5f)
        }
    }

    private fun handleInput() {
        if (Gdx.input.justTouched()) {
            val mouseX = Gdx.input.x.toFloat() * viewport.worldWidth / Gdx.graphics.width
            val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.worldHeight / Gdx.graphics.height

            val clickedObject = objects.flatMap { listOf(it.first, it.second) }
                .firstOrNull { obj -> !obj.isMatched && isMouseOverObject(mouseX, mouseY, obj) }

            clickedObject?.let { obj ->
                if (firstSelected == null) {
                    // Erstes Objekt auswählen
                    firstSelected = obj
                    obj.isSelected = true
                } else {
                    // Zweites Objekt auswählen
                    if (firstSelected!!.isEnglishPhrase != obj.isEnglishPhrase) {
                        // Überprüfen, ob die Objekte zusammengehören
                        val partner = getPartner(firstSelected!!)
                        if (partner == obj) {
                            // Richtige Auswahl: Grüne Umrandung
                            firstSelected!!.isMatched = true
                            obj.isMatched = true
                            phraseCorrectCounter++
                            minigame.phraseCheck(firstSelected!!.phrase, true)
                        } else {
                            // Falsche Auswahl: Rote Umrandung für 1 Sekunde
                            firstSelected!!.isWrong = true
                            obj.isWrong = true
                            incorrectSelectionTimer = 1f
                        }

                        firstSelected!!.isSelected = false
                        firstSelected = null
                        obj.isSelected = false
                    } else {
                        // Zwei Objekte der gleichen Sprache ausgewählt: Setze das letzte ausgewählte Objekt als firstSelected
                        firstSelected!!.isSelected = false
                        firstSelected = obj
                        firstSelected!!.isSelected = true
                    }
                }
            } ?: run {
                // Kein Objekt ausgewählt: Auswahl zurücksetzen
                firstSelected?.isSelected = false
                firstSelected = null
            }
        }
    }

    private fun getPartner(obj: DraggableObject): DraggableObject? {
        return objects.firstOrNull { pair -> pair.first == obj || pair.second == obj }
            ?.let { pair -> if (pair.first == obj) pair.second else pair.first }
    }

    private fun isMouseOverObject(mouseX: Float, mouseY: Float, obj: DraggableObject): Boolean {
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

    private fun isOverlappingWithMargin(obj1: DraggableObject, obj2: DraggableObject, margin: Float): Boolean {
        return obj1.positionX < obj2.positionX + tagSize.x + margin &&
            obj1.positionX + tagSize.x + margin > obj2.positionX &&
            obj1.positionY < obj2.positionY + tagSize.y + margin &&
            obj1.positionY + tagSize.y + margin > obj2.positionY
    }

    private data class DraggableObject(
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
