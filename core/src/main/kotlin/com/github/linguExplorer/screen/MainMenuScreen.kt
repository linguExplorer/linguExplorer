package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.models.CheckpointEntity
import com.github.linguExplorer.models.UserEntity
import com.github.linguExplorer.repositories.CheckpointRepository
import com.github.linguExplorer.repositories.TopicRepository
import com.github.linguExplorer.repositories.UserProgressRepository
import com.github.linguExplorer.repositories.UserRepository
import com.github.linguExplorer.saveNumber
import com.github.linguExplorer.userId
import java.awt.Desktop
import java.net.URI
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.max

class MainMenuScreen(private val game: linguExplorer) : KtxScreen {
    private var batch: SpriteBatch = SpriteBatch()
    private var shapeRenderer: ShapeRenderer = ShapeRenderer()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(1920f, 1080f)
    private val glyphLayout = GlyphLayout()
    private lateinit var music: Music

    private var backgroundTexture: Texture = Texture("xx_map_assets/Map/ref.png")
    private var startNewGameTexture: Texture = Texture("xx_Images/Buttons/neuesSpiel_green.png")
    private var loadGameTexture: Texture = Texture("xx_Images/Buttons/spielstandLaden_green.png")
    private var settingsIconTexture: Texture = Texture("xx_Images/SettingsIcon.png")
    private var wordmarkTexture: Texture = Texture("xx_Images/wordmark/wordmark_scaled.png")
    private var popupTexture: Texture = Texture("box.png")
    private var exitTexture: Texture = Texture("xx_Images/Buttons/red_X.png")
    private val executor: ExecutorService = Executors.newFixedThreadPool(1)
    private var showPopup = false
    private var showMenu = false
    private var newGamePopUp = false
    private var loadGamePopUp = false
    private var threadExecuted = false
    private var loadingTime = 0f
    private var userFound = false
    private var userAlreadyExists = false
    private var loadGame = false
    private var newGame = false
    private lateinit var user: UserEntity
    private lateinit var currentTopic: String
    private lateinit var currentCheckpoint: CheckpointEntity
    val saveSlots = mutableListOf<Triple<UserEntity?, CheckpointEntity?, String?>>()
    private var loadingScreenRenderer = LoadingScreenRenderer()
    private val gameMenuRenderer = GameMenuRenderer()
    private var executePositionX = 0f
    private var executePositionY = 0f

    private val popUpSize = Vector2(1000f, 600f)
    private val exitSize = 90f
    private val popUpPosition: Vector2
        get() = Vector2(
            (viewport.worldWidth / 2 - popUpSize.x / 2),
            (viewport.worldHeight / 2 - popUpSize.y / 2)
        )

    private val textureAtlas = TextureAtlas("graphics/idle_animation.atlas")
    private val playerTexture: Texture = Texture("graphics/idle_animation.png")
    private lateinit var gifAnimation: Animation<TextureRegion>
    private var animationTime = 0f

    private var dotAnimationTime = 0f
    private var dotCount = 0

    private val overlayWidth = 1200f
    private val overlayHeight = 700f
    private val settingsIconSize = 80f
    private var backgroundOffsetX = 0f
    private var backgroundOffsetY = 0f
    private var speedX = 100f
    private var speedY = 50f

    private var isTransitioning = false
    private var transitionRadius = 0f
    private var transitionCenter = Vector2()
    private val maxRadius: Float
        get() = max(viewport.worldWidth, viewport.worldHeight) * 1.5f

    override fun show() {
        music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Hintergrundmusik/Hintergrundmusik_linguExplorer.mp3"))
        music.isLooping = true
        music.volume = 0.5f
        music.play()
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))
        gifAnimation = Animation(0.1f, textureAtlas.regions, Animation.PlayMode.LOOP)
    }

    override fun render(delta: Float) {
        viewport.apply()
        val screenWidth = viewport.worldWidth
        val screenHeight = viewport.worldHeight

        val mousePos = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).also {
            viewport.unproject(it)
        }

        val backgroundWidth = backgroundTexture.width.toFloat()
        val backgroundHeight = backgroundTexture.height.toFloat()
        val scale = 1.5f
        val scaleX = screenWidth / backgroundWidth * scale
        val scaleY = screenHeight / backgroundHeight * scale
        val scaleFactor = Math.max(scaleX, scaleY)

        val scaledWidth = backgroundWidth * scaleFactor
        val scaledHeight = backgroundHeight * scaleFactor

        transitionCenter.set(viewport.worldWidth / 2f, viewport.worldHeight / 2f)

        backgroundOffsetX += speedX * delta
        backgroundOffsetY += speedY * delta

        if (backgroundOffsetX < -(scaledWidth - screenWidth)) {
            backgroundOffsetX = -(scaledWidth - screenWidth)
            speedX = -speedX
        } else if (backgroundOffsetX > 0) {
            backgroundOffsetX = 0f
            speedX = -speedX
        }

        if (backgroundOffsetY < -(scaledHeight - screenHeight)) {
            backgroundOffsetY = -(scaledHeight - screenHeight)
            speedY = -speedY
        } else if (backgroundOffsetY > 0) {
            backgroundOffsetY = 0f
            speedY = -speedY
        }

        batch.projectionMatrix = viewport.camera.combined
        shapeRenderer.projectionMatrix = viewport.camera.combined

        batch.begin()
        batch.draw(backgroundTexture, backgroundOffsetX, backgroundOffsetY, scaledWidth, scaledHeight)
        batch.end()

        val overlayColor = Color(0f, 0f, 0f, 0.65f)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = overlayColor
        val overlayX = screenWidth / 2 - overlayWidth / 2
        val overlayY = screenHeight / 2 - overlayHeight / 2
        shapeRenderer.rect(overlayX, overlayY, overlayWidth, overlayHeight)
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        batch.begin()
        val wordmarkHeight = 220f
        val wordmarkAspectRatio = wordmarkTexture.width.toFloat() / wordmarkTexture.height.toFloat()
        val wordmarkWidth = wordmarkHeight * wordmarkAspectRatio
        val wordmarkX = screenWidth / 2 - 560f
        val wordmarkY = screenHeight / 2 + 20f
        batch.draw(wordmarkTexture, wordmarkX, wordmarkY, wordmarkWidth, wordmarkHeight)

        val buttonX = screenWidth / 2 - 500f
        val startNewGameButtonY = screenHeight / 2 - 90f
        val loadGameButtonY = screenHeight / 2 - 205f

        batch.draw(startNewGameTexture, buttonX, startNewGameButtonY)
        batch.draw(loadGameTexture, buttonX, loadGameButtonY)

        val settingsX = screenWidth - settingsIconSize - 10f
        val settingsY = screenHeight - settingsIconSize - 10f
        batch.draw(settingsIconTexture, settingsX, settingsY, settingsIconSize, settingsIconSize)

        animationTime += delta
        val currentFrame = gifAnimation.getKeyFrame(animationTime)
        batch.draw(currentFrame, screenWidth / 2 + 180f, screenHeight / 2 - 200f, 300f, 300f)
        batch.end()

        if (showPopup) {
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.5f)
            shapeRenderer.rect(0f, 0f, screenWidth, screenHeight)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
            batch.begin()
            batch.draw(popupTexture, popUpPosition.x, popUpPosition.y, popUpSize.x, popUpSize.y)
            batch.draw(exitTexture, popUpPosition.x + popUpSize.x - 120f, popUpPosition.y + popUpSize.y - 100f, exitSize, exitSize)
            font.color = Color.BLACK
            var gamePausedX: Float
            var gamePausedY: Float

            if (Gdx.input.justTouched() && !isTransitioning) {
                if (isMouseInArea(mousePos.x, mousePos.y, popUpPosition.x + popUpSize.x - 120f, popUpPosition.y + popUpSize.y - 100f, exitSize, exitSize)) {
                    if (userAlreadyExists) {
                        userAlreadyExists = false
                    } else {
                        showPopup = false
                        loadGamePopUp = false
                        newGamePopUp = false
                        loadGame = false
                        newGame = false
                    }
                }
            }

            if (threadExecuted) {
                if (userAlreadyExists) {
                    font.data.setScale(0.4f, 0.4f)
                    glyphLayout.setText(font, "USER EXISTIERT!")
                    gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
                    gamePausedY = (viewport.worldHeight + glyphLayout.height) / 2
                    font.draw(batch, "USER EXISTIERT!", gamePausedX, gamePausedY)
                } else if (loadGamePopUp && !userFound) {
                    font.data.setScale(0.4f, 0.4f)
                    glyphLayout.setText(font, "Kein Spielstand vorhanden!")
                    gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
                    gamePausedY = (viewport.worldHeight + glyphLayout.height) / 2
                    font.draw(batch, "Kein Spielstand vorhanden!", gamePausedX, gamePausedY)
                } else if (userFound || newGamePopUp) {
                    font.data.setScale(0.3f, 0.3f)
                    val slotHeight = 120f
                    val spacing = 20f
                    val startY = popUpPosition.y + popUpSize.y - 140f

                    font.color = Color.BLACK
                    glyphLayout.setText(font, "Wähle einen Spielstand")
                    font.draw(batch, "Wähle einen Spielstand",
                        popUpPosition.x + (popUpSize.x - glyphLayout.width) / 2,
                        startY + 50f)

                    for ((index, slot) in saveSlots.withIndex()) {
                        val (user, checkpoint, topic) = slot

                        val slotY = startY - (slotHeight + spacing) * index
                        val slotX = popUpPosition.x + 60f
                        val slotWidth = popUpSize.x - 120f

                        val isMouseOver = isMouseInArea(mousePos.x, mousePos.y, slotX, slotY - slotHeight, slotWidth, slotHeight)
                        val slotIsActive = user != null

                        Gdx.gl.glEnable(GL20.GL_BLEND)
                        batch.end()
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                        shapeRenderer.color = when {
                            isMouseOver && slotIsActive -> Color(0.9f, 0.9f, 0.7f, 0.7f)
                            slotIsActive -> Color(0.8f, 0.8f, 0.8f, 0.7f)
                            else -> Color(0.6f, 0.6f, 0.6f, 0.5f)
                        }
                        shapeRenderer.rect(slotX, slotY - slotHeight, slotWidth, slotHeight)
                        shapeRenderer.end()
                        Gdx.gl.glDisable(GL20.GL_BLEND)

                        Gdx.gl.glLineWidth(3f)
                        Gdx.gl.glEnable(GL20.GL_BLEND)
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                        shapeRenderer.color = when {
                            isMouseOver && (slotIsActive || newGamePopUp) -> Color.YELLOW
                            slotIsActive -> Color(0.2f, 0.2f, 0.2f, 1f)
                            else -> Color(0.4f, 0.4f, 0.4f, 0.5f)
                        }
                        shapeRenderer.rect(slotX, slotY - slotHeight, slotWidth, slotHeight)
                        shapeRenderer.end()
                        batch.begin()
                        Gdx.gl.glDisable(GL20.GL_BLEND)
                        Gdx.gl.glLineWidth(1f)

                        font.color = Color.BLACK

                        if (!slotIsActive) {
                            font.data.setScale(0.3f, 0.3f)
                            glyphLayout.setText(font, "Leerer Spielstand")
                            font.draw(batch, "Leerer Spielstand",
                                slotX + (slotWidth - glyphLayout.width) / 2,
                                slotY - slotHeight/2 + glyphLayout.height/2)
                        } else {
                            font.data.setScale(0.25f, 0.25f)
                            val textStartY = slotY - 20f

                            glyphLayout.setText(font, "Name: ${user!!.name}")
                            font.draw(batch, "Name: ${user.name}", slotX + 20f, textStartY)

                            val formattedTime = checkpoint?.currentTime?.atZone(ZoneId.systemDefault())
                                ?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) ?: "-"
                            glyphLayout.setText(font, "Speicherzeit: $formattedTime")
                            font.draw(batch, "Speicherzeit: $formattedTime", slotX + 20f, textStartY - 30f)

                            glyphLayout.setText(font, "Aktuelles Thema: $topic")
                            font.draw(batch, "Aktuelles Thema: $topic", slotX + 20f, textStartY - 60f)
                        }

                        if (isMouseOver && Gdx.input.justTouched() && (slotIsActive || newGamePopUp)) {
                            if (user != null && newGamePopUp) {
                                userAlreadyExists = true
                            } else {
                                if (!newGamePopUp) {
                                    this.user = user!!
                                    currentCheckpoint = checkpoint!!
                                    currentTopic = topic!!
                                    loadGame = true
                                } else {
                                    saveNumber = index + 1
                                    newGame = true
                                }
                                isTransitioning = true
                                loadingTime = 0f
                            }
                        }
                    }
                    batch.draw(
                        currentFrame,
                        popUpPosition.x + popUpSize.x - 250f,
                        viewport.worldHeight / 2 - 250f,
                        200f,
                        200f
                    )
                }
            } else {
                loadingTime += delta

                font.data.setScale(0.4f, 0.4f)
                dotAnimationTime += delta
                if (dotAnimationTime >= 0.5f) {
                    dotAnimationTime = 0f
                    dotCount = (dotCount + 1) % 4
                }

                val animatedDots = ".".repeat(dotCount)
                val loadingText = when {
                    loadingTime >= 30f -> "Versuch es erneut :("
                    loadingTime >= 20f -> "Only a few more seconds$animatedDots"
                    loadingTime >= 10f -> "Lean back and wait$animatedDots"
                    else -> "Loading$animatedDots"
                }

                glyphLayout.setText(font, loadingText)
                gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
                gamePausedY = (viewport.worldHeight + glyphLayout.height) / 2
                font.draw(batch, loadingText, gamePausedX, gamePausedY)
            }

            batch.end()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !isTransitioning ) {
            userAlreadyExists = false
            showPopup = false
            loadGamePopUp = false
            newGamePopUp = false
            loadingTime = 0f
        }

        if (isTransitioning) {
            transitionRadius += 1500 * delta
            if (transitionRadius >= maxRadius) {
                loadingTime += delta
                if (loadGamePopUp || newGamePopUp) {
                    loadCheckpoint()
                    loadGamePopUp = false
                    newGamePopUp = false
                }

                loadingScreenRenderer.renderAnimatedText(batch, font, glyphLayout, viewport,"Loading", delta, 1f, true)
                if (threadExecuted && loadingTime > 3f) {
                    println("HUH")
                    Gdx.app.postRunnable {
                        isTransitioning = false
                        transitionRadius = 0f
                        loadingTime = 0f
                        game.setScreen<MapScreen>()
                    }
                }
                return
            }

            if(music.volume > 0.005f) {
                music.volume -= 0.002f
            } else if (music.volume <= 0.005f) {
                music.volume = 0f
            }

            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color.BLACK
            shapeRenderer.circle(transitionCenter.x, transitionCenter.y, transitionRadius)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
        }

        if (showPopup) {
            return
        }

        if (showMenu) {
            gameMenuRenderer.renderGameMenu(batch, font, glyphLayout, viewport)
            return
        }

        if (Gdx.input.justTouched()) {
            if (isMouseInArea(mousePos.x, mousePos.y, settingsX, settingsY, settingsIconSize, settingsIconSize)) {
                showMenu = true
                return
            }

            if (isMouseInArea(mousePos.x, mousePos.y, buttonX, startNewGameButtonY, startNewGameTexture.width.toFloat(), startNewGameTexture.height.toFloat())) {
                showPopup = true
                newGamePopUp = true
                checkUser()
            }

            if (isMouseInArea(mousePos.x, mousePos.y, buttonX, loadGameButtonY, loadGameTexture.width.toFloat(), loadGameTexture.height.toFloat())) {
                showPopup = true
                loadGamePopUp = true
                checkUser()
            }
        }
    }

    private fun checkUser() {
        threadExecuted = false
        executor.submit {
            if(!userFound) {
                val allUsers = UserRepository().getAllUsersById(userId)
                val allCheckpoints = CheckpointRepository().getAllCheckpointsForUser(userId) ?: emptyList()

                saveSlots.clear() // Clear existing slots

                for (i in 0 until 3) { // Maximal 3 Speicherstände
                    if (i < allUsers.size) {
                        val user = allUsers[i]
                        val checkpoint = allCheckpoints.getOrNull(i)
                        val latestProgress = UserProgressRepository().getLatestUserProgress(user.id, user.saveNumber)
                        val topic = latestProgress?.let { TopicRepository().getTopicById(it.topicId)?.name } ?: "-"

                        saveSlots.add(Triple(user, checkpoint, topic))
                    } else {
                        saveSlots.add(Triple(null, null, null))
                    }
                }

                userFound = saveSlots.any { it.first != null }
            }
            threadExecuted = true
        }
    }


    private fun loadCheckpoint() {
        threadExecuted = false
        executor.submit {
            if(newGame) {
                UserRepository().addUser(userId, saveNumber)
                CheckpointRepository().addCheckpoint(userId, saveNumber, 0, 30f, 30f, Timestamp(System.currentTimeMillis()))
                executePositionX = 30f
                executePositionY = 30f
            } else {
                CheckpointRepository().updateCheckpoint(user.id, user.saveNumber, null, null, null, Timestamp(System.currentTimeMillis()))
                saveNumber = user.saveNumber
                executePositionX = currentCheckpoint.positionX
                executePositionY = currentCheckpoint.positionY
                println (executePositionX)
            }
            println("HI")
            newGame = false
            loadGame = false
            println(saveNumber)
            Gdx.app.postRunnable {
                game.addScreen(MapScreen(game, executePositionX, executePositionY))
            }
            threadExecuted = true
            println("Done")
        }
    }

    private fun isMouseInArea(mouseX: Float, mouseY: Float, areaX: Float, areaY: Float, areaWidth: Float, areaHeight: Float): Boolean {
        return mouseX >= areaX && mouseX <= areaX + areaWidth &&
            mouseY >= areaY && mouseY <= areaY + areaHeight
    }

    private fun openWebpage(url: String) {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI(url))
        } else {
            println("Desktop-Modus nicht unterstützt.")
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        playerTexture.disposeSafely()
        textureAtlas.disposeSafely()
        batch.dispose()
        shapeRenderer.dispose()
        backgroundTexture.dispose()
        startNewGameTexture.dispose()
        loadGameTexture.dispose()
        settingsIconTexture.dispose()
        wordmarkTexture.dispose()
        popupTexture.dispose()
    }

    companion object {
        private val log = logger<MapScreen>()
    }
}
